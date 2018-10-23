<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="Database.Database"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList" %>
<%@page import="Classes.PinnedLocation"%>
<%@ page import="Classes.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%
	Database dbConn = (Database) session.getAttribute("dbConn");

	ArrayList<String> queriedUsers = new ArrayList<>();
	queriedUsers = (ArrayList<String>) session.getAttribute("queriedUsers");
	if (queriedUsers == null) { 
		queriedUsers = new ArrayList<>();
	}
	String user = (String) session.getAttribute("loggedInUser");
	ArrayList<PinnedLocation> myPinnedLocations = dbConn.getPinnedLocationsOfUsername(user);
	ArrayList<PinnedLocation> followingPinnedLocations;
	if (user.equals("guest")) {
		followingPinnedLocations = dbConn.getAllPinnedLocations();
	} else {
		followingPinnedLocations = dbConn.getPinnedLocationsOfUsernameFollowing(user);
	}
	
%>

<head>
<link rel="icon" type="image/ico" href="img/favicon.png"/>
<link rel="stylesheet" type="text/css" href="css/profile.css">
<script src="js/WebSocketEndpoint.js" type="text/javascript"></script>

<script src="http://code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>
<script src="js/map.js" type="text/javascript"></script>
<script 
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyArWjcgc-T4d3L7N-RtAn2COSsKC8DAFNY">
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SpotShare</title>
<script>
socket.onopen = function(event){
	socket.send(JSON.stringify({
		action: 'login',
		username: '<%=(String) session.getAttribute("loggedInUser")%>'
	}));
}

function sendLogin(){
	socket.send(JSON.stringify({
		action: 'login',
		username: '<%=(String) session.getAttribute("loggedInUser")%>'
	}));
}

try{
	sendLogin();
}catch(err){
	setTimeout(function() { sendLogin();}, 1000);

}

window.addEventListener('beforeunload', function(event) {
	socket.close();
  });

socket.onmessage = function (event) {
	var msg = JSON.parse(event.data);

	var action = msg.action;
	var username = msg.username;
	//alert(username);
	
	if (action == 'newMarker') {
		var toAdd = new google.maps.LatLng(msg.lat, msg.lng);
		dropLiveMarker(toAdd, msg.type, msg.name, msg.review, username);
	}else if(action == 'updateMarker'){
		//TODO: update marker
	}else if(action == 'deleteMarker'){
		//TODO: delete marker
	}
	
	addToFeed(msg.username, msg.name, msg.review, msg.type, msg.imageUrl);

}

window.dropLiveMarker = function(location, type, name, review, owner){
	
	var marker = new google.maps.Marker({
	      position: location,
	      map: map,
	      saved: true,
          animation: google.maps.Animation.DROP,
	      name : name,
	      type : type,
	      review : review
	    });
	  
	  
	  var peekinfowindow = new google.maps.InfoWindow({
		  	content: peekViewHTML
		  });
	  
	  //save reference to marker
	  markers.push(marker);
	  
	  marker.addListener('click', function() {
		  var peekinfocontent = document.createElement('div');
		  var nameStrong = document.createElement('strong');
		  var typeText = document.createElement('text');
		  var reviewText = document.createElement('text');
		  var ownerText = document.createElement('text');
		  nameStrong.textContent = marker.name;
		  typeText.textContent = "Type: " + marker.type;
		  reviewText.textContent = "Description: " + marker.review;
		  ownerText.textContent = "Pinned by: " + owner;
		  peekinfocontent.appendChild(nameStrong);
		  peekinfocontent.appendChild(document.createElement('br'));
		  peekinfocontent.appendChild(typeText);
		  peekinfocontent.appendChild(document.createElement('br'));
		  peekinfocontent.appendChild(reviewText);
		  peekinfocontent.appendChild(document.createElement('br'));
		  peekinfocontent.appendChild(ownerText);
		  peekinfowindow.setContent(peekinfocontent);
		  peekinfowindow.open(map, marker);
	  });
}

var map;
function initMap() {
  var home = {lat: 34.0522, lng: -118.2437}; //set to users home location or current
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 10,
    center: home,
    streetViewControl: false
  });
  
  <% for (PinnedLocation p : myPinnedLocations) { %>
  var lati = <%=p.getLatitude()%>
  var longi = <%=p.getLongitude()%>
  var toAdd = new google.maps.LatLng(lati, longi);
  var name = '<%=p.getName()%>'
  var review = '<%=p.getReview()%>'
  var type = '<%=p.getCategoryName()%>'
  var owner = '<%=p.getUsername()%>'

  dropLiveMarker(toAdd, type, name, review, owner);
  <%}%>

  <% for (PinnedLocation p : followingPinnedLocations) { %>
  var lati = <%=p.getLatitude()%>
  var longi = <%=p.getLongitude()%>
  var toAdd = new google.maps.LatLng(lati, longi);
  var name = '<%=p.getName()%>'
  var review = '<%=p.getReview()%>'
  var type = '<%=p.getCategoryName()%>'
  var owner = '<%=p.getUsername()%>'
  var imgURL = '<%=dbConn.getUserOfUsername(p.getUsername()).getImage()%>'
	
	addToFeed(owner, name, review, type, imgURL)
		
  dropLiveMarker(toAdd, type, name , review, owner);
  <%}%>
  
  google.maps.event.addListener(map, 'click', function(event) {
      placeMarker(event.latLng);
    });
}
  
