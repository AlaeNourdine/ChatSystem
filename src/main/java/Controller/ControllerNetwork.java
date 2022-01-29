package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import Model.Messages;
import Model.User;
import Network.TCPReceive;
import Network.TCPSend;
import Network.UDPReceive;
import Network.broadcastUDP;
import Network.NetworkManager.MessageType;

public class ControllerNetwork {

	TCPReceive serverTCP ; 
	UDPReceive serverUDP ;
	static InetAddress ipAddress ;
	static String iptoString ;
	static boolean isAvailable ;
	
	public ControllerNetwork() {
		
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface ni = en.nextElement();

				List<InterfaceAddress> list = ni.getInterfaceAddresses();
				Iterator<InterfaceAddress> it = list.iterator();

				while (it.hasNext()) {
					InterfaceAddress ia = it.next();
					if (ia.getBroadcast()!=null && ipAddress == null) {
						ipAddress = ia.getAddress() ;
					}
				}		
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		iptoString = ipAddress.toString();
		if (iptoString.charAt(0) == ('/')) {
			iptoString = iptoString.substring(1);
		}
	}
	
	public void runServers() {
		new Thread(this.serverTCP).start();
		new Thread(this.serverUDP).start();
	}
	
	public void sendMessage(String msg, InetAddress destinationIP) {
		String FormatdudMsg = Messages.msgForme( msg, User.getNickname(),  Messages.Type.MESSAGE);
		TCPSend.sendMessage(FormatdudMsg, destinationIP);
	}
	
	public synchronized boolean usernameAvailable(String username) throws IOException {
		isAvailable = true ;
		long timeElapsed = 0;
		long start = System.currentTimeMillis();
		long finish = 0;
		String msg = Messages.msgForme( iptoString, username,  Messages.Type.BROADCAST_NICKNAME);

		
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

	public void notifyConnected(String username) throws IOException {
		String msg = Messages.msgForme( iptoString, username,  Messages.Type.CONNEXION) ;
		broadcastUDP.envoiBroadcast(msg);
	}
	
	public void notifyUserameChanged(String nouveau) throws IOException {
		String msg = Messages.msgForme( iptoString, nouveau,  Messages.Type.USERNAME_EDIT) ;
		broadcastUDP.envoiBroadcast(msg);
	}
	
	public void notifyDisconnected(String username) throws IOException {
		String msg = Messages.msgForme( iptoString, username,  Messages.Type.DECONNEXION) ;
		broadcastUDP.envoiBroadcast(msg);
	}
	
	public void askUsernames(String username) throws IOException {
		String msg = Messages.msgForme( "", username,  Messages.Type.GET_ALL_USERS) ;
		broadcastUDP.envoiBroadcast(msg);
	}
	
	
	public static void usernameRequest(String username, InetAddress destinationIP) {
		Manager.usernameRequest(username, destinationIP);
	}


	public void sendUnavailableUsername(InetAddress destinationIP) {
		String msg = Messages.msgForme( "", User.getNickname(),  Messages.Type.BROADCAST_NICKNAME) ;
		TCPSend.sendMessage(msg, destinationIP);
	}


	public static void newUserConnected(String username, InetAddress IP) {
		Manager.newUserConnected(username, IP);
	}

	public static void updateUsername(InetAddress IP, String newUsername) {
		Manager.updateUsername(IP, newUsername) ;
	}
	
	public static void userDisconnected(String username) {
		Manager.userDisconnected(username);
	}

	
	public static void notifyNewMessage(String message, String user) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = date.format(format);
		
		String IPSender = Manager.getIP(user).toString() ;
		if (IPSender.charAt(0) == ('/')) {
			IPSender = IPSender.substring(1);
		}
		Manager.addMessageToHistory(IPSender, iptoString, message, formattedDate);
	}

	public void disconnection(String username) throws IOException {
		notifyDisconnected(username);
		serverUDP.setOnline(false) ;
		serverTCP.setOnline(false) ;
	}
}
