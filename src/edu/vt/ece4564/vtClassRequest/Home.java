package edu.vt.ece4564.vtClassRequest;

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
 *  @version Nov 27, 2013
 */

public class Home extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        responce.getWriter().write("hey");
    }
}
