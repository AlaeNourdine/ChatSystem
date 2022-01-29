package Model;

import java.io.Serializable;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Messages implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User destinataire ;
	private String msg;
    private Date horodatage;
    private User emetteur;
    private Type type ;

    public Messages ( String msg, User emetteur,Type type) {
        this.setEmetteur(emetteur) ;
        this.setMsg(msg) ;
        this.setHorodatation(new Date());
        this.type = type;
    }

    public void setDate(Date date) {
        this.setHorodatation(date);
    }

    
	public static enum Type {
		BROADCAST_NICKNAME,
		CONNEXION,
		DECONNEXION,
		GET_ALL_USERS,
		USERNAME_EDIT, 
		MESSAGE
	}
    
	public static String msgForme(String msg,String username, Type type) {
		return (type + "/-/" + username+ "/-/" + msg);
	}
	
    public String msgtoString() {
        String msg = "";
        msg += ("Destinataire :" + this.getDestinataire() + "\n") ;
        msg += ("Emetteur :" + this.getEmetteur()+ "\n") ;
        msg += ("Type :"+ this.type+ "\n");
        msg += ("Date :" + this.horodateToString() + "\n") ;
        msg += ("Message :" + this.getMsg() + "\n" );
        return msg ;
    }
    
    public static Messages stringToMessageHorodated(String s) {
        String[] mots = s.split("\n");
        User destinataire = User.stringToUser(mots[0].split(" :")[1]);
        User emetteur = User.stringToUser(mots[1].split(" :")[1]);
        int type = Integer.parseInt(mots[2].split(":")[1]);
        String contenu = "";
        for(int i=4; i< mots.length; i++) {
            if(mots[i].startsWith("Message :")) {
                mots[i]=mots[i].split(" :")[1];
            }
            contenu += mots[i]+"\n";
        }
        return new Messages(contenu, emetteur ,  null);
    }
    
    public String horodateToString() {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(this.getHorodatation());
    }
    
    
	private void setHorodatation(Date horodatage) {
		this.horodatage = horodatage ;
	}

    public Date getHorodatation()
    {
        return this.horodatage;

    }

    
	private void setMsg(String msg) {
		this.msg = msg;
		
	}
	
	public String getMsg()
    {
        return this.msg;

    }
	
	
	private void setEmetteur(User emetteur) {
		this.emetteur = emetteur;
		
	}

    public User getEmetteur() {
        return emetteur;
    }
    
    private User getDestinataire() {
    	return this.destinataire;
    }
	private void setDestinataire(User destinataire) {
		this.destinataire = destinataire ;
		
	}
	
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }

}
