package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe message. Un message est caracterise par
 * emetteur : la personne qui envoie le message 
 * destinataire : la personne qui recoit le message
 * data : le message envoye
 * time : horodatage (en string)
 * 
 */

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private User emetteur;
	private User destinataire;
	private String data;
	private String time; 
	
	
	

	/**
	 * 1er Constructeur d'un message, sans attribut 
	 */
	public Message() {
	}
	
	/**
	 * 2eme Constructeur d'un message
	 * @param from User
	 * @param to User
	 * @param msg String
	 */
	public Message(User from, User to, String msg) {
		this.setEmetteur(from);
		this.setDestinataire(to);
		this.setData(msg);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.setTime(dateFormat.format(new Date()));
	}
	
	
	/**
	 * 3meme Constructeur d'un message
	 * @param from User
	 * @param to User
	 * @param msg String
	 * @param date String
	 */
	public Message(User from, User to, String msg, String date) {
		this.setEmetteur(from);
		this.setDestinataire(to);
		this.setData(msg);
		this.setTimeString(date);
	}
	
	
	/**
	 * 4eme Constructeur d'un message
	 * @param msg String
	 */
	public Message(String msg) {
		this.setData(msg);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.setTime(dateFormat.format(new Date()));

	}
	
	
	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public User getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(User user) {
		this.emetteur = user;
	}

	public User getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(User destinataire) {
		this.destinataire = destinataire;
	}

	public String getTime() {
		return time;
	}
	
	public String getTimeString() {
		return time.toString();
	}

	public void setTime(String string) {
		this.time = string;
	}
	
	public void setTimeString (String date) {
		this.time = date;
	}

	
	
	/**
	 * Methode pour reecrire les informations d'un message. Retourne cela en String
	 */
	@Override
	public String toString() {
		String smsg= "Sender: "+this.getEmetteur()+"\n"+"Receiver:  "+this.getDestinataire()+"\n"
	+"Time:  "+ this.getTime()+"\n"+ "Data:  "+this.getData()+"\n";
		return smsg;	
	}
	
	/**
	 * Methode pour retranscrire un message 
	 * @param smsg String 
	 */
	public static Message toMessage(String smsg) {
		String[] paramsg=smsg.split("\n");
		User sender= User.toUser(paramsg[0].split(":")[1]);
		User receiver= User.toUser(paramsg[1].split(":")[1]);
		String[] fulldate=paramsg[2].split(":");
		String date= (fulldate[1]+":"+fulldate[2]);
 		//typemsg type=toTypemsg(paramsg[3].split(":")[1]);
		String [] tabdata=paramsg[3].split(":");
		String data="";
		for (int i=1;i<tabdata.length;i++) {
			data+=tabdata[i];
		}
		return new Message(sender,receiver,data,date);
	
	}
	
	







}
