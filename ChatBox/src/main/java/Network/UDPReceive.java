package Network ;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;

import Model.*;
import Controller.*;

/**
 * Classe UDPRunner pour être à l'écoute lors des demandes de connexions, changements pseudos et autres
 * cas : integer permettant de savoir quel problème gérer entre la connexion, changement de pseudo et écoute
 * ouvert : booléen permettant de gérer la fermeture du socket
 * disponible : booléen permettant de savoir si le pseudo est disponible (unique) dans le système
 * app : Application associée
 * 
 */

public class UDPReceive extends Thread{
	
	private int cas ;
	private int port;
	private boolean ouvert ; 
	private boolean disponible ;
	private DatagramPacket rPacket;
	private static DatagramSocket sSocket;
	private Controller app ;
	
	//Constructor
	public UDPReceive(Controller app) {
		setOuvert(true);
		setApp(app);
		setDisponible(true);
	}
	
	/**
	 * Réception UDP correspondant à la connexion, changement de pseudos et des arrivées et départ
	 *   
	 */
	public void run() {
		int serverPort=4445;
		try {
			sSocket = new DatagramSocket(serverPort);
	        String sentence="";
	        byte[] array = new byte[100000000];

	        //System.out.printf("Listening on udp:%s:%d%n", getCurrentIp().getHostAddress(), serverPort);
        	while (ouvert) {
        		//cas de la connexion
        		if (getCas()==1) {
	        		try {	
	        	        sSocket.setSoTimeout(2000);
	        	        rPacket = new DatagramPacket(array, array.length);
	        			sSocket.receive(rPacket);
	        			sentence = new String( rPacket.getData(), 0, rPacket.getLength() );
	        			//System.out.println("On a reçu: "+ sentence);
	        			User usertoadd= User.toUser(sentence);
	        			String[] parametersuser=sentence.split("_");
	        			String validate= parametersuser[0];
	        			//Si réponse négative then renvoi faux
	        			if (validate.equals("notOk")) {
	        				//System.out.println("pseudo Not ok");
	        				setDisponible(false);
	        				setCas(3);
	        			}else {
	        				//Si réponse positive then renvoi vrai
	        				//System.out.println("pseudo ok");
	        				setDisponible(true);
	        				if(usertoadd.getIP().equals(getApp().getMe().getIP())) {
	        					//nothing to do
	        				}
	        				else if(!(usertoadd.getIP().equals("IP"))) {
	        					//si on est le 1er du réseau on ajoute personne 
	        					//System.out.println("on ajoute "+usertoadd);
	        					getApp().getActifUsers().addConnectedUser(usertoadd);
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
	        			sSocket.setSoTimeout(2000);
	        			rPacket = new DatagramPacket(array, array.length);
	        			sSocket.receive(rPacket);
	        			sentence = new String( rPacket.getData(), 0, rPacket.getLength() );
	        			//System.out.println("On a reçu: "+ sentence);
	        			User usertoadd= User.toUser(sentence);
	        			String[] parametersuser=sentence.split("_");
	        			String validate= parametersuser[0];
	        			//Si réponse négative then renvoi faux
	        			if (validate.equals("notOk")) {
	        				//System.out.println("pseudo Not ok");
	        				setDisponible(false);
	        				setCas(3);
	        			}else {
	        				//Si réponse positive then renvoi vrai
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
        		
        	//cas écoute udp
	        else if (getCas()==3) {
	        		sSocket.close();
	        		sSocket = new DatagramSocket(serverPort); //on le recrée pour qu'il n'est pas le settime out
			        //System.out.printf("Listening on udp:%s:%d%n", getCurrentIp().getHostAddress(), 4445);     
			        rPacket = new DatagramPacket(array, array.length);
			        sSocket.receive(rPacket);
			        String response = new String( rPacket.getData(), 0, rPacket.getLength() );
			        //System.out.println("on va dans receptionmsg\n");
			        ControllerChat.ReceptionMsg(response);
			        
				}
	        }

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	 
	//-------------------- GETTEURS & SETTEURS -----------------------------//
	
	public Controller getApp() {
		return app;
	}

	public void setApp(Controller app) {
		this.app = app;
	}

	public void closeSocket() {
		sSocket.close();
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
}
