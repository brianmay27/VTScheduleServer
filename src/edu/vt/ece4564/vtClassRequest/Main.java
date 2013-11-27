package edu.vt.ece4564.vtClassRequest;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import java.io.ObjectInputStream;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import java.sql.ResultSet;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
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

public class Main extends HttpServlet
{
    protected HashMap<String, Student> users;
    protected static SqlConnection sqlC = new SqlConnection();
    public Main(char[] user, char[] password) {
        try
        {
            Student student = new Student("bmac", "shxn", "BSCPECPE");
            try
            {
                sqlC.addStudent(student);
                student = sqlC.getStudent("bmac");

            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (student != null) {
                getDARS dars = new getDARS(student, user, password);
                Thread thead = new Thread(dars);
                thead.run();
            }

        }
        catch (LoginException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        /*try
        {
            Main m = new Main(args[0].toCharArray(), args[1].toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Server server = new Server(8081);
//        WebAppContext content = new WebAppContext();
//        content.setWar("war");
//        content.setContextPath("/");
//        server.setHandler(content);
//        server.start();
//        server.join();
		*/
    	
    	// Add courses
    	ArrayList<Course> courses = TimetableScraper.getCourses("ECE", "1574", "201401");
		courses.addAll(TimetableScraper.getCourses("ECE", "2014", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "2054", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "2504", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "2534", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "2704", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "3106", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "4124", "201401"));
		courses.addAll(TimetableScraper.getCourses("ECE", "4514", "201401"));
		System.out.println("Got all courses\n");
		
		// Return schedules between 12 and 19 credits
		ArrayList<Schedule> schedules = Scheduler.makeSchedules(12, 19, courses);
		
		//print schedules
		for(Schedule s : schedules) {
			System.out.println(s + "\n");
		}
    }

}
