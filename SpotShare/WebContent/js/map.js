    	var markers = [];
    	var tempMarker;
    	
    	var peekViewHTML;
    	var newViewHTML;
    	var editViewHTML;
    	
    
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	        	peekViewHTML = xhttp.responseText;
	        }
        }      
        xhttp.open("GET", "${pageContext.request.contextPath}/../infoView/peekView.html", true);
        xhttp.send();
        
        var xhttp2 = new XMLHttpRequest();
        xhttp2.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	        	editViewHTML = xhttp2.responseText;
	        }
        }      
        xhttp2.open("GET", "${pageContext.request.contextPath}/../infoView/editView.html", true);
        xhttp2.send();
        
        var xhttp3 = new XMLHttpRequest();
        xhttp3.onreadystatechange = function() {
	        if (this.readyState == 4 && this.status == 200) {
	            newViewHTML = xhttp3.responseText;
	        }
        }      
        xhttp3.open("GET", "${pageContext.request.contextPath}/../infoView/newView.html", true);
        xhttp3.send();

    var map;
	  function initMap() {
		alert("initmap");
	    var home = {lat: 34.0522, lng: -118.2437}; //set to users home location or current
	    map = new google.maps.Map(document.getElementById('map'), {
	      zoom: 10,
	      center: home,
	      streetViewControl: false
	    });
	    
	    google.maps.event.addListener(map, 'click', function(event) {
	        placeMarker(event.latLng);
	      });
  	  }
		  
	  function deleteTempMarker() {
		  if(tempMarker != null){
			  tempMarker.setMap(null)
		      tempMarker = null;
		  }
	  }
	  
	  function placeMarker(location){
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
	    		peekinfowindow.open(map, marker);
	    	}
	      });
	  }

	  function removeMarker(marker){
		  marker.setMap(null);
	  }
	  
	  function save(marker){
		  markers.push(marker);
		  //send marker to socket
	  }
	  
	  
	  //--------FILTERS----------
	  function resetFilters(){
		  for(var i = 0; i < markers.length; i++){
			  markers[i].setMap(map);
		  }
	  }
	  
