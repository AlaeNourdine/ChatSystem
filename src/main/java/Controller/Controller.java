package Controller;

import Network.* ;
import Model.*;

public class Controller {
	
	public static int maxSessions;
	protected static User me ;
	private static ControllerChat cc ;
	private static BDD db ;
	private ActifsUsers actifs;
	private UDPReceive serverUDP ;
	private Exception tooMuchSessions ;
	
	public Controller(User user1) {
		this.setMe(user1);
		if (!(maxSessions>49)) {
			maxSessions++;
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

	public ActifsUsers getActifUsers() {
		return actifs;
	}

	public void setActifUsers(ActifsUsers actifs) {
		this.actifs = actifs;
	}

	public ControllerChat getControlChat() {
		return cc;
	}

	public void setcSystem(ControllerChat cc) {
		this.cc = cc;
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
	
	public void setServerUDP(UDPReceive serverUDP) {
		this.serverUDP = serverUDP;
	}

	
}