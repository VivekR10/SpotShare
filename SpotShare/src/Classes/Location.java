package Classes;



public class Location {
	
	/**
	 * PRIVATE VARIABLE DECLARATIONS
	 */
	private int locationID;
	private String name;
	private float longitude;
	private float latitude;
	private String firstUsername;
	private int numPins;

	/**
	 * CONSTRUCTORS
	 */
	public Location(int locationID, 
			String name, 
			float longitude, 
			float latitude, 
			String firstUsername,
			int numPins) {
		this.locationID = locationID;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.firstUsername = firstUsername;
		this.numPins = numPins;
	}
	public Location(Location location) {
		this.locationID = location.getLocationID();
		this.name = location.getName();
		this.longitude = location.getLongitude();
		this.latitude = location.getLatitude();
		this.firstUsername = location.getFirstUsername();
		this.numPins = location.getNumPins();
	}
	
	
	/**
	 * GETTERS
	 */
	public int getLocationID() {
		return locationID;
	}
	public String getName() {
		return name;
	}
	public float getLongitude() {
		return longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public String getFirstUsername() {
		return firstUsername;
	}
	public int getNumPins() {
		return numPins;
	}
}
