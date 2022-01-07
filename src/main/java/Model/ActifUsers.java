package Model;

import java.net.InetAddress;
import java.util.ArrayList;

public class ActifUsers {
	
	private ArrayList<User> actifUsersList;
	
	public ActifUsers() {
		this.actifUsersList = new ArrayList<>();
	}


	public synchronized void ajouterUser(User user) {
		if (! this.appartient(user)){
			this.actifUsersList.add(user);
		}
	}
	
	public User getUserByNickname(String nickname) {
		for(User elem: this.actifUsersList)
		{
			if (elem.getNickname().equals(nickname)) {
				return elem ;
			}
		}
		return null;
	}
	
	public User getUserByIP(InetAddress ipAddress) throws Exception {
		for(User elem: this.actifUsersList)
		{ 
			if (elem.getIpAddress().equals(ipAddress)) {
				return elem ; 
			}
		}
		throw new Exception("Cette adresse IP ne correspond à aucun utilisateurs.");
		
	}
	
	
	public Boolean appartient(User user) {
        boolean appartient = false ;
        for(User elem: this.actifUsersList)
        {
            if (elem.getNickname().equals(user.getNickname())) {
                appartient = true;
                break;
            }
        }
        System.out.println("appartient ?: " + appartient);
        return appartient;
    }
}