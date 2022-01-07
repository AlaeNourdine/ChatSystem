package Model;

import java.net.InetAddress;

public class User {
	private final InetAddress ipAddress;
    private String nickname;
    private Integer port ; 
    
    //Integer TAILLE_MAX = 10; 
    
    public User (String nickname, InetAddress ipAddress, Integer port){

        this.setNickname(nickname);
        this.setPort(port);
        this.ipAddress= ipAddress;
        
    }
    
    private Integer getPort() {
    	return port;
    }
	private void setPort(Integer port) {
		this.port = port ;
	}


	public InetAddress getIpAddress() {
        return ipAddress;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

   
}