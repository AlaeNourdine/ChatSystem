package Controller;

import java.io.IOException;

import View.*;
import Model.User;
import Controller.Controller;
import Network.UDPReceive;
import Network.broadcastUDP;

/**
 * Classe InteractiveChatSystem permettant à l'user de gérer connexion, déconnexion et 
 *   changement de pseudo

 */
public class ControllerChat {
	private static Controller app;
	private UDPReceive serverUDP;

	/*type des messages à envoyer ou recevoir en broadcast correspondant aux différentes
	  situations à gérer par l'user*/
	enum typemsg {DECONNEXION, CONNEXION, CHANGEMENTPSEUDO};
	
	//Constructor
	public ControllerChat(Controller app) {
		this.setApp(app);
		setUdplisten(getApp().getServerUDP());
	}

	
	public boolean Connexion(String nouveau) {
		//envoi broadcast
		serverUDP.setCas(1);
		serverUDP.start();
		int port = 4445;
		try {
		    //System.out.println("Tentative de connexion");
			broadcastUDP.envoibroadcast(("CONNEXION_"+nouveau+"_"+getApp().getMe().getIP()+"_"+port), port);
			Thread.sleep(2000); //on attends les réponses 
		}catch (Exception e) {
			System.out.println("Erreur broadcast dans Connexion");
		}
		finally {
			getApp().getMe().setNickname(nouveau);
			serverUDP.setCas(3);
		}
		return serverUDP.isDisponible();
	}		

	public boolean editNickname(String nouveau, int port) {
		serverUDP.setCas(2);
		try {
		    //System.out.println("Tentative de changement de pseudo en broadcast");
		    broadcastUDP.envoibroadcast(("CHANGEMENTPSEUDO_"+nouveau+"_"+getApp().getMe().getIP()+"_"+getApp().getMe().getPort()), port);
			Thread.sleep(2000); //on attends les réponses 
		}catch (Exception e) {
			System.out.println("Erreur broadcast dans ChangePseudo");
		}
		finally {
			getApp().getMe().setNickname(nouveau);
			serverUDP.setCas(3);

		}
		return serverUDP.isDisponible();

	}
	
	
	public void Deconnexion() {
		int port=4445;
		try {
			//System.out.println("je me deconnecte et je l'envoie en broadcast ");
			broadcastUDP.envoibroadcast(("DECONNEXION_"+getApp().getMe().getNickname()+"_"+getApp().getMe().getIP()+"_"+getApp().getMe().getPort()), port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ReceptionMsg (String msgrecu) {
		String[] splitmessage=msgrecu.split("_");
		if(splitmessage[0].equals("ok")) {
			//nothingtodo 
		}
		else {
		typemsg type= typemsg.valueOf(splitmessage[0]);

		switch (type) {
        case CONNEXION:
    	    //System.out.println(msgrecu);

        	User usertoadd= User.toUser(msgrecu);
     	    if (usertoadd.getNickname().equals(getApp().getMe().getNickname())) {
     		    //System.out.println("pseudo utilisé");
     	    	String envoiko= "notOk"+getApp().getMe().toString();
     	    	try {
     	    		//System.out.println("envoiko "+ usertoadd.getIP());
         	    	broadcastUDP.sendUDP(envoiko, usertoadd.getPort(), usertoadd.getIP());
     	    	}catch (Exception e) {
     	    		System.out.println("Pb envoi UDP KO");
     	    	}
     	    }else{
     		    //System.out.println("pseudo ok");
     	    	String envoiok= "ok"+getApp().getMe().toString();
        	    	try {
     	    		//System.out.println("envoiok "+ usertoadd.getIP());
     				getApp().getActifUsers().addConnectedUser(usertoadd); //on ajoute dans le tableau
     				getApp().getDb().createTableConvo(usertoadd.getIP()); //on ajoute dans la bd
     				broadcastUDP.sendUDP(envoiok, usertoadd.getPort(), usertoadd.getIP());
    	    		Home.displayNotifUsers(usertoadd.getNickname()," just landed \n");
         		    //System.out.println("j'ajoute" +usertoadd+ "et je maj");
        			Home.miseAJourContact();


     	    	}catch (Exception e) {
     	    		System.out.println("Pb envoi UDP OK");
     	    	}    	    
     	    }
     	    break;
     	    
        case CHANGEMENTPSEUDO:
    	    //System.out.println(msgrecu);
    	 
    	    User usertocompare= User.toUser(msgrecu);
    	    //Si c'est moi meme qui recoit
    	    if(usertocompare.getIP().equals(getApp().getMe().getIP())) {
    		    //System.out.println("JE MAUTORISE");
    	    }
    	    else {
    	    	if (usertocompare.getNickname().equals(getApp().getMe().getNickname())) {
    	    		//System.out.println("pseudo utilisé");
    	    		String envoiko= "notOk"+getApp().getMe().toString();
    	    		try {
    	    			//System.out.println("envoiko "+ usertocompare.getIP());
    	    			broadcastUDP.sendUDP(envoiko, 4445, usertocompare.getIP());
    	    		}catch (Exception e) {
    	    			System.out.println("Pb envoi UDP KO");
    	    		}
    	    	}else{
    	    		//System.out.println("pseudo ok");
    	    		String envoiok= "ok"+getApp().getMe().toString();
    	    		try {
    	    			//System.out.println("envoiok "+ usertocompare.getIP());
    	    			broadcastUDP.sendUDP(envoiok, 4445, usertocompare.getIP());
    	    			if (!(getApp().getActifUsers().appartient(usertocompare.getNickname()))) {
    	    				String oldpseudo=getApp().getActifUsers().getUserfromIP(usertocompare.getIP()).getPseudo();
    	    				getApp().getActifUsers().getUserfromIP(usertocompare.getIP()).setPseudo(usertocompare.getPseudo());
    	    				Home.displayNotifUsers(oldpseudo," just changed his pseudo to "+usertocompare.getPseudo()+"\n");
    	    				Home.miseAJourContact();
    	    				if (Home.getTalkingto().getText()!=usertocompare.getNickname()) {
    	    					Home.getTalkingto().setText(usertocompare.getNickname());
    	    				}
    	    			}
    	    		}catch (Exception e) {
    	    			System.out.println("Pb envoi UDP OK");
    	    		}    	    
    	    	}	
    	    }
    	    break;

        case DECONNEXION:
    	    //System.out.println(msgrecu);
    	    User usertodisconnect= User.toUser(msgrecu);
    	    //Si c'est moi meme qui recoit
    	    if(usertodisconnect.getIP().equals(getApp().getMe().getIP())) {
    		    //System.out.println("JE MAUTORISE");
    	    }
    	    else {
    	    	getApp().getActifUsers().deleteContact(getApp().getActifUsers().getUserfromPseudo(usertodisconnect.getPseudo()));
	    		Home.displayNotifUsers(usertodisconnect.getNickname()," just disconnect \n");
	    		if (Home.getTalkingto().getText().equals(usertodisconnect.getNickname())) {
	    			Home.getTalkingto().setText("");
	    			Home.clearMessagesArea();
	    		}
    	    	Home.miseAJourContact();
    	    }
        	break;
		default:
			break;
        
        }
		}
	}


	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public static Controller getApp() {
		return app;
	}

	public void setApp(Controller app) {
		ControllerChat.app = app;
	}



	public UDPReceive getServerUDP() {
		return serverUDP;
	}



	public void setUdplisten(UDPReceive serverUDP) {
		this.serverUDP = serverUDP;
	}


}

