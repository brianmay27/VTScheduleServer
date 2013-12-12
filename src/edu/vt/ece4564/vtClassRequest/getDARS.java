package edu.vt.ece4564.vtClassRequest;

import javax.rmi.CORBA.Tie;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.http.auth.InvalidCredentialsException;
import javax.servlet.http.HttpServletResponse;
import edu.vt.ece4564.shared.Course;
import edu.vt.ece4564.shared.Schedule;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

// -------------------------------------------------------------------------
/**
 *  Provides the a runnable way to fetch and return all classes needed and the
 *  Necessary information about them.
 *
 *  @author Brian
 *  @version Nov 10, 2013
 */

public class getDARS implements Runnable
{
    private static final String submittedDars = "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_CheckAudit";
    private static final String requestWhatIf = "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_SubmitAudit?whatif_parm=Y";
    private static final String runWhatIf = "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_RunAudit";
    private char[] username, password;
    private CASManager cas;
    private String idm;
    private String sess;
    private Student student;
    private String major;
    private int min;
    private int max;
    private boolean gotNewWhatIf = false;
    private ArrayList<Schedule> schedules = null;
    protected long id;
    private ArrayList<String> aditCourse;
    private boolean useDars;
    /**
     *
     * Create a new getDARS object.
     * @param student the student whos request this is
     * @param userName users username
     * @param password users password not encrypted
     * @param min minimum number of credits user wants to take
     * @param max maximum number of credits the user want to take
     * @param major their major in a DARS formated string
     * @throws LoginException
     */
    public getDARS(Student student, char[] userName, char[] password, int min, int max, String major, ArrayList<String> aditCourse, boolean useDars) throws LoginException {
        this.student = student;
        cas = new CASManager();
        cas.setCredentials(userName, password);
        cas.startSession();
        id = Calendar.getInstance().getTimeInMillis();
        idm = cas.getCookies().get("IDMSESSID");
        sess = cas.getCookies().get("SESSID");
        this.max = max;
        this.min = min;
        this.major = major;
        this.aditCourse = aditCourse;
        this.useDars = useDars;
    }
    @Override
    public void run()
    {
        try
        {
            if (student.getLatestDars() == null) {
                requestWhatIf(sess, idm, major);
                while (!gotNewWhatIf) {
                    Thread.sleep(500);
                }
            }
            else if (student.getClassesNeeded() == null) {
                ParseDars dars = new ParseDars(student.getLatestDars());
                student.setClassesNeeded(dars);
            }
                ArrayList<Course> coursesAll = new ArrayList<>();
                ExecutorService threads = Executors.newFixedThreadPool(10);
                ArrayList<TimetableScraper> thread = new ArrayList<>();
                for (String course : student.getClassesNeeded().arrayPreBrian) {
                    String[] split = course.split(" ");
                    TimetableScraper time = new TimetableScraper(split[1].toUpperCase(), split[2], "201401");
                    thread.add(time);
                    threads.submit(time);
                }

                for (String str : aditCourse) {
                    String[] split = str.split(" ");
                    TimetableScraper time = new TimetableScraper(split[0].toUpperCase(), split[1], "201401");
                    thread.add(time);
                    threads.submit(time);
                }
                threads.shutdown();
                while (!threads.isTerminated()) {
                    Thread.sleep(500);
                }
                for (TimetableScraper scraper : thread) {
                    ArrayList<Course> courses = scraper.getCourses();
                    if (courses == null) continue;
                    coursesAll.addAll(courses);
                    //System.out.println(split[1] + " " + split[2]);
                    for (Course timeCourse: courses) {
                        System.out.println("  : " + timeCourse.toString());
                    }
                }
                ArrayList<Course> removedNN = removeClassesWOPre(coursesAll, student.getClassesNeeded().arrayTakenCourse);
                schedules = Scheduler.makeSchedules(min, max, removedNN);
                student.putSchedules(schedules, id);
                System.out.println("Done processing");
        }
        catch (Exception e)
        {
            if (e instanceof InvalidCredentialsException) {

            } else {
                e.printStackTrace();
            }

        }

    }
    private ArrayList<Course> removeClassesWOPre(ArrayList<Course> needed, ArrayList<Course> taken) {
        ArrayList<Course> ret = new ArrayList<>();
        for (Course course : needed) {
            boolean allContained = true;
            if (course.getPrereqs() != null && course.getPrereqs().get(0) != null) {
                for (ArrayList<Course> prerec : course.getPrereqs()) {
                    boolean orTaken = false;
                    for (Course pre : prerec) {
                        if (taken.contains(pre)) {
                            orTaken = true;
                            break;
                        }
                    }
                    if (orTaken == false) allContained = false;
                }
            }
            if (allContained) ret.add(course);
        }
        return ret;
    }
    private void requestWhatIf(String SessionId, String IdmSession, String Major) throws Exception {
        HttpsURLConnection conn = (HttpsURLConnection)new URL(runWhatIf).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Referer", "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_WhatIf");
        conn.setRequestProperty("Cookie", IdmSession + "; " + SessionId);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", "75");
        conn.setRequestProperty("User-Agent", CASManager.USER_AGENTS);
        conn.setDoOutput(true);
        conn.connect();
        BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        //szvinst_ident=AAARvfAAhAAAAQDAAA&fdprog=BSCPECPE+++++++201009&use_planned=Y
        for (int addPlus = 15 - Major.length(); addPlus > 0; addPlus--) Major += "+";
        writter.write("szvinst_ident=AAARvfAAhAAAAQDAAA&fdprog=" + Major + "201009&use_planned=Y");
        writter.flush();
        writter.close();
        String responce = conn.getResponseMessage();
        conn.disconnect();
        if (responce.equals(HttpServletResponse.SC_FORBIDDEN)) throw new InvalidCredentialsException("Invalid password");
        requestCheckDars.scheduleAtFixedRate(checkDars, 10000, 15000);
    }
    private Timer requestCheckDars = new Timer();

