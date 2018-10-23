<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<script>
	
		function addToFeed(Username, title, description, type, imageUrl) {
			//alert("ADDING TO FEED");
			var event = document.createElement("div");
			event.setAttribute("class", "event");
			
			//alert("Made Event");
			var image = document.createElement("div");
			image.setAttribute("class", "image");
			var actualImage = document.createElement("img");
			actualImage.setAttribute("src", imageUrl);
			actualImage.onclick = function() {
				$('#profile-wrapper').load('profile.jsp?username=' + Username);
			}; 
			image.appendChild(actualImage);
			event.appendChild(image);
			
			//alert("Made Image");
			var infoHolder = document.createElement("div");
			infoHolder.setAttribute("class", "infoHolder");
			//alert("Made InfoHolder");
			
			var feedDescription = document.createElement("div");
			feedDescription.setAttribute("class", "feeddescription");
			var feedDescriptionText = document.createTextNode(description);
			feedDescription.appendChild(feedDescriptionText);
			//alert("Made Feed Desc");
			
			var feedTitle = document.createElement("div");
			feedTitle.setAttribute("class", "feedtitle");
			var feedTitleText = document.createTextNode(title);
			feedTitle.appendChild(feedTitleText);
			//alert("Made Feed Title");
			
			var feedUsername = document.createElement("div");
			feedUsername.setAttribute("class", "feedusername");
			var feedUsernameText = document.createTextNode(Username);
			feedUsername.appendChild(feedUsernameText);
			//alert("Made Feed Username");

			
			infoHolder.appendChild(feedUsername);
			infoHolder.appendChild(feedTitle);
			infoHolder.appendChild(feedDescription);
			
		
			//alert("Appended All Children");

			event.appendChild(infoHolder);
			//alert("Appended to Event");

			/* var button = document.createElement("button");
			button.innerHTML  = "View user";
			button.setAttribute("id", "viewUserButton");
			button.onclick = function() {
				$('#profile-wrapper').load('profile.jsp?username=' + Username);
			}; 
			
			//<object type="type/html" data="profile.jsp?username=' + Username + '"></object>
			event.appendChild(button);  */
			
			var feed = document.getElementById("feedbox");
			
			feed.insertBefore(event, feed.firstChild);
		}
	
</script>

<div id="profile-wrapper">
</div>

<div class="feedbox" id="feedbox">

</div>