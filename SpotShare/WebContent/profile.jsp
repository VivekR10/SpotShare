<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="Database.Database"%>
<%@ page import="Classes.User"%>
<%@ page import="Classes.PinnedLocation"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ArrayList" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/profile.css">
		<title>Test</title>
	</head>
	<body>
		<%
		  //retrieving loggedInUser information to display loggedInUser profile
		  Database dbConn = (Database) session.getAttribute("dbConn");
		  String loggedInUser = (String) session.getAttribute("loggedInUser");
		  
		  /*Database dbConn = new Database();
		  HttpSession sessionn = request.getSession();
		  sessionn.setAttribute("dbConn", dbConn);
		  String loggedInUser = "joannlin";*/
		
		  String username = request.getParameter("username");
		  User profileUser = dbConn.getUserOfUsername(username);
		  String name = profileUser.getName();
		  String image = profileUser.getImage();
		
		  int numFollowers = dbConn.getFollowerUsernamesOfUsername(username).size();
		  int numFollowing = dbConn.getFollowingUsernamesOfUsername(username).size();
		
		  boolean foreignUser = true;
		
		  if (loggedInUser.equals(username) || loggedInUser.equals("guest")) {
		    foreignUser = false;
		  }
		
		  boolean following = false;
		  String follow = "";
		
		  if (foreignUser) {
		    if (dbConn.getFollowerUsernamesOfUsername(username).contains(loggedInUser)) {
		      following = true;
		    }
		
		    if (following) {
		      follow = "UNFOLLOW";
		    } else {
		      follow = "FOLLOW";
		    }
		  }
		
		  SimpleDateFormat formatDate = new SimpleDateFormat ("MMMM dd");
		  ArrayList<PinnedLocation> profilePinnedLocations = dbConn.getPinnedLocationsOfUsername(username);
		%>
		<div id="profile-content">
			<a id="exit" href="#" onClick="closeProfile()">x</a>
			<div id="profile-info" class="<%=profileUser.getUsername()%>">
			  <div id="profile-info-left" class="profile-info-item">
			    <span id="num-followers"><%=numFollowers %></span> <br/>Followers
			  </div>
			  <div id="profile-info-middle" class="profile-info-item">
			    <div id="profile-info-pic-container">
			        <img id="profile-info-pic" src="<%=image %>"/>
			    </div>
			    <div id="profile-info-name-container">
			      <div id="profile-info-name">
			        <%=name %>
			        </div>
			    </div>
			    <div id="profile-info-follow-container" class="<%=loggedInUser%>">
			      <a id="profile-follow-status" href="#" onClick="updateFollowing()" style="text-decoration: none; color: #c6c5b9;"><%=follow %></a>
			    </div>
			  </div>
			  <div id="profile-info-right" class="profile-info-item">
			      <%=numFollowing %> <br/>Following
			  </div>
			</div>
			<div id="profile-feed-container" class="profile-item">
			  <div id="profile-feed" class="profile-bottom-item">
			  <% for (PinnedLocation pl : profilePinnedLocations) {
			    out.print("<table class=\"profile-feed-item\">");
			      out.print("<tr>");
			      out.print("<td class=\"profile-feed-item-name\">");
			      out.print("<span class=\"emphasis\">" + name + "</span> pinned</td>");
			      out.print("<td class=\"profile-feed-item-date\">" + formatDate.format(pl.getDatePinned()) + "</td>");
			      out.print("</tr>");
			      out.print("<tr>");
			      out.print("<td class=\"profile-feed-item-location\">");
			      out.print("<span class=\"emphasis\">" + pl.getName() + "</span></td>");
			    out.print("</tr>");
			  out.print("</table>");
			  }%>
			  </div>
			</div>
		</div>
		
		<script>
				function updateFollowing() {
					var followButton = document.getElementById("profile-follow-status")
					var followStatus = followButton.innerHTML
					var loggedInUser = document.getElementById("profile-info-follow-container").className
					var profileUser = document.getElementById("profile-info").className

					var xhttp = new XMLHttpRequest();
					var requestText = "./FollowServlet?fs="
					
					if (followStatus == "FOLLOW") {
						console.log(loggedInUser + " is trying to follow " + profileUser)
						followButton.innerHTML = "UNFOLLOW";
						requestText += "FOLLOW"
					}
					
					if (followStatus == "UNFOLLOW") {
						console.log(loggedInUser + " is trying to unfollow " + profileUser)
						followButton.innerHTML = "FOLLOW";
						requestText += "UNFOLLOW"
					}
					
					requestText += "&c=" + loggedInUser + "&f=" + profileUser
					
					xhttp.open("GET", requestText, false)
					xhttp.send()
					
					console.log(xhttp.responseText);
					
					if (xhttp.responseText.trim().length > 0) {
						document.getElementById("num-followers").innerHTML = xhttp.responseText;
					}
					location.reload(true);
				}
				
				function closeProfile() {
					document.getElementById("profile-content").style.display = "none";
				}
		</script>
	</body>
</html>
