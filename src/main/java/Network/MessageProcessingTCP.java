package Network;

import Controller.*;
import Model.*;


import java.io.*;
import java.net.InetAddress;
//import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MessageProcessingTCP implements Runnable {

	// Attributes 
	Socket clientSocket ;
	ServerSocket serverSocket ;
	TCPReceive serverTCP ; 
	String message;
	boolean isAvailable;

	// Constructor 
	public MessageProcessingTCP(Socket client, ServerSocket serverSocket, TCPReceive serverTCP) {
		this.clientSocket = client ;
		this.serverSocket = serverSocket ;
		this.serverTCP = serverTCP ; 
	}

	// Getters 
	public boolean getAvailable() {
		return this.isAvailable;
	}

	public String getMessage() {
		return this.message;
	}

	public void dataFilter(String msg) {
		String[] token = msg.split("/-/");
		Messages.Type type = Messages.Type.valueOf(token[2].toUpperCase());
		String username = token[1];
		String payload = token[0];

		switch(type) {

		case BROADCAST_NICKNAME:
			ControllerNetwork.notifyUsernameUnavailable();
			break;

		case GET_ALL_USERS:
			try {
				payload = token[0];
				InetAddress IP = InetAddress.getByName(payload); 
				ControllerNetwork.newUserConnected(username, IP);
			}
			catch(Exception e) {
				System.out.println(e);
			}
			break;

		case MESSAGE:
			payload = token[0];
			ControllerNetwork.notifyNewMessage(payload, username);
			break;

		default:
			break;
		}
	}


	public void forbidUsername() {
		this.serverTCP.isAvailable = false ;
		this.isAvailable = true ; 
	}


	public void processMessage() {
	}


	public void run() {
		BufferedReader input ;		
		try {
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String msg ;
			msg = input.readLine();


			if (!this.isAvailable) {
				forbidUsername() ; 
			} 
			else {
				processMessage() ; 
			}

			input.close();
		}
		catch (Exception e) {
			System.out.println(e) ;
		}

	}

}