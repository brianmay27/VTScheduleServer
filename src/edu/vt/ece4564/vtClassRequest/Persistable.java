package edu.vt.ece4564.vtClassRequest;

import java.sql.SQLException;
import java.io.Serializable;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Brian
 *  @version Nov 20, 2013
 */

public abstract class Persistable
{
    public Persistable() {
    }
    public void Persist(Serializable clas) {
        if (clas instanceof Student) {
            try
            {
                if (!Main.sqlC.updateStudent((Student)clas)) {
                    Main.sqlC.addStudent((Student)clas);
                }
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
