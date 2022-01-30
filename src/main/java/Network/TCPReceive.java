package Network;

import Controller.Controller;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceive extends Thread{

	private Controller app;
    byte[] array = new byte[100000000];
	private static boolean ouvert;
	private static Socket Csocket;

	//Constructor
	public TCPReceive(Controller app) {
		this.app=app;
		setOuvert(true);

	}
		
	//Threads ecoutant les demandes 
	public void run() {
		
		 ServerSocket server;
	        try {
	            server = new ServerSocket(2000); 
	            //System.out.println("listening on port 2000 ready to have conversation");
	            while(ouvert) { 
	                Csocket = server.accept(); 
	                TCPSend chat = new TCPSend(app,Csocket);
	                
	            }
	            //link.close();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
		//-------------------- GETTEURS & SETTEURS -----------------------------//

	    public boolean isOuvert() {
			return ouvert;
		}

		public static void setOuvert(boolean ouvert) {
			TCPReceive.ouvert = ouvert;
		}

	}