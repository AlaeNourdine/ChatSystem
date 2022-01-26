package Controller;

import Model.ActifUsers;
import Model.User;
import Network.* ;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;


public class ControllerChat {
	
	private ActifUsers actifs ;
	private User me ;
	private static ControllerChat chatsystem ;
	private boolean online ;
	//private RunnerEcouteTCP runnerEcouteTCP;
	public ArrayList<InetAddress> listCommunication = new ArrayList<>();
	private JSONObject configJSON;
	public static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	
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
        JSONParser jsonP = new JSONParser();
        try {
            this.configJSON = (JSONObject) jsonP.parse(new FileReader(path));
        } catch (IOException e) {
            System.out.println("Fichier JSON non trouvable");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Fichier JSON mal formé");
            e.printStackTrace();
        }
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
    
	public User getMe() {
		return me;
	}
}