package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Database.Database;

/**
 * Servlet implementation class guestLoginHandle
 */
@WebServlet("/guestLoginHandle")
public class guestLoginHandle extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	session.setAttribute("loggedInUser", "guest");
    	session.setAttribute("error", "");
    	
    	try {
			response.sendRedirect("home.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
