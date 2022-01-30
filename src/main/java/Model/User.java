package Model;

import Network.UDPReceive;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Classe représentant un utilisateur de l'application
 * ip : addresse ip de l'user (en string)
 * pseudo : pseudo de l'user
 * port : numéro de port de l'user
 *
 */

public class User {
	
	private String ipAddress;
    private String nickname;
    private int port ; 
    
    
    public User (){
    	
    	this.setIP(UDPReceive.getCurrentIp().getHostAddress());
		this.setPort(1234);
	}
    
    //Constructor
    public User(String ipaddress, int port, String nickname) {
		this.setIP(ipaddress);
		this.setPort(port);
		this.setNickname(nickname);
	}
    
    public String toString() {
		return "_"+this.nickname+"_"+this.ipAddress+"_"+String.valueOf(this.port);
	}
    
    public static User toUser(String s) {
		String[] parametersuser=s.split("_");
		//String validate= parametersuser[0];
		String userpseudo = parametersuser[1];
		String userip = parametersuser[2];
		String userport = parametersuser[3];
		User people= new User(userip, Integer.parseInt(userport), userpseudo);
		return people;
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

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int p) {
		this.port = p;
	}

}
