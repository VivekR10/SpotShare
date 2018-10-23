package servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Database.Database;

/**
 * Servlet implementation class FollowServlet
 */
@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter();
		
		Database dbConn = (Database) session.getAttribute("dbConn");
		
		String loggedInUser = request.getParameter("c"); 
		String foreignUser = request.getParameter("f");
		String followStatus = request.getParameter("fs");
		
		System.out.println(loggedInUser);
		
		if (followStatus.equals("FOLLOW")) {
			dbConn.insertFollowingRelationship(loggedInUser, foreignUser);
		}
		
		if (followStatus.equals("UNFOLLOW")) {
			dbConn.deleteFollowingRelationship(loggedInUser, foreignUser);
		}
		
		int newNumFollowers = dbConn.getFollowerUsernamesOfUsername(foreignUser).size();
		
		pw.print(newNumFollowers);
		
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
