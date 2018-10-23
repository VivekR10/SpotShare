package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QueryUsersServlet
 */
@WebServlet("/QueryUsersServlet")
public class QueryUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public void service(HttpServletRequest request, HttpServletResponse response) {
    	String searchInput = request.getParameter("search_input");
    	ArrayList<String> queriedUsers = new ArrayList<String>(); 
    	
    	String[] searchInputArray = searchInput.split(" ");
    	if (searchInputArray.length == 1) {
    		// search w/ fname, lname, username
    	} else if (searchInputArray.length == 2) {
    		// search w/ fname, lname & lname, fname
    	} else {
    		// TODO: make some popup error
    	}
    	
    	request.getSession().setAttribute("queriedUsers", queriedUsers);
    }

}
