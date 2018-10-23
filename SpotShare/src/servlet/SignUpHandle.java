package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Database.Database;

/**
 * Servlet implementation class SignUpHandle
 */
@WebServlet("/SignUpHandle")
public class SignUpHandle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	// Make new user
    	HttpSession session = request.getSession();
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String fname = request.getParameter("fname");
    	String lname = request.getParameter("lname");
    	String image = request.getParameter("image");
    	Database conn = (Database) session.getAttribute("dbConn");
    	String error = null;
    	
    	if (username == null || password == null || fname == null || lname == null || image == null) {
    		error = "One or more of the required data fields are not filled in";
    		
			session.setAttribute("error", error);
			try {
				response.sendRedirect("index.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
    	}
    	
    	if(conn.checkUsernameExists(username))
    	{
    		error = "Username already exists";
    		session.setAttribute("error", error);

    	}
    	else
    	{
    		conn.insertUser(username, password.hashCode(), fname, lname, image);
    		error = "Successfully added user!";
    		session.setAttribute("error", error);
    		
    	}
    	try {
			response.sendRedirect("index.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
    	
    	// set user data fields, set passcode to passcode.getHash()
    }
}
