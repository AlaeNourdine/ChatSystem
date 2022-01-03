package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String ipAddress;
    private String nickname;

    private HashMap<String,ArrayList<Messages>> history;

    public User (String nickname, String ipAddress){

        this.nickname = nickname;
        this.setHistory(new HashMap<String,ArrayList<Messages>>());

        this.ipAddress = ipAddress;
}

    public String getIpAddress() {
        return ipAddress;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

	public HashMap<String,ArrayList<Messages>> getHistory() {
		return history;
	}

	public void setHistory(HashMap<String,ArrayList<Messages>> hashMap) {
		this.history = hashMap;
	}
}