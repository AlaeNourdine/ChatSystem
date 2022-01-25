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
		throw new Exception("Cette adresse IP ne correspond à aucun utilisateur.");
		
	}
	
	public synchronized void deleteUser(User user) {
		boolean Sup = false ;
		for(User elem: this.actifUsersList)
		{
			if (elem.equals(user)) {
				this.actifUsersList.remove(elem);
				Sup = true ;
				this.showActifUsers();
				break;
			}
		}
		if (!Sup) {
            System.out.println("Utilisateur n'est pas dans la liste.");
        }
	}
	
    public synchronized void editList(String ancien , String nouveau) {
        for(User elem: this.actifUsersList)
        {
            if (ancien.equals( elem.getNickname() ) ) {
                this.actifUsersList.remove(elem);
                elem.setNickname(nouveau);
                this.ajouterUser(elem);
                break ;
            }
        }
        this.showActifUsers();
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
	
	
    public void showActifUsers() {
        System.out.println ("Utilisateurs actifs : ");
        StringBuilder User = new StringBuilder();
        for(User elem: this.actifUsersList)
        {
            System.out.println (elem.toString());
            User.append(elem).append("\n");
        }
    }
        public Boolean checkNicknameUnicity(String nickname) {
            for(User elem: this.actifUsersList)
            {
                if (nickname.equals( elem.getNickname() ) ) {
                    return false ;
                }
            }
            return true;
        }
}
