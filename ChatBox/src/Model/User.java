package Model;

import Network.UDPReceive;

/**
 * Classe qui representer un utilisateur de l'application
 * Cet utilisateur est caracterise par 3 attributs :
 * - ipAddress : son adresse IP
 * - nickname : son pseudo
 * - port : son numero de port
 */

public class User {
	
	private String ipAddress;
	private int port;
	private String nickname;
	
	
	/**
	 * 1) Constructeur d'un User sans attributs 
	 */
	public User() {
		this.setIP(UDPReceive.getCurrentIp().getHostAddress());
		this.setPort(1234);
	}
	
	/**
	 * 2) Constructeur d'un User
	 * @param address ip de l'utilisateur
	 * @param port port de l'utilisateur
	 * @param nickname pseudo l'utilisateur
	 */
	public User(String address, int port, String pseudonym) {
		this.setIP(address);
		this.setPort(port);
		this.setNickname(pseudonym);
	}
	 
	
	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public String getIP() {
		return ipAddress;
	}

	public void setIP(String address) {
		this.ipAddress = address;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String pseudo) {
		this.nickname = pseudo;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int p) {
		this.port = p;
	}
	
	
	//-------------------- Methodes -----------------------------//

	/**
	 * Methode pour renvoyer les caracteristiques d'un utilsiateur
	 */
	@Override
	public String toString() {
		return "_"+this.nickname+"_"+this.ipAddress+"_"+String.valueOf(this.port);
	}
	
	/**
	 * Methode pour creer un type User a partir de ses caracteristiques de type String recuperee
	 * @param s 
	 * @return User
	 */
	public static User toUser(String s) {
		String[] parametersuser=s.split("_");
		//String validate= parametersuser[0];
		String userpseudo = parametersuser[1];
		String userip = parametersuser[2];
		String userport = parametersuser[3];
		User people= new User(userip, Integer.parseInt(userport), userpseudo);
		return people;
	}
	
	


}
