package Network;

import java.net.ServerSocket;
import java.net.Socket;

import Controller.*;

/**
 * Classe TCP Receive
 * 
 */

public class TCPReceive extends Thread {
	TCPSend chat ;
	private Socket link;
    byte[] array = new byte[100000000];
	private static boolean ouvert;
	private Controller app;
    
	/**
	 * Constructeur de TCPReceive
	 * @param app Controller
	 */
	public TCPReceive(Controller app) {
        this.app=app;
		setOuvert(true);

    }

	//-------------------- GETTEURS & SETTEURS -----------------------------//

    public boolean isOuvert() {
		return ouvert;
	}

	public static void setOuvert(boolean ouvert) {
		TCPReceive.ouvert = ouvert;
	}

	
	//-------------------- Methodes -----------------------------//

	
	/**
	 * Methode pour ecouter les demandes de chat
	 */
    public void run() {
        ServerSocket server;
        try {
            server = new ServerSocket(2000); 
            //System.out.println("listening on port 2000 ready to have conversation");
            while(ouvert) { 
                link = server.accept(); 
                chat = new TCPSend(app,link);
                
            }
            //link.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

}