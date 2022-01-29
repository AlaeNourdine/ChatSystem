package Network ;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import Model.*;
import Controller.*;

public class UDPReceive extends Thread{
	int port;
	int length;
	boolean connected ; 
	
	public UDPReceive(int port, int length) {
		this.port = port;
		this.length = length;
		this.connected = true ; 
	}
	

	public void setOnline(boolean statut) {
		this.connected = statut ; 
	}
	
	
	public void dataProcessing(String data) {
		String[] token = data.split("/-/");
		
		Messages.Type type = Messages.Type.valueOf(token[2].toUpperCase());
		String username = token[1];
		String content = token[0];
		
		switch (type) {
		
		case BROADCAST_NICKNAME:
			try {
				content = token[2];
				InetAddress IP = InetAddress.getByName(content); 
				ControllerNetwork.usernameRequest(username,IP);				
			}
			catch(Exception e) {
				System.out.println(e);
			}
			break;
			
		case CONNEXION:
			try {
				content = token[2];
				InetAddress IP = InetAddress.getByName(content); 
				ControllerNetwork.newUserConnected(username,IP);				
			}
			catch(Exception e) {
				System.out.println(e);
			}
			break;
			
		case DECONNEXION:
			ControllerNetwork.userDisconnected(username);
			break;
			
		case GET_ALL_USERS:
			ControllerNetwork.sendNickname(username);
			break;
			
		case USERNAME_EDIT: 
			try {
				content = token[2];
				InetAddress IP = InetAddress.getByName(content); 
				ControllerNetwork.updateUsername(IP, username) ; 
			}
			catch(Exception e) {
				System.out.println(e);
			}
			
		
		default:
			break;
			
		}
		
	}
	
	public void run() {
		try {
			
			DatagramSocket socket = new DatagramSocket(port);
			DatagramPacket packet = null;
			byte[] buffer = new byte[length];
			
			while(this.connected) {
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String data = new String(packet.getData(), 0, packet.getLength());
				
				dataProcessing(data);
				
				// Clear buffer
				buffer = new byte[length];
			}
			
			socket.close();
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}