window.placeMarker = function(location){
	  deleteTempMarker();
  var marker = new google.maps.Marker({
	      position: location,
	      map: map,
	      saved: false,
	      name : "",
	      type : "",
	      review : ""
	    });
  var newinfowindow = new google.maps.InfoWindow({
      content: newViewHTML
    });
  
  var peekinfowindow = new google.maps.InfoWindow({
  	content: peekViewHTML
  });
  
  //makes the marker persist afte
  google.maps.event.addListener(newinfowindow, 'domready', function(){
  	document.getElementById("pin-form").addEventListener("submit", function(e){
  		e.preventDefault();
  		marker.saved = true;
  		tempMarker = null;
  		marker.name = document.getElementById("location").value;
  		marker.type = document.getElementById("type").value;
  		marker.review = document.getElementById("review").value;
  		var newPin = {latlong:marker.position, name:marker.name, type:marker.type, review:marker.review};
  		newinfowindow.close();
  		newMarker(newPin);
  		markers.push(marker);
  	});
  });
	
  tempMarker = marker;
  newinfowindow.open(map, marker);
	
  newinfowindow.addListener('closeclick', function(){
  	if(marker.saved == false){
  		marker.setMap(null);
  	}
  });
  
  marker.addListener('click', function() {
  	if(marker.saved ==  false){
	        newinfowindow.open(map, marker);
  	}else{
  		var peekinfocontent = document.createElement('div');
  		var nameStrong = document.createElement('strong');
  		var typeText = document.createElement('text');
  		var reviewText = document.createElement('text');
  		var ownerText = document.createElement('text');
  		nameStrong.textContent = marker.name;
  		typeText.textContent = "Type: " + marker.type;
  		reviewText.textContent = "Description: " + marker.review;
  		ownerText.textContent = "Pinned by: " + '<%=user %>';
  		peekinfocontent.appendChild(nameStrong);
  		peekinfocontent.appendChild(document.createElement('br'));
  		peekinfocontent.appendChild(typeText);
  		peekinfocontent.appendChild(document.createElement('br'));
  		peekinfocontent.appendChild(reviewText);
  		peekinfocontent.appendChild(document.createElement('br'));
  		peekinfocontent.appendChild(ownerText);
  		peekinfowindow.setContent(peekinfocontent);
  		peekinfowindow.open(map, marker);
  	}
    });
}

function newMarker(marker){
	var message = {
			marker: marker,
			action: 'newMarker',
			username: '<%=session.getAttribute("loggedInUser")%>'
	}
	socket.send(JSON.stringify(message));
}

function updateMarker(marker){
	var message = {
			marker: marker,
			action: 'updateMarker',
			username: '<%=session.getAttribute("loggedInUser")%>'
	}
	
	socket.send(JSON.stringify(message));
}

function deleteMarker(marker){
	var message = {
			marker: marker,
			action: 'deleteMarker',
			username: '<%=session.getAttribute("loggedInUser")%>'
	}
	
	socket.send(JSON.stringify(message));
	
}

google.maps.event.addDomListener(window, "load", initMap);

function setFilter(){
	  var filter = document.getElementById("filters").value;
	  for(var i = 0; i < markers.length; i++){
		  if(markers[i].type != filter){
			  markers[i].setMap(null);
		  }
		  
	  }
}
</script>

</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp" />
	</div>
	<div id="filters-container">
		<select id="filters"> 
			<option value="Entertainment">Entertainment</option>
			<option value="Restaurant">Restaurant</option>
			<option value="Park">Park</option>
			<option value="Hike">Hike</option>
			<option value="Bar">Bar</option>
			<option value="Beach">Beach</option>
			<option value="Shopping">Shopping</option>
			<option value="Landmark">Landmark</option>
			<option value="Museum">Museum</option>
			<option value="Attraction">Attraction</option>
		</select>
		<button onclick="setFilter()" >APPLY FILTER</button>
		<button onclick="resetFilters()">RESET FILTERS</button>
	</div>

	
    <div id="map" style="height: 100%; width: 80%; position: absolute; left:0px; top: 70px;">
    	<</button>
    </div>

    <jsp:include page="feed.jsp" />
    
    <%
    	//retrieving user information to display user profile
    	String username = "brooks";
    	User profileUser = dbConn.getUserOfUsername(username);
    	if (profileUser == null) {
    		System.out.println("user was null");
    	}
    	String name = profileUser.getName();
    	String image = profileUser.getImage();
    	
    	int numFollowers = dbConn.getFollowerUsernamesOfUsername(username).size();
    	int numFollowing = dbConn.getFollowingUsernamesOfUsername(username).size();
    	
    	boolean foreignUser = true;
    	
    	if (user.equals(username)) {
    		foreignUser = false;
    	}
    	
    	boolean following = false;
    	String follow = "";

    	if (foreignUser) {
	    	if (dbConn.getFollowerUsernamesOfUsername(username).contains(user)) {
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
</body>
