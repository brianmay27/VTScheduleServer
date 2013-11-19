package edu.vt.ece4564.vtClassRequest;

import java.net.ProtocolException;
import java.util.Timer;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.TimerTask;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
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
    public getDARS(Student student, char[] userName, char[] password) throws LoginException {
        this.student = student;
        cas = new CASManager();
        cas.setCredentials(userName, password);
        cas.startSession();
        idm = cas.getCookies().get("IDMSESSID");
        sess = cas.getCookies().get("SESSID");
    }
    @Override
    public void run()
    {
        try
        {
            requestWhatIf(sess, idm, "BSCPECPE");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    protected void requestWhatIf(String SessionId, String IdmSession, String Major) throws Exception {
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
        writter.write("szvinst_ident=AAARvfAAhAAAAQDAAA&fdprog=" + Major + "+++++++201009&use_planned=Y");
        writter.flush();
        String responce = conn.getResponseMessage();
        conn.disconnect();
        requestCheckDars.scheduleAtFixedRate(checkDars, 10000, 15000);
    }
    protected Timer requestCheckDars = new Timer();

    protected TimerTask checkDars = new TimerTask() {

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
                String[] dars = url.split("<a STYLE=\"text-decoration: none\" onMouseOver='window.status=\"View Audit Results\";return true'onMouseOut='window.status=\"Done\";return true'");
                if (dars.length <= 1)
                    return;
                Pattern darUrlPattern = Pattern.compile(".*href=\"JavaScript:mywindowOpen\\('(https://webapps.banner.vt.edu/dars/bar\\?jobQSeqNo=\\d+&job_id=[^']+)'\\)\">BACHELOR OF SCIENCE IN COMPUTER ENGINEERING</a>.*");
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

    protected void processWhatIf(String sessID, String IDMSess, String url) {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", CASManager.USER_AGENTS);
            conn.setRequestProperty("Cookie", idm + "; " + sess);
            conn.setRequestProperty("Referer", "https://banweb.banner.vt.edu/ssb/prod/hzsksaud.P_CheckAudit");
            conn.connect();
            String html = getHtml(conn);
            ParseDars dars = new ParseDars(html);
        } catch (Exception e) {

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