    private TimerTask checkDars = new TimerTask() {

        @Override
        public void run()
        {
            try {
                HttpsURLConnection conn = (HttpsURLConnection) new URL(submittedDars).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", CASManager.USER_AGENTS);
                conn.setRequestProperty("Cookie", idm + "; " + sess);
                conn.setRequestProperty("Referer", "https://banweb.banner.vt.edu/ssb/prod/twbkwbis.P_GenMenu?name=bmenu.P_DarsMnu");
                conn.connect();
                String url = getHtml(conn);
                String[] dars = url.split("<TR ALIGN=\"DEFAULT\">");
                if (dars.length <= 1)
                    return;
                Pattern darUrlPattern = Pattern.compile(".*href=\"JavaScript:mywindowOpen\\('(https://webapps.banner.vt.edu/dars/bar\\?jobQSeqNo=\\d+&job_id=[^']+)'\\)\">.*");
                Matcher darsUrlmatcher = darUrlPattern.matcher(dars[1]);
                Pattern darsDatePattern = Pattern.compile(".*<TD width=\"20%\" CLASS=\"dedefault\">(\\w{3} \\d{1,2}, \\d{4}) \\d{1,2}:\\d{2}\\w{2}</TD>.*");
                Matcher darsDateMatcher = darsDatePattern.matcher(dars[1]);
                String DarUrl = null;
                if (darsUrlmatcher.matches() && darsDateMatcher.matches()) {
                    DarUrl= darsUrlmatcher.group(1);
                    Date date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(darsDateMatcher.group(1));
                    Date today = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH).getTime();
                    String todayStr = new SimpleDateFormat("MM dd yyyy").format(today);
                    String dateStr = new SimpleDateFormat("MM dd yyyy").format(date);
                    if (todayStr.equals(dateStr)) {
                        requestCheckDars.cancel();
                        processWhatIf(sess, idm, DarUrl);
                        gotNewWhatIf = true;
                    }
                    System.out.println(DarUrl);
                } else {
                    //nothing matched
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    };

    private void processWhatIf(String sessID, String IDMSess, String url) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", CASManager.USER_AGENTS);
            conn.setRequestProperty("Cookie", idm + "; " + sess);
            conn.setRequestProperty("Referer", "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_CheckAudit");
            conn.connect();
            String html = getHtml(conn);
            student.setLatestDars(html);
            //Main.sqlC.updateStudent(student);
            ParseDars dars = new ParseDars(html);
            student.setClassesNeeded(dars);
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * getHtml()
     *
     * Builds the HTML string
     *
     * @param conn - The HTTP Connection object
     * @return the HTML in a string
     * @throws IOException
     */
    private String getHtml(HttpsURLConnection conn) throws IOException {
        // Build the html string
        StringBuilder html = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String buffer;
        while((buffer = in.readLine()) != null)
            html.append(buffer);
        return html.toString();
    }

}
