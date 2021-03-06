package edu.vt.ece4564.vtClassRequest;

import edu.vt.ece4564.shared.Schedule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasypt.util.text.BasicTextEncryptor;

// -------------------------------------------------------------------------
/**
 *  Class used to recieve requests from users and begin the correct processing
 *
 *  @author Brian
 *  @version Nov 17, 2013
 */

public class UserRequest extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        String mode = request.getParameter("mode");
        BasicTextEncryptor encrypter = new BasicTextEncryptor();
        encrypter.setPassword("Sj872!=nc>S2%whs6");
        if (mode.equals("request")) {
            String username = request.getParameter("username");
            String encryptedPasswd = request.getParameter("passwd");
            char[] password = encrypter.decrypt(encryptedPasswd).toCharArray();
            int minCredit = Integer.valueOf(request.getParameter("min"));
            int maxCredit = Integer.valueOf(request.getParameter("max"));
            String major = request.getParameter("major");
            String useDars = request.getParameter("usedars");
            ArrayList<String> aditCourse = new ArrayList<>();
            int i = 0;
            String clas;
            while ((clas = request.getParameter("class" + String.valueOf(i++))) != null) {
                aditCourse.add(clas);
            }
            System.out.println("Got a request");
            try
            {
                Student student = Main.sqlC.getStudent(username);
                if (student == null) {
                    student = new Student(username, password.hashCode(), major);
                } else {
                    if (student.getPasswordHash() != password.hashCode()) {
                        //Password is bad
                    }
                }
                try
                {
                    getDARS dars = new getDARS(student, username.toCharArray(), password, minCredit, maxCredit, major, aditCourse, Boolean.getBoolean(useDars));
                    responce.setStatus(HttpServletResponse.SC_OK);
                    responce.getWriter().write(String.valueOf(dars.id) +"\r");
                    responce.flushBuffer();
                    responce.getWriter().close();
                    System.out.println(dars.id);
                    Thread thread = new Thread(dars);
                    thread.run();
                }
                catch (LoginException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (mode.equals("grab")) {
            String username = request.getParameter("username");
            String encryptedPasswd = request.getParameter("passwd");
            char[] password = encrypter.decrypt(encryptedPasswd).toCharArray();
            String loc = request.getParameter("id");
            long id;

            int section = Integer.valueOf(request.getParameter("loc"));
            try
            {
                Student student = Main.sqlC.getStudent(username);
                if (student == null)
                {
                    // do something like return nothing or null or yeah
                }
                else
                {
                    if (student.getPasswordHash() != password.hashCode())
                    {
                        // Password is bad
                    }
                }
                ArrayList<Schedule> schedule;
                if (loc.equals("last")) {
                    schedule = student.getLastSchedule();
                } else {
                     id = Long.valueOf(request.getParameter("id"));
                    schedule = student.getSchedules(id);
                }
                byte[] data = null;
                if (schedule != null)
                {
                    // return null or something
                    ArrayList<Schedule> retVal;
                    if (schedule.size() > (section * 50) + 50) {
                    retVal = new ArrayList<>(schedule.subList(section * 50, (section * 50) + 50));
                    } else {
                        retVal = new ArrayList<>(schedule.subList(section * 50, schedule.size()));
                    }
                    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutput out = new ObjectOutputStream(bos))
                            {
                            out.writeObject(retVal);
                            data = bos.toByteArray();
                            }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                }
                responce.getOutputStream().write(data);
                responce.getOutputStream().flush();
                responce.getOutputStream().close();


            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (mode.equals("process")) {
            String username = request.getParameter("username");
            String ID = request.getParameter("id");
            try {
                Student student = Main.sqlC.getStudent(username);
                if (student == null) {
                    responce.sendError(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
                if (student.getSchedules(Long.valueOf(ID)) != null) responce.getWriter().write("Success");
                else responce.getWriter().write("Working");
                responce.getWriter().flush();
                responce.getWriter().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {

    }

}
