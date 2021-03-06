package edu.vt.ece4564.vtClassRequest;

import java.util.HashMap;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// -------------------------------------------------------------------------
/**
 * Used to connect the the SQL database and allow methods
 * to read and write to the database to save and restore info
 *
 *  @author Brian
 *  @version Nov 17, 2013
 */

public class SqlConnection
{
    private Connection c;
    /**
     * Create a new SqlConnection object.
     */
    public SqlConnection() {
        try
        {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:VTClass.db");
            createTable();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Gets all the students in the database
     * @return map of student pid to their class
     * @throws SQLException
     */
    protected HashMap<String, Student> getStudents() throws SQLException {
        Statement stmt = null;
        ResultSet result = null;
        try
        {
            HashMap<String, Student> students = new HashMap<>();
            stmt = c.createStatement();
            String sql = "SELECT * FROM student";
            result = stmt.executeQuery(sql);
            do {
                Student student;
                byte[] data = result.getBytes("data");
                ByteInputStream bis = new ByteInputStream(data, data.length);
                student = (Student)new ObjectInputStream(bis).readObject();
                students.put(student.getPid(), student);
            } while (result.next());
            return students;


        }
        catch (SQLException | ClassNotFoundException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            result.close();
            stmt.close();
        }
        return new HashMap<>();
    }

    private void createTable() throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS student " +
            "(id TEXT PRIMARY KEY NOT NULL," +
            "data TEXT NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("created table");
    }

    /**
     * Add a student to the sql
     * @param student the student to add
     * @return false if the user exists already or it failed and true if it added the user
     * @throws SQLException
     */
    public boolean addStudent(Student student) throws SQLException {
        PreparedStatement stmt = null;
        try
        {
            if (studentExists(student))
                return false;
            stmt = c.prepareStatement("INSERT INTO student (id, data) VALUES (?,?)");
            byte[] data = null;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
                out.writeObject(student);
                data = bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (data == null)
                return false;
            stmt.setString(1, student.getPid());
            stmt.setBytes(2, data);
            stmt.executeUpdate();
            stmt.close();
            return true;
        } finally {
            if (stmt != null)
                stmt.close();
        }

    }
    private boolean studentExists(Student student) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = c.prepareStatement("SELECT * FROM student WHERE id=?");
            stmt.setString(1, student.getPid());
            result = stmt.executeQuery();
            try {
                int n = result.getBytes("data").length;
            } catch (Exception e) {
                return false;
            }

        } catch (Exception e) {

        } finally {
                try
                {
                    result.close();
                    stmt.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        return true;
    }
    /**
     * replaces a student in the database
     * @param student student to save
     * @return false if failed or student does not exist, true if it passed
     * @throws SQLException
     */
    public boolean updateStudent(Student student) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet result = null;
        try
        {
            try {
                stmt = c.prepareStatement("SELECT * FROM student WHERE id=?");
                stmt.setString(1, student.getPid());
                result = stmt.executeQuery();
                try {
                    int n = result.getBytes("data").length;
                }catch (Exception e) {
                    return false;
                }

            } finally {
                result.close();
                stmt.close();
            }
            stmt = c.prepareStatement("UPDATE student SET data=? WHERE id=?");
            byte[] data = null;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
                out.writeObject(student);
                data = bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (data == null)
                return false;
            stmt.setString(2, student.getPid());
            stmt.setBytes(1, data);
            stmt.executeUpdate();
            stmt.close();
            return true;
        }
        finally {
            result.close();
            stmt.close();
        }

    }

    /**
     * Gets a student account
     * @param name the PId of the user
     * @return the student
     * @throws SQLException
     */
    public Student getStudent(String name) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet result = null;
        try
        {
            stmt = c.prepareStatement("SELECT * FROM student WHERE id=?");
            stmt.setString(1, name);
            result = stmt.executeQuery();
            byte[] studentInfo;
            if ((studentInfo = result.getBytes("data")).length != 0) {
                Student student;
                ByteInputStream bis = new ByteInputStream(studentInfo, studentInfo.length);
                student = (Student)new ObjectInputStream(bis).readObject();
                return student;
            }

        }
        catch (ClassNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            return null;
        } finally {
            result.close();
            stmt.close();
        }
        return null;

    }
}
