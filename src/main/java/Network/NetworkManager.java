package Network;

import Controller.*;
import Model.User;
import Network.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class NetworkManager {

	// Attributes 
	TCPReceive serverTCP ; 
	UDPReceive serverUDP ;
	static InetAddress myIP ;
	static String myIPString ;
	static boolean isAvailable ;

	// Constructor 
	public NetworkManager() {
		this.serverTCP = new TCPReceive() ; 
		this.serverUDP = new UDPReceive(5001, 50000) ;

		// Get IP address (!= 127.0.0.1)
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();

				List<InterfaceAddress> list = ni.getInterfaceAddresses();
				Iterator<InterfaceAddress> it = list.iterator();

				while (it.hasNext()) {
					InterfaceAddress ia = it.next();
					if (ia.getBroadcast()!=null && myIP == null) {
						myIP = ia.getAddress() ;
					}
				}		
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		myIPString = myIP.toString();
		if (myIPString.charAt(0) == ('/')) {
			myIPString = myIPString.substring(1);
		}
	}

	public static InetAddress getMyIP() {
		return myIP ;
	}
	
	public String getMyIPString() {
		return myIPString ;
	}


	//////////////////////////////////////////////////////////////////////
	///////////////////////////// RUN SERVERS ////////////////////////////
	//////////////////////////////////////////////////////////////////////
	public void runServers() {
		new Thread(this.serverTCP).start();
		new Thread(this.serverUDP).start();
	}


	//////////////////////////////////////////////////////////////////////
	///////////////////////////MESSAGES FORMAT////////////////////////////
	//////////////////////////////////////////////////////////////////////

	// Enum for each message type
	public static enum MessageType {
		USERNAME_BRDCST,
		USERNAME_CONNECTED,
		USERNAME_DISCONNECT,
		GET_USERNAMES,
		USERNAME_CHANGED, 
		MESSAGE
	}


	// Message formatting
	public static String messageFormatter(MessageType type,String username, String content) {
		return (type + "/-/" + username+ "/-/" + content);
	}


	//////////////////////////////////////////////////////////////////////
	////////////////////PRINCIPAL SENDMESSAGE FUNCTION////////////////////
	//////////////////////////////////////////////////////////////////////

	// Classic message sending function
	public void sendMessage(String msg, InetAddress destinationIP) {
		// Classic message between users 
		String formatedMsg = messageFormatter(MessageType.MESSAGE, User.getNickname(), msg);
		// Sends message and closes socket
		TCPSend.sendMessage(formatedMsg, destinationIP);
	}


	//////////////////////////////////////////////////////////////////////
	///////////////////////////////EMISSION///////////////////////////////
	//////////////////////////////////////////////////////////////////////

	// Availability of the username
	public synchronized boolean usernameAvailable(String username) throws IOException {
		isAvailable = true ;
		long timeElapsed = 0;
		long start = System.currentTimeMillis();
		long finish = 0;
		String msg = messageFormatter(MessageType.USERNAME_BRDCST, username, myIPString);

		// Ask everyone if the username is available 
		broadcastUDP.envoiBroadcast(msg);

		// We wait for an answer (users only answer if their username is the same as the one we want)
		while(timeElapsed<1000) {
			finish = System.currentTimeMillis();
			timeElapsed = finish - start;			
		}

		// We check if our TCP server has received a message notifying that 
		// The username is available or not
		return isAvailable ;
	}
	
	public static void notifyUsernameUnavailable() {
		isAvailable = false;
	}


	// Once we are connected, we send a broadcast with our username and our IP
	public void notifyConnected(String username) throws IOException {
		String msg = messageFormatter(MessageType.USERNAME_CONNECTED, username, myIPString) ;
		broadcastUDP.envoiBroadcast(msg);
	}
	
	// Notify all users when a username is changed 
	public void notifyUserameChanged(String newUsername) throws IOException {
		String msg = messageFormatter(MessageType.USERNAME_CHANGED, newUsername, myIPString) ;
		broadcastUDP.envoiBroadcast(msg);
	}

	// Notify all users when a user is disconnected 
	public void notifyDisconnected(String username) throws IOException {
		String msg = messageFormatter(MessageType.USERNAME_DISCONNECT, username, myIPString) ;
		broadcastUDP.envoiBroadcast(msg);
	}

	//Broadcast asking everyone for their username
	public void askUsernames(String myusername) throws IOException {
		String msg = messageFormatter(MessageType.GET_USERNAMES, myusername, "") ;
		broadcastUDP.envoiBroadcast(msg);
	}


	//////////////////////////////////////////////////////////////////////
	///////////////////////////////ANSWER/////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	//Call Manager to know if the username wanted is valid
	public static void usernameRequest(String username, InetAddress destinationIP) {
		Manager.usernameRequest(username, destinationIP);
	}


	// The username wanted is not available so we notify the user
	public void sendUnavailableUsername(InetAddress destinationIP) {
		String msg = messageFormatter(MessageType.USERNAME_BRDCST, User.getNickname(), "") ;
		TCPSend.sendMessage(msg, destinationIP);
	}


	// We add the user to the DB
	public static void newUserConnected(String username, InetAddress IP) {
		Manager.newUserConnected(username, IP);
	}

	// Change of username = disconnection with previous username and connection with the new one 
	public static void updateUsername(InetAddress IP, String newUsername) {
		Manager.updateUsername(IP, newUsername) ;
	}
	
	// We delete the user from the DB
	public static void userDisconnected(String username) {
		Manager.userDisconnected(username);
	}


	// If someone asks our username, we answer with our username and our IP
	public static void sendUsername(String destinationUsername) {
		if (!destinationUsername.equals(User.getNickname())) {
			String msg = messageFormatter(MessageType.GET_USERNAMES, User.getNickname(), myIPString) ;
			InetAddress destinationIP = User.getIpAddress();
			TCPSend.sendMessage(msg, destinationIP);
		}
	}

	// When we receive a message, we add it to the history
	public static void notifyNewMessage(String message, String user) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = date.format(format);
		
		String IPSender = Manager.getIP(user).toString() ;
		if (IPSender.charAt(0) == ('/')) {
			IPSender = IPSender.substring(1);
		}
		Manager.addMessageToHistory(IPSender, myIPString, message, formattedDate);
	}


	//////////////////////////////////////////////////////////////////////
	////////////////////////////DISCONNECTION/////////////////////////////
	//////////////////////////////////////////////////////////////////////

	// Principal function of disconnection
	public void disconnection(String username) throws IOException {
		notifyDisconnected(username);
		serverUDP.setOnline(false) ;
		serverTCP.setOnline(false) ;
	}


	public static void main (String [] args) {
		new Thread(new UDPReceive(5001, 50000)).start();
	}


}