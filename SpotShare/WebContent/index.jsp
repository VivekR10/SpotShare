<!-- SpotShare by Brooks Klinker, Joann Lin, Robert Diersing, Drew Perlman, and Vivek Ramachandran -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="Database.Database"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" type="image/ico" href="img/favicon.png"/><!-- http://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/map-marker-icon.png -->
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>SpotShare | Login or Sign Up</title>

<%

	if(session.getAttribute("dbConn") == null)
	{
		Database conn  = new Database();
		session.setAttribute("dbConn", conn);	
	}
	
	String error = (String) session.getAttribute("error");
%>
</head>
<body>

 <div id = "content"> 

<div id = "loginbox">
<h1>Login</h1>
<form id = "login" action = "LoginHandle" method = "POST">

 <input type = "text" name = "username" placeholder="Username" required>
 <input type = "password" name = "password" placeholder="Password" required>
 <input type="submit" value="Login">
 
 <div class="error">
 
 </div>

</form>

<form id="guestLogin" action="guestLoginHandle" method = "POST">
	<input type="submit" name="LoginAsGuest" value="Login as Guest">
</form>

</div>


<div id = "loginbox">
<h1>Sign Up</h1>

<form id = "signup" action = "SignUpHandle" method = "POST">


<input type = "text" name = "username" placeholder="Username" required>
 <input type = "password" name = "password" placeholder="Password" required>
 <input type = "text" name = "fname" placeholder="First Name" required>
<input type = "text" name = "lname" placeholder="Last Name" required>
<input type = "text" name = "image" placeholder="Image URL">
<input type = "submit" value="Sign Up">

</form>

 </div>
 <div>
 <% if (error!=null) 
 {
	 
 %>
 	<%=error%>
 
 	<%
	session.removeAttribute("error");	
 }  %>
 </div>
</div>
</body>
</html>