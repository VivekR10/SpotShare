<%@ page language="java" contentType="text/html; charset=ISO-8859-1"

    pageEncoding="ISO-8859-1" import = "Classes.User, Database.Database, java.util.ArrayList, Database.JSONParse, org.json.JSONArray" %>

    
    


 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link href="css/jquery-ui.css" rel="stylesheet">
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/jquery-ui.js" defer></script>
 



	
<%
	Database conn  = (Database) session.getAttribute("dbConn");
	ArrayList<User> allUsers = conn.getAllUsers();
	String loggedInUsername = (String) session.getAttribute("loggedInUser");
	JSONArray array= JSONParse.parseUsers(allUsers);
	User loggedInUser = conn.getUserOfUsername(loggedInUsername);
	String loggedInUserImage = loggedInUser.getImage();
	String error = (String) session.getAttribute("error");
	
%>
	
	<script>
	
	$(function() {

		var users = <%=array.toString()%>
		

		    $("#search_input").autocomplete({
		        source: function(request, response){
		            var matcher = new RegExp( $.ui.autocomplete.escapeRegex( request.term ), "i" );
		            response( $.grep( users, function( value ) {
		            return matcher.test(value['fname']) || matcher.test(value['lname'])|| matcher.test(value['username']) || matcher.test(value['fullname']);
		        }));
		        },
		        select: function(event, ui) {
		            if(ui.item){
		            	 $('#profile-wrapper').load('profile.jsp?username=' + ui.item.value);
		            }
		           
		        }
		    });

		  });
	</script>
	
<div id="header">
	<div class="profilePic">
		<img id="user_image" src="<%=loggedInUserImage%>">
	</div>
	<div class="SpotShare_title">
		SpotShare
	</div>
	
	<form  action="" method="GET">
		<div class="searchBar">
			<input type="search" id="search_input" placeholder="Search Users">
			
			
		</div>
	</form>
	
	<div class="logout_wrapper">
		<a href="index.jsp">
			<img id="logout" src="https://d30y9cdsu7xlg0.cloudfront.net/png/4930-200.png">
		</a>
	</div>
	
</div>