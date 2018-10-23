package Database;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Classes.Location;
import Classes.PinnedLocation;
import Classes.User;

public class Database {
	private Connection conn = null;
	private String dbUsername = "root";
	private String dbPassword = "root"; //to change
	private String dbName = "SpotShare";
	
	private MathContext mc = new MathContext(7);
	
	/**
	 * CONSTRUCTOR - ESTABLISHES CONNECTION TO DATABASE
	 */
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/"
					+ dbName
					+ "?user=" + dbUsername
					+ "&password=" + dbPassword
					+ "&useSSL=false");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * USER
	 */
	public boolean checkUsernameExists(String username) {
		boolean usernameExists = false;
		String statementText = "SELECT * FROM user WHERE user_username=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			if (rs.first()) {
				usernameExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usernameExists;
	}
	public String getNameOfUsername(String username) {
		String name = null;
		String statementText = "SELECT * FROM user WHERE user_username=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			while (rs.next()) {
				name = rs.getString("user_fname") + " " + rs.getString("user_lname");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return name;
	}
	public User getUserOfUsername(String username) {
		User user = null;
		String statementText = "SELECT * FROM user WHERE user_username=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			while (rs.next()) {
				user = new User(
						rs.getString("user_username"),
						rs.getInt("user_password"),
						rs.getString("user_fname"),
						rs.getString("user_lname"),
						rs.getString("user_image"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	public ArrayList<User> getUsersOfName(String nameQuery) {
		ArrayList<User> users = new ArrayList<User>();
		String statementText = "SELECT * FROM " + dbName + ".user WHERE LOWER(user_lname)='" 
				+ nameQuery.toLowerCase()
				+ "' or LOWER(user_fname)='" 
				+ nameQuery.toLowerCase() 
				+ "'";
		
		try {
			ResultSet rs = executeSQuery(statementText);
			users = getUsersFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		User matchingUser = getUserOfUsername(nameQuery);
		if (matchingUser != null) {
			users.add(matchingUser);
		}
		
		return users;
	}
	public int getPasswordOfUsername(String username) {
		int password = 0;
		String statementText = "SELECT * FROM user WHERE user_username=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			while (rs.next()) {
				password = rs.getInt("user_password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return password;
	}
	public ArrayList<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		String statementText = "SELECT * FROM " + dbName + ".user;";
		
		try {
			ResultSet rs = executeSQuery(statementText);
			users = getUsersFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public void insertUser(String username, int password, String fname, String lname, String image) {
		String statementText = "INSERT INTO " + dbName + ".user "
				+ "(user_username, user_password, user_fname, user_lname, user_image) "
				+ "VALUES ('" + username + "', '" + password + "', '" + fname + "', '" + lname + "', '"+ image + "');";
		
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * FOLLOW
	 */
	public ArrayList<String> getFollowingUsernamesOfUsername(String username) {
		ArrayList<String> followingUsernames = new ArrayList<String>();
		String statementText = "SELECT * FROM follow WHERE follow_username_following=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			while (rs.next()) {
				followingUsernames.add(rs.getString("follow_username_being_followed"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return followingUsernames;
	}
	public ArrayList<String> getFollowerUsernamesOfUsername(String username) {
		ArrayList<String> followerNames = new ArrayList<String>();
		String statementText = "SELECT * FROM follow WHERE follow_username_being_followed=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			while (rs.next()) {
				followerNames.add(rs.getString("follow_username_following"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return followerNames;
	}
	
	public void insertFollowingRelationship(String usernameFollowing, String usernameBeingFollowed) {
		String statementText = 
				"INSERT INTO " + dbName + ".follow "
				+ "(follow_username_following, follow_username_being_followed)"
				+ "VALUES ('" + usernameFollowing + "', '" + usernameBeingFollowed + "');";
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteFollowingRelationship(String usernameFollowing, String usernameBeingFollowed) {
		String statementText = 
				"DELETE FROM " + dbName + ".follow WHERE "
				+ "follow_username_following='" + usernameFollowing
				+ "' and follow_username_being_followed='" + usernameBeingFollowed + "'";
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PINNED LOCATION
	 */
	public boolean checkAlreadyPinnedLocation(int locationID, String username) {
		String statementText = "SELECT * FROM " + dbName + ".pinned_location "
				+ "WHERE pl_username='" + username + "' and pl_id=" + locationID + ";";
	
		try {
			ResultSet rs = executeSQuery(statementText);
			if (rs.first()) {
				return true;	//already pinned
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public ArrayList<PinnedLocation> getRecentLocations() {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		String statementText = "SELECT * FROM SpotShare.pinned_location ORDER BY pl_date_pinned DESC LIMIT 5";
		
		try {
			ResultSet rs = executeSQuery(statementText);
			pinnedLocations = getPinnedLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return pinnedLocations;
	}
	public ArrayList<PinnedLocation> getAllPinnedLocations() {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		String statementText = "SELECT * FROM pinned_location";
		
		try {
			ResultSet rs = executeSQuery(statementText);
			pinnedLocations = getPinnedLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pinnedLocations;
	}
	public ArrayList<PinnedLocation> getPinnedLocationsOfUsername(String username) {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		String statementText = "SELECT * FROM pinned_location WHERE pl_username=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, username);
			pinnedLocations = getPinnedLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pinnedLocations;
	}
	public ArrayList<PinnedLocation> getPinnedLocationsOfUsernameFollowing(String username) {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		ArrayList<String> followingUsernames = getFollowingUsernamesOfUsername(username);
		String statementText = "SELECT * FROM pinned_location WHERE pl_username=?";
		
		for (String following : followingUsernames) {
			try {
				ResultSet rs = executePSQuery(statementText, following);
				ArrayList<PinnedLocation> followingPinnedLocations = getPinnedLocationsFromResultSet(rs);
				pinnedLocations.addAll(followingPinnedLocations);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (pinnedLocations.size() > 0) {
			quicksortByDate(0, pinnedLocations.size()-1, pinnedLocations); 
		}
		
		return pinnedLocations;
	}
	public ArrayList<PinnedLocation> getPinnedLocationsOfLocationID(int locationID) {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		String statementText = "SELECT * FROM pinned_location WHERE pl_location_id=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, locationID);
			pinnedLocations = getPinnedLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pinnedLocations;
	}
	public ArrayList<PinnedLocation> getPinnedLocationsOfCategory(String category) {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		String statementText = "SELECT * FROM pinned_location WHERE pl_category=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, category);
			pinnedLocations = getPinnedLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pinnedLocations;
	}
	
	public void insertPinnedLocation(
			BigDecimal longitude, 
			BigDecimal latitude, 
			String locationName,
			String currentUsername, 
			String review, 
			String category) {
		Location location = getLocationOfLongitudeLatitude(longitude, latitude);
		if (location == null) {
			location = insertLocation(locationName, longitude, latitude, currentUsername);
		} else {
			incrementPinCountOfLocation(location.getLocationID());
		}
		
		if (checkAlreadyPinnedLocation(location.getLocationID(), currentUsername)) {
			return; //return if user has already pinned this location
		}
		
		String statementText = "INSERT INTO " + dbName + ".pinned_location "
				+ "(pl_location_id, pl_username, pl_review, pl_category) "
				+ "VALUES ('" + location.getLocationID() 
				+ "', '" + currentUsername + "', '" + review + "', '" + category + "');";
		
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updatePinnedLocation(int locationID, String username, String review, String category) {
		String statementText = "";
		
		boolean changedReview = (review != null && !review.isEmpty());
		boolean changedCategory = (category != null && !category.isEmpty());
		
		if (changedReview && changedCategory) {
			statementText = "UPDATE " + dbName + ".pinned_location "
					+ "SET pl_category='" + category 
					+ "', pl_review='" + review 
					+ "' WHERE pl_username='" + username 
					+ "' and pl_location_id=" + locationID + ";";
		} else if (changedCategory) {
			statementText = "UPDATE " + dbName + ".pinned_location "
					+ "SET pl_category='" + category 
					+ "' WHERE pl_username='" + username 
					+ "' and pl_location_id=" + locationID + ";";
		} else if (changedReview) {
			statementText = "UPDATE " + dbName + ".pinned_location "
					+ "SET pl_review='" + review 
					+ "' WHERE pl_username='" + username 
					+ "' and pl_location_id=" + locationID + ";";
		}
		
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deletePinnedLocation(int locationID, String username) {
		String statementText = "DELETE FROM " + dbName + ".pinned_location "
				+ "WHERE pl_username='" + username + "' and pl_location_id=" + locationID + ";";
	
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}	
	
	/**
	 * LOCATION
	 */
	
	public Location getLocationofLocationID(int locationID) {
		Location location = null;
		String statementText = "SELECT * FROM location WHERE location_id=?";
		
		try {
			ResultSet rs = executePSQuery(statementText, locationID);
			location = getLocationFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return location;
	}
	public Location getLocationOfLongitudeLatitude(BigDecimal longitude, BigDecimal latitude) {
		Location location = null;
		String statementText = "SELECT * FROM " + dbName + ".location WHERE location_longitude=" + longitude + " and location_latitude=" + latitude + ";";
				
		try {
			ResultSet rs = executeSQuery(statementText);
			location = getLocationFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return location;
	}
	public ArrayList<Location> getPopularLocations() {
		ArrayList<Location> popularLocations = new ArrayList<Location>();
		String statementText = "SELECT * FROM Spotshare.location ORDER BY location_number_pins DESC LIMIT 10;";
		
		try {
			ResultSet rs = executeSQuery(statementText);
			popularLocations = getLocationsFromResultSet(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return popularLocations;
	}
	
	
	private Location insertLocation(String name, BigDecimal longitude, BigDecimal latitude, String firstUsername) {		
		String statementText = "INSERT INTO " + dbName + ".location "
				+ "(location_name, location_longitude, location_latitude, location_first_username) "
				+ "VALUES ('" + name + "', " + longitude + ", " + latitude + ", '" + firstUsername + "');";
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return getLocationOfLongitudeLatitude(longitude, latitude);
	}
	
	private void incrementPinCountOfLocation(int locationID) {
		String statementText = "UPDATE " + dbName + ".location "
				+ "SET location_number_pins=location_number_pins+1 "
				+ "WHERE location_id=" + locationID;
		try {
			executeSUpdate(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * HELPER FUNCTIONS
	 * @throws SQLException 
	 */
	private ResultSet executePSQuery(String statementText, String parameter) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(statementText);
		ps.setString(1, parameter);
		ResultSet rs = ps.executeQuery();
		return rs;
	}
	private ResultSet executePSQuery(String statementText, int parameter) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(statementText);
		ps.setInt(1, parameter);
		ResultSet rs = ps.executeQuery();
		return rs;
	}
	private ResultSet executeSQuery(String statementText) throws SQLException {
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(statementText);
		return rs;
	}
	private void executeSUpdate(String statementText) throws SQLException {
		Statement statement = conn.createStatement();
		statement.executeUpdate(statementText);
	}
	
	private ArrayList<User> getUsersFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		while (rs.next()) {
			User user = new User(
					rs.getString("user_username"), 
					rs.getInt("user_password"), 
					rs.getString("user_fname"),
					rs.getString("user_lname"),
					rs.getString("user_image")
					);
			users.add(user);
		}
		return users;
	}
	private ArrayList<PinnedLocation> getPinnedLocationsFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<PinnedLocation> pinnedLocations = new ArrayList<PinnedLocation>();
		
		while (rs.next()) {
			int locationID = rs.getInt("pl_location_id");
			Location location = getLocationofLocationID(locationID);
			java.sql.Date dateSQL = rs.getDate("pl_date_pinned");
			java.util.Date date = new java.util.Date(dateSQL.getTime());
			PinnedLocation pl = new PinnedLocation(
					location,
					rs.getString("pl_username"),
					rs.getString("pl_category"),
					rs.getString("pl_review"),
					date);
			pinnedLocations.add(pl);
		}
		
		return pinnedLocations;
	}
	private ArrayList<Location> getLocationsFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<Location> locations = new ArrayList<Location>();
		
		while (rs.next()) {
			int locationID = rs.getInt("location_id");
			Location location = getLocationofLocationID(locationID);
			locations.add(location);
		}
		
		return locations;
	}
	private Location getLocationFromResultSet(ResultSet rs) throws SQLException {
		Location location = null;
		while (rs.next()) {
			location = new Location(
					rs.getInt("location_id"), 
					rs.getString("location_name"), 
					rs.getFloat("location_longitude"),
					rs.getFloat("location_latitude"),
					rs.getString("location_first_username"),
					rs.getInt("location_number_pins"));
		}
		return location;
	}

	public BigDecimal formatDouble(double d) {
		BigDecimal bd = new BigDecimal(d);
		bd = bd.round(mc);
		return bd;
	}
	
	private void quicksortByDate(int low, int high, ArrayList<PinnedLocation> pls) {
	        int i = low, j = high;
	        
	        java.util.Date pivot = pls.get(low + (high-low)/2).getDatePinned();
	
	        while (i <= j) {
	                while (pls.get(i).getDatePinned().getTime() < pivot.getTime()) {
	                        i++;
	                }
	                while (pls.get(j).getDatePinned().getTime() > pivot.getTime()) {
	                        j--;
	                }
	                if (i <= j) {
	                        exchange(i, j, pls);
	                        i++;
	                        j--;
	                }
	        }
	        if (low < j)
	        	quicksortByDate(low, j, pls);
	        if (i < high)
	        	quicksortByDate(i, high, pls);
	}
	
	private void exchange(int i, int j, ArrayList<PinnedLocation> pls) {
	        PinnedLocation temp = pls.get(i);
	        pls.set(i, pls.get(j));
	        pls.set(j, temp);
	}
}
