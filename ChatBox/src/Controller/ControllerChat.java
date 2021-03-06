package Controller;

import java.io.IOException;

import Model.User;
import View.*;
import Network.UDPReceive;
import Network.broadcastUDP;

/**
 * Classe ControllerChat qui permet a l'utilisateur de controller les connexion, deconnexion et changement de pseudo
 */
public class ControllerChat {
	private static Controller app;
	private UDPReceive udplisten;


	/**
	 * type des messages a envoyer ou recevoie en broadcastUDP
	 * ces types sont equivalents aux differents scenarios que peut faire un utilisateur : Connexion, Deconnexion et Changement de Pseudp
	 * @author maher
	 */
	enum typemsg { CONNEXION,DECONNEXION,CHANGEMENTPSEUDO};
	
	
	
	/**
	 * Constructeur de la classe ControllerChat
	 * @param app Controller 
	 */
	public ControllerChat(Controller app) {
		this.setApp(app);
		setUdplisten(getApp().getServerUDP());
	}

	
	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public static Controller getApp() {
		return app;
	}

	public void setApp(Controller app) {
		ControllerChat.app = app;
	}



	public UDPReceive getServerUDP() {
		return udplisten;
	}



	public void setUdplisten(UDPReceive udplisten) {
		this.udplisten = udplisten;
	}

	
	
	/**
	 * Methode pour la connexion d'un utilisateur
	 * @param newPseudo String
	 * @return boolean
	 */
	public boolean Connexion(String newPseudo) {
		udplisten.setCas(1);
		udplisten.start();
		int port = 4445;
		try {
			broadcastUDP.envoiebroadcast(("CONNEXION_"+newPseudo+"_"+getApp().getMe().getIP()+"_"+port), port);
			Thread.sleep(2000); 
		}catch (Exception e) {
			System.out.println("Erreur broadcast dans Connexion");
		}
		finally {
			getApp().getMe().setNickname(newPseudo);
			udplisten.setCas(3);
		}
		return udplisten.isDisponible();
	}		

	/**
	 * Methode pour changer le pseudo
	 * @param newPseudo String 
	 * @param port int
	 * @return boolean
	 */
	public boolean editNickname(String newPseudo, int port) {
		udplisten.setCas(2);
		try {
		    //System.out.println("Tentative de changement de pseudo en broadcast");
			broadcastUDP.envoiebroadcast(("CHANGEMENTPSEUDO_"+newPseudo+"_"+getApp().getMe().getIP()+"_"+getApp().getMe().getPort()), port);
			Thread.sleep(2000); //on attends les r???ponses 
		}catch (Exception e) {
			System.out.println("Erreur broadcast dans ChangePseudo");
		}
		finally {
			getApp().getMe().setNickname(newPseudo);
			udplisten.setCas(3);

		}
		return udplisten.isDisponible();

	}
	
	
	
	/**
	 * Methode pour se deconnecter
	 */
	public void Deconnexion() {
		int port=4445;
		try {
			broadcastUDP.envoiebroadcast(("DECONNEXION_"+getApp().getMe().getNickname()+"_"+getApp().getMe().getIP()+"_"+getApp().getMe().getPort()), port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode pour recevoir un message en broadcastUDP
	 * @param msgrecu String
	 */ 
	public static void ReceptionMsg (String msgrecu) {
		String[] splitmessage=msgrecu.split("_");
		if(splitmessage[0].equals("ok")) {
		}
		else {
		typemsg type= typemsg.valueOf(splitmessage[0]);

		switch (type) {
        case CONNEXION:
        	User usertoadd= User.toUser(msgrecu);
     	    if (usertoadd.getNickname().equals(getApp().getMe().getNickname())) {
     		    System.out.println("pseudo utilis??");
     	    	String envoiko= "notOk"+getApp().getMe().toString();
     		    System.out.println(envoiko);
     	    	try {
     	    		broadcastUDP.sendUDP("envoiko", usertoadd.getPort(), usertoadd.getIP());
     	    	}catch (Exception e) {
     	    		System.out.println("Pb envoi UDP KO");
     	    	}
     	    }else{
     	    	String envoiok= "ok"+getApp().getMe().toString();
        	    	try {
     				getApp().getActifsUsers().addConnectedUser(usertoadd); //on ajoute dans le tableau
     				getApp().getDb().createTableConvo(usertoadd.getIP()); //on ajoute dans la bd
     				broadcastUDP.sendUDP(envoiok, usertoadd.getPort(), usertoadd.getIP());
    	    		General.displayNotifUsers("" + usertoadd.getNickname()," vient d'arriver.\n");
        			General.miseAJourContact();


     	    	}catch (Exception e) {
     	    		System.out.println("Pb envoi UDP OK");
     	    	}    	    
     	    }
     	    break;
     	    
        case CHANGEMENTPSEUDO:
    	 
    	    User usertocompare= User.toUser(msgrecu);
    	    if(usertocompare.getIP().equals(getApp().getMe().getIP())) {
    	    }
    	    else {
    	    	if (usertocompare.getNickname().equals(getApp().getMe().getNickname())) {
    	    		String envoiko= "notOk"+getApp().getMe().toString();
    	    		try {
    	    			broadcastUDP.sendUDP(envoiko, 4445, usertocompare.getIP());
    	    		}catch (Exception e) {
    	    			System.out.println("Pb envoi UDP KO");
    	    		}
    	    	}else{
    	    		String envoiok= "ok"+getApp().getMe().toString();
    	    		try {
    	    			broadcastUDP.sendUDP(envoiok, 4445, usertocompare.getIP());
    	    			if (!(getApp().getActifsUsers().appartient(usertocompare.getNickname()))) {
    	    				String oldpseudo=getApp().getActifsUsers().getUserfromIP(usertocompare.getIP()).getNickname();
    	    				getApp().getActifsUsers().getUserfromIP(usertocompare.getIP()).setNickname(usertocompare.getNickname());
    	    				General.displayNotifUsers("" + oldpseudo," a chang?? son pseudo ?? "+usertocompare.getNickname()+"\n");
    	    				General.miseAJourContact();
    	    				if (General.getTalkingto().getText()!=usertocompare.getNickname()) {
    	    					General.getTalkingto().setText(usertocompare.getNickname());
    	    				}
    	    			}
    	    		}catch (Exception e) {
    	    			System.out.println("Pb envoi UDP OK");
    	    		}    	    
    	    	}	
    	    }
    	    break;

        case DECONNEXION:
    	    User usertodisconnect= User.toUser(msgrecu);
    	    if(usertodisconnect.getIP().equals(getApp().getMe().getIP())) {
    	    }
    	    else {
    	    	getApp().getActifsUsers().deleteUser(getApp().getActifsUsers().getUserfromPseudo(usertodisconnect.getNickname()));
	    		General.displayNotifUsers("" + usertodisconnect.getNickname()," vient de se d??connecter.\n");
	    		if (General.getTalkingto().getText().equals(usertodisconnect.getNickname())) {
	    			General.getTalkingto().setText("");
	    			General.clearMessagesArea();
	    		}
    	    	General.miseAJourContact();
    	    }
        	break;
		default:
			break;
        
        }
		}
	}




}
