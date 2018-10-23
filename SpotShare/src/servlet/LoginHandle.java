package servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Database.Database;

/**
 * Servlet implementation class LoginHandle
 */
@WebServlet("/LoginHandle")
public class LoginHandle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	String error = null;
    	HttpSession session = request.getSession();
    	if(request.getParameter("username") == null || request.getParameter("password") == null)
    	{
    		error = "Please enter a username and password";
    		session.setAttribute("error", error);
    		try {
				response.sendRedirect("index.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return;
    	}
    	
    	
    	Database conn = (Database) session.getAttribute("dbConn");	

    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	String nextPage;
    	nextPage="index.jsp";
    	// check if username exists
    	// check if password is usernames hashed password
		System.out.println("Checking if username exists");

    	if(conn.checkUsernameExists(username)){
    		System.out.println("Username found");
    		System.out.println("password entered:" + password);
			System.out.println("password hash:" + password.hashCode());
			System.out.println("stored hash:" + conn.getPasswordOfUsername(username));

    		if(conn.getPasswordOfUsername(username) == password.hashCode())
    		{
    			
    			nextPage = "home.jsp";
    	    	session.setAttribute("loggedInUser", username);
    		}
    		else
    		{
    			error = "Either your username or password are incorrect";
        		session.setAttribute("error", error);

    		}
    			
    			
    	}
    	else
    	{
    		error = "Either your username or password are incorrect";
    		session.setAttribute("error", error);

    	}
    	try {
			response.sendRedirect(nextPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// if password & username are valid
    	// loggedInUser = user
    	
    	// else error = "Either your username or password are incorrect";
    	// nextPage = "/index.jsp";
    	
    	
    	
    	/*RequestDispatcher dispatch = getServletContext().getRequestDispatcher(nextPage);
    	try {
			dispatch.forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    	
    }

}
