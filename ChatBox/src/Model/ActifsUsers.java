package Model;

import java.util.ArrayList;



/**
 * Classe ActifsUsers qui represente la liste des utilisateurs en ligne
 * Cette classe herite de ArayList<User>
 */

public class ActifsUsers extends ArrayList<User>{
	
	private static final long serialVersionUID = 1L;
	static ArrayList<User> users = new ArrayList<User>();
	
	/**
	 * Constructeur de ActifsUsers
	 */
	public ActifsUsers() {
		super();
	}
	
	/**
	 * Methode pour afficher tous les utilisateurs en ligne
	 */
	public static void showActifUsers() {
		for (User user : users) {
		}
	}
	
	/**
	 * Methode pour ajouter un utilisateur en ligne
	 * @param e User
	 */
	public void addConnectedUser (User e) {
		users.add(e);
	}
	
	/**
	 * Methode pour supprimer un utilisateur
	 * @param e User
	 */ 
	public void deleteUser (User e) {
		users.remove(e);
	}
	
	/**
	 * Methode pour compter le nombre d'utilisateurs en ligne pour rendre sa taille afin de l'utiliser apres
	 * @return n int : taille de la listes
	 */
	public int length () {
		int n=0;
		for (User user : users) {
			n++;
		}
		return n;
	}
	
	/**
	 * Methode pour creer un tableau des utilisateurs en ligne
	 * C'est un tableau de string
	 * @return String [] tab : tableau des utilsiateurs en ligne (Tableau de String)
	 */
	public String[] getListPseudo() {
		if (!(users.isEmpty())) {
			String[] tab= new String[length()];
			int i=0;
			for (User user : users) {
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
	
	/**
	 * Methode pour recuperer un type User d'apres son pseudp
	 * @param pseudo Pseudo 
	 * @return User user
	 */
	public User getUserfromPseudo (String pseudo) {
		User toreturn = null;
		for (User user : users) {
			if (user.getNickname().equals(pseudo)) {
				toreturn=user;
				return toreturn;
			}
		}
		return null;
	}
	
	/**
	 * Methode pour recuperer un type User d'apres son adresse IP
	 * @param ip Ip de l'user � r�cup�rer
	 * @return user avec l'ip correspondante
	 */
	public User getUserfromIP (String ip) {
		User toreturn = null;
		for (User user : users) {
			if (user.getIP().equals(ip)) {
				toreturn=user;
				return toreturn;
			}
		}
		return null;
	}
	
	/**
	 * Methode pour recuperer le champ Pseudp d'un type User d'apres son pseudo
	 * @param IP Ip 
	 * @return String pseudo
	 */
	public String getPseudofromIP (String IP) {
		String toreturn = null;
		for (User user : users) {
			if (user.getIP().equals(IP)) {
				toreturn=user.getNickname();
				return toreturn;
			}
		}
		return null;
	}

	/**
	 * Methode pour savoir si un pseudo appartient bien a un utilisateur
	 * @param pseudo
	 * @return boolean
	 */
	public boolean appartient (String pseudo) {
		for (User user : users) {
			if (user.getNickname().equals(pseudo)) {
				return true;
			}
		}
		return false;
	}
	

}
