package Network;

import View.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class TCPSend {
	
	private static Socket socket;
	private static String nickname;

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		socket = new Socket("localhost", 4567);
		PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
		
		ServerConnection serverConnection = new ServerConnection(socket); 
		serverConnection.start();
		
		System.out.println("Enter nickname");
		//On a utilise la solution proposee car le scanner ne se ferme jamais
		try (Scanner s = new Scanner(System.in)) {
			nickname = s.nextLine();
			output.println(nickname);

			
			String query=s.nextLine();
			while(query!=null) {
				output.println(query);
				query=s.nextLine();
			}
		}
		
		
		new View(out) ;
	}
	public static void sendMessage(String formatedMsg, InetAddress destinationIP) {
		int port = 5000;
		PrintWriter output;
		try {
				
			Socket clientSocket = new Socket(destinationIP, port);
			output = new PrintWriter(clientSocket.getOutputStream());
				
			output.println(formatedMsg);
			output.flush() ;
			output.close();
			clientSocket.close();
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}		
	
	
}	