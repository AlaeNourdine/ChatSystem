package Network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import Controller.*;
import Model.*;

/**
 * Classe UDPReceive
 *
 */


public class UDPReceive extends Thread {
	private static DatagramSocket serverSocket;
	private DatagramPacket receivePacket;
	private boolean ouvert;
	private boolean disponible;
	private int cas; //1-> connexion 2 -> changement pseudo 3 -> �coute udp 
	private Controller app;
	private User usertoadd ;

	/**
	 * Constructeur UDPRunner pour ouvrir le socket
	 */
	public UDPReceive(Controller app) {
		setOuvert(true);
		setApp(app);
		setDisponible(true);
	}
	
	//-------------------- GETTEURS & SETTEURS -----------------------------//
	
	public Controller getApp() {
		return app;
	}

	public void setApp(Controller app) {
		this.app = app;
	}

	public void closeSocket() {
		serverSocket.close();
	}
	
	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	public boolean isOuvert() {
		return ouvert;
	}

	public void setOuvert(boolean ouvert) {
		this.ouvert = ouvert;
	}

	public int getCas() {
		return cas;
	}

	public void setCas(int cas) {
		this.cas = cas;
	}
	
	
	
	//-------------------- Methodes-----------------------------//

	
	/**
	 * Methode pour recevoir en UDP
	 * Pour la connexion, deconnexion et changement de pseudo
	 */
	public void run() {  
		int serverPort=4445;
		try {
	        serverSocket = new DatagramSocket(serverPort);
	        String sentence="";
	        byte[] array = new byte[100000000];

	        //System.out.printf("Listening on udp:%s:%d%n", getCurrentIp().getHostAddress(), serverPort);
        	while (ouvert) {
        		//cas de la connexion
        		if (getCas()==1) {
	        		try {	
	        	        serverSocket.setSoTimeout(2000);
	        			receivePacket = new DatagramPacket(array, array.length);
	        			serverSocket.receive(receivePacket);
	        			sentence = new String( receivePacket.getData(), 0, receivePacket.getLength() );
	        			//System.out.println("On a re�u: "+ sentence);
	        			User usertoadd= User.toUser(sentence);
	        			String[] parametersuser=sentence.split("_");
	        			String validate= parametersuser[0];
	        			//Si r�ponse n�gative then renvoi faux
	        			if (validate.equals("notOk")) {
	        				//System.out.println("pseudo Not ok");
	        				setDisponible(false);
	        				setCas(3);
	        			}else {
	        				//Si r�ponse positive then renvoi vrai
	        				//System.out.println("pseudo ok");
	        				setDisponible(true);
	        				if(usertoadd.getIP().equals(getApp().getMe().getIP())) {
	        					//nothing to do
	        				}
	        				else if(!(usertoadd.getIP().equals("IP"))) {
	        					//si on est le 1er du r�seau on ajoute personne 
	        					//System.out.println("on ajoute "+usertoadd);
	        					getApp().getActifsUsers().addConnectedUser(usertoadd);
	        					getApp().getDb().createTableConvo(usertoadd.getIP()); //on ajoute dans la bd

	        				}
	        			}

	        		}
	        		catch(SocketTimeoutException e){
	        			sentence="ok_pseudo_IP_4445";
        				setDisponible(true);
	        		}
	        		catch(Exception e) {
	        			e.printStackTrace();
	        		}

	        	}
        		
        	//cas du changement de pseudo
	        else if (getCas()==2) {
	        		try {			
	        	        serverSocket.setSoTimeout(2000);
	        			receivePacket = new DatagramPacket(array, array.length);
	        			serverSocket.receive(receivePacket);
	        			sentence = new String( receivePacket.getData(), 0, receivePacket.getLength() );
	        			//System.out.println("On a re�u: "+ sentence);
	        			setUsertoadd(User.toUser(sentence));
	        			String[] parametersuser=sentence.split("_");
	        			String validate= parametersuser[0];
	        			//Si r�ponse n�gative then renvoi faux
	        			if (validate.equals("notOk")) {
	        				//System.out.println("pseudo Not ok");
	        				setDisponible(false);
	        				setCas(3);
	        			}else {
	        				//Si r�ponse positive then renvoi vrai
	        				//System.out.println("pseudo ok");
	        				setDisponible(true);
	        			}
	        		}
	        		catch (SocketTimeoutException e ) {
        				setDisponible(true);

	        		}
	        		catch(Exception e) {
	        			e.printStackTrace();
	        		}
	        	}
        		
        	//cas �coute udp
	        else if (getCas()==3) {
	        		serverSocket.close();
	    	        serverSocket = new DatagramSocket(serverPort); //on le recr�e pour qu'il n'est pas le settime out
			        //System.out.printf("Listening on udp:%s:%d%n", getCurrentIp().getHostAddress(), 4445);     
			        receivePacket = new DatagramPacket(array, array.length);
			        serverSocket.receive(receivePacket);
			        String response = new String( receivePacket.getData(), 0, receivePacket.getLength() );
			        //System.out.println("on va dans receptionmsg\n");
			        ControllerChat.ReceptionMsg(response);
			        
				}
	        }

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode pour recuperer l'adresse IP de l'hote
	 */
	 public static InetAddress getCurrentIp() { 
		 try { 
			 Enumeration networkInterfaces = NetworkInterface .getNetworkInterfaces();
			 while (networkInterfaces.hasMoreElements()) { 
				 NetworkInterface ni = (NetworkInterface) networkInterfaces .nextElement(); 
				 Enumeration nias = ni.getInetAddresses(); 
				 while(nias.hasMoreElements()) { 
					 InetAddress ia= (InetAddress) nias.nextElement();
					 if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) { 
						 return ia; 
					 } 
				 } 
			 } 
		 } 
		 catch (SocketException e) {
			 System.out.println("unable to get current IP " + e.getMessage());
		 } 
	return null;
	}

	public User getUsertoadd() {
		return usertoadd;
	}

	public void setUsertoadd(User usertoadd) {
		this.usertoadd = usertoadd;
	} 
	 

}
