package edu.vt.ece4564.vtClassRequest;

import org.jasypt.util.text.BasicTextEncryptor;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.OutputStream;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
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
            char[] password = encryptedPasswd.toCharArray();//encrypter.decrypt(encryptedPasswd).toCharArray();
            int minCredit = Integer.valueOf(request.getParameter("min"));
            int maxCredit = Integer.valueOf(request.getParameter("max"));
            String major = request.getParameter("major");
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
                    getDARS dars = new getDARS(student, username.toCharArray(), password, minCredit, maxCredit);
                    responce.getWriter().write(String.valueOf(dars.id));
                    responce.flushBuffer();
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
            char[] password = encryptedPasswd.toCharArray(); //encrypter.decrypt(encryptedPasswd).toCharArray();
            int id = Integer.valueOf(request.getParameter("id"));
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
                ArrayList<Schedule> schedule = student.getSchedules(id);
                if (schedule == null)
                {
                    // return null or something
                }
                List<Schedule> retVal =
                    schedule.subList(section * 5, (section * 5) + 4);
                BufferedWriter writter =
                    new BufferedWriter(new OutputStreamWriter(
                        responce.getOutputStream()));
                String data = "";
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutput out = new ObjectOutputStream(bos))
                {
                    out.writeObject(retVal);
                    data = bos.toString();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                writter.write(data);
                writter.flush();
                writter.close();

            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {

    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
//        String username = request.getParameter("username");
//        char[] password = request.getParameter("passwd").toCharArray();
//        int id = Integer.valueOf(request.getParameter("id"));
//        int section = Integer.valueOf(request.getParameter("loc"));
//        try {
//            Student student = Main.sqlC.getStudent(username);
//            if (student == null) {
//                //do something like return nothing or null or yeah
//            } else {
//                if (student.getPasswordHash() != password.hashCode()) {
//                    //Password is bad
//                }
//            }
//            ArrayList<Schedule> schedule = student.getSchedules(id);
//            if (schedule == null) {
//                //return null or something
//            }
//            List<Schedule> retVal = schedule.subList(section * 5, (section * 5) + 4);
//            BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(responce.getOutputStream()));
//            String data = "";
//            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
//                out.writeObject(retVal);
//                data = bos.toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            writter.write(data);
//            writter.flush();
//            writter.close();
//
//        }
//        catch (SQLException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
