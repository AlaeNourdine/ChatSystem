package Controller;

import Model.ActifUsers;
import Model.User;
import Network.TCPReceive ;
import Network.Discussion;
import Network.broadcastUDP;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;


public class ControllerChat implements PropertyChangeListener{
	
	private ActifUsers actifs ;
	private static User me ;
	private static ControllerChat chatsystem ;
	private static boolean online ;
	//private  TCPReceive listener;
	public ArrayList<InetAddress> listCommunication = new ArrayList<>();
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	
	public static void main(String[] args) {
		
	}
	
	public boolean isOnline() {
		return online ;
	}

    public ControllerChat(String nickname, Integer port){
        this.actifs = new ActifUsers() ;
        InetAddress ipAddress = broadcastUDP.getCurrentIp(); 
        this.me = new User(nickname, ipAddress, port);
        this.actifs.ajouterUser(getMe());

        this.online=false;

        String path = System.getProperty("user.dir")+ FILE_SEPARATOR + "config.json";
        System.out.println("Path is : "+path);
    }
    
    public static synchronized ControllerChat getInstance(String nickname, Integer port){
        if (chatsystem == null) {
            chatsystem = new ControllerChat(nickname, port);
        }
    return chatsystem;
    }
    public static ControllerChat getInstance(){
        return chatsystem;
    }
    
    public boolean editNickname(String nouveau) throws IOException{
        String broadcastMessage = "Demande Modification Pseudo\n" + getMe().toString() + "\n" + nouveau + "\n";
        broadcastUDP.envoiBroadcast(broadcastMessage);
        try {
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (broadcastUDP.getvalidNickname()) {
            System.out.println("Modification pseudo reussi");
            broadcastMessage = "Modification pseudo reussi\n" + getMe().toString() + "\n" + nouveau + "\n";
            broadcastUDP.envoiBroadcast(broadcastMessage);
            this.getActifUsers().editList(getMe().getNickname(), nouveau);
            getMe().setNickname(nouveau);
            System.out.println("Changement pseudo accepte, nouvelle liste des utilisateurs actifs:");
            this.getActifUsers().showActifUsers();
            return true;
        }
        else
        {
            System.out.println("Echec Modification pseudo");
            broadcastUDP.setvalidNickname(true);
            return false;
        }
    }
    
    public static boolean connexion(String nickname) throws IOException{
    	me.setNickname(nickname);
        String broadcastMessage = "Connexion\n" + getMe().toString() ;
        broadcastUDP.envoiBroadcast(broadcastMessage);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (broadcastUDP.getConnexion()) {
            System.out.println("Connexion reussie");
            online=true;
            return true;
        }
        else
        {
            System.out.println("Connexion echoue");
            broadcastUDP.setConnexion(true);
            return false ;
        }
    }

  
    public void demarrerSession(String nickname){
        User user = this.actifs.getUserByNickname(nickname);
        Discussion session = new Discussion(user,this);
        //listener.addSession(session);
    }
    

    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case "chatCreated" :
                //Discussion session = this.listener.getDiscussion();
                //session.addPropertyChangeListener(this);
                break;
        }
    }
    
    public void activerEcouteTCP(){
        //this.listener = new listener(this);
        //this.listener.addPropertyChangeListener(this);
    }

	public static User getMe() {
		return me;
	}
	
    public ActifUsers getActifUsers() {
        return actifs;
    }

}