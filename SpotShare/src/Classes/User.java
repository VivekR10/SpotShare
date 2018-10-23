package Classes;



public class User {
	/**
	 * PRIVATE VARIABLE DECLARATIONS
	 */
	private String username;
	private int password;
	private String fname;
	private String lname;
	private String image;

	
	/**
	 * CONSTRUCTOR
	 */
	public User(String username, 
			int password, 
			String fname,
			String lname,
			String image) {
		this.username = username;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.image = image;
	}
	
	/**
	 * GETTERS
	 */
	public String getUsername() {
		return username;
	}
	public int getPassword() {
		return password;
	}
	public String getFName() {
		return fname;
	}
	public String getLName() {
		return lname;
	}
	public String getName() {
		return fname + " " + lname;
	}
	public String getImage() {
		return image;
	}
	
}
