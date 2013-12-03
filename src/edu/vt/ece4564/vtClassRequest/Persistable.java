package edu.vt.ece4564.vtClassRequest;

import java.sql.SQLException;
import java.io.Serializable;

// -------------------------------------------------------------------------
/**
 *  Used to help make parsisting the student class easy
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
