package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class User {
	private static InetAddress ipAddress;
	private final String id;
    private static String nickname;
    private Integer port ; 
    
    Integer TAILLE_MAX = 8;
    
 
    
    public User (String nickname, InetAddress ipAddress, Integer port){
        this.setNickname(nickname);
        this.setPort(port);
        this.ipAddress= ipAddress;
        String aux=ipAddress.getHostName() ;
        if (aux.length()> TAILLE_MAX) 
        	aux = aux.substring(0, TAILLE_MAX);
        this.id = aux;
    }
        
    private Integer getPort() {
    	return port;
    }
	private void setPort(Integer port) {
		this.port = port ;
	}

	public String getId() {
		return id ;
	}
	
	public static InetAddress getIpAddress() {
        return ipAddress;
    }

    public static String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    
    public String UserToString(){
        String str = "";
        str+="nickname" + this.nickname + " - ";
        str+="port " + (this.port).toString() + " - ";
        str+="ipAddress " + (this.ipAddress).toString() + " - ";
        return str;
    }
    
    public static User stringToUser(String userstr) {
        String nickname;
        Integer port = 0;
        String ip = "" ;
        String mots[] = userstr.split(" ");
        nickname=mots[1];
        port=Integer.parseInt(mots[4]);
        ip=mots[7];
        User user = null;
        try {
            user = new User(nickname,InetAddress.getByName(ip.split("/")[1]), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return user;
    }

}