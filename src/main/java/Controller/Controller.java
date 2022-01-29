package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import Network.* ;
import Model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
	protected static String nickname ;
	private static ControllerNetwork controllerNetwork = new ControllerNetwork();
	private static BDD db = new BDD() ;
	private static BDD2 db2 = new BDD2() ;
	
	private static boolean validBDDusername(String bonNickname) {
		InetAddress IP = db.getIP(bonNickname) ;
		return (IP==null || IP.equals(ControllerNetwork.getIP()));
	
	}
	
	public static boolean validUsername(String bonNickname) throws IOException {
		boolean nicknameOk = true ; 
		nicknameOk = nicknameOk && validBDDusername(bonNickname);
		if(nicknameOk) {
			nickname = bonNickname;
		}
		return nicknameOk ; 
	}
	
	public static String getNickname() {
		return nickname;
	}

	public static void connection(String nickname) throws IOException {
		// Once the username has been accepted, bc username
		controllerNetwork.notifyConnected(nickname);
		// We ask everyone their usernames
		controllerNetwork.askUsernames(nickname);
	}
	
	public static void runServers() {
		controllerNetwork.runServers();
	}
	
	
	public static void notifyUsernameChanged(String nouveau) throws IOException {
		controllerNetwork.notifyUserameChanged(nouveau);
	}
	
	public static void disconnection() throws IOException {
		controllerNetwork.disconnection(nickname);
	}
	
	public static void usernameRequest(String pseudo, InetAddress IP) {
		if (pseudo.equals(nickname)) {
			controllerNetwork.sendUnavailableUsername(IP);
		}
	}
	
	public static void newUserConnected(String nickname, InetAddress IP) {
		db.addUser(nickname, IP);
	}
	
	public static void userDisconnected(String nickname) {
		db.userDisconnected(nickname);
	}
	
	public static void setLastAccess(String nickname, String newDate) {
		String IPString = db.getIP(nickname).toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		InetAddress IP = null;
		try {
			IP = InetAddress.getByName(IPString);
		} catch (UnknownHostException e) {
			System.out.println(e) ; 
		} 
		db.updateLastAccess(IP, newDate);
	}
	
	public static String getUsername(InetAddress IP) {
		return db.getUsername(IP);
	}
	
	public static InetAddress getIP(String name) {
		return db.getIP(name);
	}
	
	public static String getLastAccess(String nickname) {
		return db.getLastAccess(db.getIP(nickname)) ; 
	}
	
	public static ArrayList<String> getConnectedUsernames() {
		return db.getConnectedUsernames() ; 
	}
	
	
	public static boolean userReachable(ArrayList<String> userConnectes, String userToReach) {
		return userConnectes.contains(userToReach) ; 
	}
	
	public static ArrayList<String> getAllUsernames() {
		return db.getAllUsernames() ; 
	}
	
	
	
	
	public static void dropTable() {
		db.dropDatabase();
	}
	
	public static void addMessageToHistory(String emetteur, String destinataire, String msg, String dateTime) {
		db2.addMessage(emetteur, destinataire, msg, dateTime) ;
	}
	
	public static Session getHistory(String user1, String user2) {
		String IP1 = getIP(user1).toString();
		String IP2 = getIP(user2).toString();
		if (IP1.charAt(0) == ('/')) {
			IP1 = IP1.substring(1);
		}
		if (IP2.charAt(0) == ('/')) {
			IP2 = IP2.substring(1);
		}
		return db2.getMessages(IP1, IP2);
	}
	
	public static ArrayList<String> getInterlocutors(String nickname) {
		String IP = getIP(nickname).toString();
		if (IP.charAt(0) == ('/')) {
			IP = IP.substring(1);
		}
		String[] IPArray = db2.getInterlocutors(IP) ; 
		ArrayList<String> interlocutors = new ArrayList<String>(); 
		for (int i=0; i<IPArray.length; i++) {
			try {
				interlocutors.add(getUsername(InetAddress.getByName(IPArray[i]))) ;
			} catch (UnknownHostException e) {
				System.out.println(e) ; 
			} 
		}
		return interlocutors ; 
	}
	
	public static String getLastDate(String emetteur, String destinataire) {
		String IP1 = getIP(emetteur).toString();
		String IP2 = getIP(destinataire).toString();
		if (IP1.charAt(0) == ('/')) {
			IP1 = IP1.substring(1);
		}
		if (IP2.charAt(0) == ('/')) {
			IP2 = IP2.substring(1);
		}
		return db2.getLastReceivedMessages(IP1, IP2); 
	}
	
	public static void updateUsername(InetAddress IP, String nouveau) {
		db.updateUsername(IP, nouveau);
	}
	
	public static void sendMessage(String destinataireName, String text) {
		InetAddress destinationIP = getIP(destinataireName);
		controllerNetwork.sendMessage(text, destinationIP);
	}
	
	public static void main (String [] args) throws IOException {
		runServers();
	}
}
