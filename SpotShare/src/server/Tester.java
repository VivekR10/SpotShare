package server;


import java.util.ArrayList;

import Classes.Location;
import Classes.User;
import Database.Database;



public class Tester {
	static Database db = null;
	
	public static void main(String [] args) {
		db = new Database();
		
		ArrayList<User> users = db.getUsersOfName("joann");
		for (User u: users) {
			printUserInformation(u);
		}
	}
	
	/* TESTING USER FUNCTIONS */
	public static void test_checkUsernameExists(String username) {
		System.out.println("Testing checkUsernameExists()...");
		System.out.print("\t" + username + " ");
		if (db.checkUsernameExists(username)) {
			System.out.print("exists");
		} else {
			System.out.print("doesn't exist");
		}
	}
	public static void test_getUserOfUsername(String username) {
		System.out.println("Testing getUserOfUsername("+username+")...");
		User u = db.getUserOfUsername("joannlin");
		
		printUserInformation(u);
	}
	public static void printUserInformation(User u) {
		System.out.println(u.getUsername());
		System.out.println("\t" + u.getPassword());
		System.out.println("\t" + u.getFName());
		System.out.println("\t" + u.getLName());
		System.out.println("\t" + u.getImage());
	}
	public static void test_insertUser() {
		System.out.println("Testing insertUser()...");
		
		String username = "drew";
		
		db.insertUser(username, username.hashCode(), "Drew", "P.", "https://cdn.pixabay.com/photo/2013/07/13/12/07/avatar-159236_1280.png");

	}
	
	public static void test_getPopularLocations() {
		System.out.println("Testing getPopularLocations()...");
		ArrayList<Location> popularLocations = db.getPopularLocations();
		int counter = 1;
		
		for (Location location : popularLocations) {
			System.out.println("\t" + counter + ": " + location.getName() + " - " + location.getNumPins());
			counter ++;
		}
	}
}
