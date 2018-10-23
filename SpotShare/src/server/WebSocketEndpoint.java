package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Classes.SocketMessage;
import Database.Database;

@ServerEndpoint(value = "/ws")

public class WebSocketEndpoint {
	private Database database;
	private static final Logger logger = Logger.getLogger("BotEndpoint");
	private static final Map<String, Session> sessionIdsToSessions = new HashMap<String, Session>();

	private static Map<String, Session> usernamesToSessions = new HashMap<String, Session>();
	private static Map<String, Session> sessions = new HashMap<String, Session>();

	private transient Gson mGson = new Gson(); //Convenient library provided by Google to convert Java objects to JSON
	private static Lock lock = new ReentrantLock();

	@OnOpen
	public void open(Session session) {
		
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		database = new Database();
		lock.lock();
		
		//figure out what to do based on action
		JsonObject msg = mGson.fromJson(message, JsonObject.class); //message is rawJson
		String action = msg.get("action").getAsString();

		if(action.equals("login")){
			String username = msg.get("username").getAsString();

			usernamesToSessions.put(username, session);
			lock.unlock();
			return;
		}
		
		//message must be a marker action, extract fields
		JsonObject marker = mGson.fromJson(msg.get("marker"), JsonObject.class);
		JsonObject latlong = mGson.fromJson(marker.get("latlong"), JsonObject.class);

		String username = msg.get("username").getAsString();
		String name = marker.get("name").getAsString();
		String review = marker.get("review").getAsString();
		String type = marker.get("type").getAsString();
		String lat = latlong.get("lat").getAsString();
		String lng = latlong.get("lng").getAsString();

		if(action.equals("newMarker")){
			//insert live pin to db
			database.insertPinnedLocation(database.formatDouble(Double.parseDouble(lng)), database.formatDouble(Double.parseDouble(lat)), name, username, review, type);
			String imageUrl = database.getUserOfUsername(username).getImage();
			ArrayList<String> followers = database.getFollowerUsernamesOfUsername(msg.get("username").getAsString());

			//if the user has followers, send them live pin
			if(followers != null){
				SocketMessage socketMessage = new SocketMessage();
				socketMessage.setAction("newMarker");
				socketMessage.setUsername(username);
				socketMessage.setName(name);
				socketMessage.setReview(review);
				socketMessage.setType(type);
				socketMessage.setLat(lat);
				socketMessage.setImage(imageUrl);
				socketMessage.setLng(lng);
				
				for(int i = 0; i < followers.size(); i ++){
					sendToSession(usernamesToSessions.get(followers.get(i)), socketMessage);
				}
			}

		}else if(action.equals("updateMarker")){

		}else if(action.equals("deleteMarker")){
			//TODO: delete marker in db
		}else{
			System.out.println("Unknown message type recieved from client");
		}
		
		lock.unlock();
	}

	@OnClose
	public void close(Session session) {
		lock.lock();
		//should remove session from map
		lock.unlock();
	}

	@OnError
	public void onError(Throwable error) {
		error.printStackTrace();
	}

	public void sendToSession(Session session, SocketMessage message) {

		if(session == null){
			return;
		}
		lock.lock();
		try {
			session.getBasicRemote().sendText(mGson.toJson(message));
		} catch (IOException e) {
			sessions.remove(session.getId());
		}
		lock.unlock();
	}
}