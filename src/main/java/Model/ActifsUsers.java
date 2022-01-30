package Model;

import java.util.ArrayList;


/**
 * Class  héritant de ArrayList<User> représentant la liste des utilisateurs connectés 
 */

public class ActifsUsers extends ArrayList<User>{
	
	private static final long serialVersionUID = 1L;
	static ArrayList<User> actifsUsersList = new ArrayList<User>();
	
	//Constructor
	public ActifsUsers() {
		super();
	}
	
	
	public static void showActifsUsers() {
		//System.out.println("Actifs Users list:");
		for (User user : actifsUsersList) {
			//System.out.println(user.getPseudo());
		}
	}
	

	public void addConnectedUser(User e) {
		actifsUsersList.add(e);
	}
	
	
	public void deleteUser (User e) {
		actifsUsersList.remove(e);
	}
	
	/**
	 * Compte le nombre de users dans la liste pour de trouver sa taille
	 */
	public int length () {
		int n=0;
		for (User user : actifsUsersList) {
			n++;
		}
		return n;
	}
	
	/**
	 * Création d'un tableau de string correspondant aux users de notre liste * @return tableau des pseudos des users
	 */
	public String[] getListPseudo() {
		if (!(actifsUsersList.isEmpty())) {
			String[] tab= new String[length()];
			int i=0;
			for (User user : actifsUsersList) {
				tab[i]=user.getNickname();
				i++;
			}
			return tab;
		}
		else {
			String[] tab= {};
			return tab;
		}
	}
	

	public User getUserfromPseudo (String pseudo) {
		User toreturn = null;
		for (User user : actifsUsersList) {
			if (user.getNickname().equals(pseudo)) {
				toreturn=user;
				return toreturn;
			}
		}
		return null;
	}
	

	public User getUserfromIP (String ip) {
		User toreturn = null;
		for (User user : actifsUsersList) {
			if (user.getIP().equals(ip)) {
				toreturn=user;
				return toreturn;
			}
		}
		return null;
	}
	

	public String getPseudofromIP (String IP) {
		String toreturn = null;
		for (User user : actifsUsersList) {
			if (user.getIP().equals(IP)) {
				toreturn=user.getNickname();
				return toreturn;
			}
		}
		return null;
	}

	
	public boolean appartient (String pseudo) {
		for (User user : actifsUsersList) {
			if (user.getNickname().equals(pseudo)) {
				return true;
			}
		}
		return false;
	}
	

}
