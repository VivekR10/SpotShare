package Classes;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class SocketMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String action;
	public String marker;
	public String username;
	public String lat;
	public String lng;
	public String type;
	public String review;
	public String name;
	public String imageUrl;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		
		System.out.println(marker);
		//this.marker = marker.replaceAll("\\", "");
		this.marker = marker;
	}

	public void setAction(String string) {
		action = string;
	}
	
	public String getAction(){
		return action;
	}

	public void setUsername(String username) {
		this.username = username;
		
	}

	public void setImage(String imageUrl) {

		this.imageUrl = imageUrl;
	}
}
