package Model;

import java.io.Serializable;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Classe pour toutes les echanges tcp 
 
 */

public class Messages implements Serializable{

	private static final long serialVersionUID = 1L;
	private User destinataire ;
	private String data;
    private String time;
    private User emetteur;

    //Differents constructeurs avec different nombre d'arguments
    
	public Messages() {
	}

	
	public Messages(User from, User to, String msg) {
		this.setEmetteur(from);
		this.setDestinataire(to);
		this.setData(msg);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.setTime(dateFormat.format(new Date()));
	}
	
	public Messages(User from, User to, String msg, String date) {
		this.setEmetteur(from);
		this.setDestinataire(to);
		this.setData(msg);
		this.setTimeString(date);
	}
	
	public Messages(String msg) {
		this.setData(msg);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.setTime(dateFormat.format(new Date()));

	}


	public String toString() {
		String smsg= "Sender: "+this.getEmetteur()+"\n"+"Receiver:  "+this.getDestinataire()+"\n"
	+"Time:  "+ this.getTime()+"\n"+ "Data:  "+this.getData()+"\n";
		return smsg;	
	}
	

	public static Messages toMessage(String smsg) {
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
		return new Messages(sender,receiver,data,date);
	
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

	public void setEmetteur(User sender) {
		this.emetteur = sender;
	}

	public User getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(User receiver) {
		this.destinataire = receiver;
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



}
