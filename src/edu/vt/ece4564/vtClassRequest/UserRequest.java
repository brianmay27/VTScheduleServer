package edu.vt.ece4564.vtClassRequest;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        String username = request.getParameter("username");
        char[] password = request.getParameter("passwd").toCharArray();
        int minCredit = Integer.valueOf(request.getParameter("min"));
        int maxCredit = Integer.valueOf(request.getParameter("max"));
        String major = request.getParameter("major");
        try
        {
            Student student = Main.sqlC.getStudent(username);
            if (student == null) {
                student = new Student(username, String.valueOf(password.hashCode()), major);
            } else {
                if (!student.getPasswordHash().equals(new String(password).hashCode())) {
                    //Password is bad
                }
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {

    }
}
