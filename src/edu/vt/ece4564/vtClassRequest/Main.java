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
 *  Starts the server!
 *
 *  @author Brian
 *  @version Nov 17, 2013
 */

public class Main extends HttpServlet
{
    protected HashMap<String, Student> users;
    protected static final SqlConnection sqlC = new SqlConnection();

//    public Main(char[] user, char[] password) {
//        try
//        {
//            Student student = new Student("bmac", "shxn".hashCode(), "BSCPECPE");
//            try
//            {
//                sqlC.addStudent(student);
//                student = sqlC.getStudent("bmac");
//
//            }
//            catch (SQLException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            if (student != null) {
//                getDARS dars = new getDARS(student, user, password, 12, 16);
//                Thread thead = new Thread(dars);
//                thead.run();
//            }
//
//        }
//        catch (LoginException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    /**
     *Starting point
     * @param args none needed
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
       try
        {
            //Main m = new Main(args[0].toCharArray(), args[1].toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server server = new Server(8081);
        WebAppContext content = new WebAppContext();
        content.setWar("war");
        content.setContextPath("/");
        server.setHandler(content);
        server.start();
        server.join();

    }

}
