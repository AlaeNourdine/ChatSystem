package Controller;

import Model.*;
import Network.UDPReceive;

/**
 * Classe Controller qui regroupe toutes les instances necessaires a l'utilsiation
 *  
 */

public class Controller {

	private User me;
	private ControllerChat cSystem;
	private BDD db;
	private UDPReceive serverUDP;
	private ActifsUsers actifs;
	public static int maxConnection;
	public Exception tooMuchSessions;

	/**
	 * Constructeur de la classe Controller
	 * @param u1 User
	 * */
	public Controller(User u1) {
		this.setMe(u1);
		if (!(maxConnection>49)) {
			maxConnection++;
		}else {
			
		}
		setActifUsers(new ActifsUsers());
	}
	

	//-------------------- GETTEURS & SETTEURS -----------------------------//
	
	public User getMe() {
		return me;
	}

	public void setMe(User me) {
		this.me = me;
	}

	public ActifsUsers getActifsUsers() {
		return actifs;
	}

	public void setActifUsers(ActifsUsers actifs) {
		this.actifs = actifs;
	}

	public ControllerChat getcSystem() {
		return cSystem;
	}

	public void setcSystem(ControllerChat cSystem) {
		this.cSystem = cSystem;
	}

	public BDD getDb() {
		return db;
	}

	public void setDb(BDD db) {
		this.db = db;
	}


	public UDPReceive getServerUDP() {
		return serverUDP;
	}


	public void setServerUDP(UDPReceive udplisten) {
		this.serverUDP = udplisten;
	}


}
