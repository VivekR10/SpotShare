package Database;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.User;

public class JSONParse {
	public static JSONArray parseUsers(ArrayList<User> users) {
		JSONArray usersJSON = new JSONArray();

		for (User user: users) {

			JSONObject userJSON = new JSONObject();
			try {
				userJSON.put("label", user.getName());
				userJSON.put("value", user.getUsername());
				userJSON.put("username", user.getUsername());
				userJSON.put("fname", user.getFName());
				userJSON.put("lname", user.getLName());
				userJSON.put("fullname", user.getName());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			usersJSON.put(userJSON);
		}
		System.out.println(usersJSON);
		System.out.println(usersJSON.toString());
		return usersJSON;
	}

}
