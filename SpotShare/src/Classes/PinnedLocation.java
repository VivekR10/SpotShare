package Classes;



import java.util.Date;

public class PinnedLocation extends Location {
	/**
	 * PRIVATE VARIABLE DECLARATIONS
	 */
	private String username;
	private String categoryName;
	private String review;
	private Date datePinned;
	
	/**
	 * CONSTRUCTOR
	 */
	public PinnedLocation(
			int locationID, 
			String name, 
			float longitude, 
			float latitude, 
			String firstUsername,
			int numPins,
			String username,
			String categoryName,
			String review,
			Date datePinned) {
		super(locationID, name, longitude, latitude, firstUsername, numPins);
		this.username = username;
		this.categoryName = categoryName;
		this.review = review;
		this.datePinned = datePinned;
	}
	public PinnedLocation(
			Location location,
			String username,
			String categoryName,
			String review,
			Date datePinned) {
		super(location);
		this.username = username;
		this.categoryName = categoryName;
		this.review = review;
		this.datePinned = datePinned;
	}
	
	/**
	 * GETTER FUNCTIONS
	 */
	public String getUsername() { 
		return username;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public String getReview() {
		return review;
	}
	public Date getDatePinned() {
		return datePinned;
	}
	
}
