package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPReceive {

	private static ServerSocket ssocket;
	private static Socket clientsocket;
	private static int count=0;
	private static ArrayList<ServerThread> threadslist ;

	
	public static void main(String[] args) throws IOException {

		threadslist = new ArrayList<>();
		ssocket = new ServerSocket(4567);
		
		
		while(true) {
			System.out.println("Waiting for connection...");
			clientsocket = ssocket.accept();
			count++;
			System.out.println("Connected to client n° "+count);
			ServerThread serverconn = new ServerThread(clientsocket,count);
			threadslist.add(serverconn);
			serverconn.start();


		}
	}
	

	public static ArrayList<ServerThread> getThreadslist() {
		return threadslist;
	}


	public static void setThreadslist(ArrayList<ServerThread> threadslist) {
		TCPReceive.threadslist = threadslist;
	}
	
	boolean isAvailable = true;	
	boolean online = true ; 
	
	public boolean getAvailable() {
		return this.isAvailable ; 
	}
	
	public void setOnline(boolean statut) {
		this.online = statut ; 
	}
}
