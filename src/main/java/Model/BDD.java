package Model;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet ; 
import java.sql.DriverManager;
import java.util.ArrayList;

import Controller.ControllerChat;

public class BDD {
	
    /* URL pour acceder à la BDD */
	//C'est une concatenation des éléments suivants : 
	//jdbc:mysql : qui indique le protocole utilisé pour accéder à la BDD
	//localhost::3306 : dans notre cas srv-bdens.insa-toulouse.fr:3306 , indique le nom de l'hôte qui héberge la bdd, ainsi que le port d'accès
	//nom_de_la_bdd : dans notre cas tp_servlet_020 
	private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_020?" ;

    /* Connection avec la BDD */
    private Connection connection = null;

    /* Login pour se connecter à la BDD */
    private String login = "tp_servlet_020";

    /* Mdp pour se connecter à la BDD */
    private String mdp = "Or4Xaigh";

    private ControllerChat chatapp;

    /* Singleton */
    private static final BDD instance = null;

	
	Statement statement ;
	
	// Constructor 
	public BDD() {		
		// Load the driver class file 
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver") ; 
			Class.forName("com.mysql.cj.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Echec installation Driver");
		}
		
		try {
			//Etablir une connexion , forme : (url, "myLogin", "myPassword");            
        	System.out.println(this.DB_URL);
            System.out.println(this.mdp);
            System.out.println(this.login);
            this.connection = DriverManager.getConnection(this.DB_URL,this.login,this.mdp); //Ouverture de la connexion
            System.out.println("Connexion Etablie");
            
			// Create a statement object
			this.statement = this.connection.createStatement() ; 
			
			
			// Execute the statement 			
			String query = "CREATE TABLE IF NOT EXISTS UsernameToIP " +
			           "(username VARCHAR(255) not NULL, " +
			           " ip VARCHAR(255) not NULL PRIMARY KEY," +
			           " isConnected BOOLEAN not NULL CHECK (isConnected IN (0,1))," +
			           " lastAccess VARCHAR(255) not NULL) ;" ;  

			this.statement.executeUpdate(query) ;
		
		
		} catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Echec d'etablissement de la connexion");
		}
	}
	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                       Add or update a user in the database                                   //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void addUser(String username, InetAddress IP) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		
		String query = "REPLACE INTO UsernameToIP(Username, IP, isConnected, lastAccess) VALUES ('" + username + "', '" + IPString + "', 1, '') ;" ;
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                     Setters                                                  //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Function to set the connected state -> disconnected 
	public void userDisconnected(String username) {
		
		String query = "UPDATE UsernameToIP SET isConnected = 0 WHERE Username = '" + username + "' ;" ; 
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	// Function to update the username 
	public void updateUsername(InetAddress IP, String newUsername) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		String query = "UPDATE UsernameToIP SET Username = '" + newUsername + "' WHERE IP = '" + IPString + "' ;" ; 
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	// Function to update the last access date 
	public void updateLastAccess(InetAddress IP, String newDate) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		String query = "UPDATE UsernameToIP SET lastAccess = '" + newDate + "' WHERE IP = '" + IPString + "' ;" ; 
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                     Getters                                                  //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Get the username according to the IP address
	public String getUsername(InetAddress IP) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		String query = "SELECT UsernameToIP.Username FROM UsernameToIP WHERE IP = '" + IPString + "' ;";	
		String username = "" ; 
		try {
			// Execute the statement 
			ResultSet rs = this.statement.executeQuery(query) ; 
			if (rs.next()) {
				 username = rs.getString(1);
			}
			rs.close(); 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}

		return username ;	
	}
	
	// Get the IP address according to the username
	public InetAddress getIP(String username) {
		String query = "SELECT UsernameToIP.IP FROM UsernameToIP WHERE Username = '" + username + "' ;";	
		String IPString = "" ; 
		InetAddress IP = null ; 
		try {
			// Execute the statement 
			ResultSet rs = this.statement.executeQuery(query) ; 
			
			if (rs.next()) {
				 IPString = rs.getString(1) ; 
				 IP = InetAddress.getByName(IPString) ; 
			}
			rs.close(); 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return IP ; 
	}
	
	// Get the last access according to the IP  
	public String getLastAccess(InetAddress IP) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		String query = "SELECT UsernameToIP.lastAccess FROM UsernameToIP WHERE IP = '" + IPString + "' ;";	
		String lastAccess = "" ; 
		try {
			// Execute the statement 
			ResultSet rs = this.statement.executeQuery(query) ; 
			if (rs.next()) {
				 lastAccess = rs.getString(1);
			}
			rs.close(); 
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return lastAccess ;	
	}	
	
	
	// Get all usernames of the database 
	public ArrayList<String> getAllUsernames() {
		String query = "SELECT UsernameToIP.Username FROM UsernameToIP ;";	
		ArrayList<String> results = new ArrayList<String>() ; 
		
		try {
			// Execute the statement 
			ResultSet rs = this.statement.executeQuery(query) ;
			
			while(rs.next()) {
				results.add(rs.getString(1));
			}
			rs.close(); 
		} 
		catch ( Exception e) {
			System.out.println(e);
		}
		
		return results ; 
	}
	
	
	// Get all connected usernames of the database 
		public ArrayList<String> getConnectedUsernames() {String query = "SELECT UsernameToIP.Username FROM UsernameToIP WHERE isConnected=1;";	
			ArrayList<String> results = new ArrayList<String>() ; 
			
			try {
				// Execute the statement 
				ResultSet rs = this.statement.executeQuery(query) ;
				
				while(rs.next()) {
					results.add(rs.getString(1));
				}
				rs.close(); 
			} 
			catch ( Exception e) {
				System.out.println(e);
			}
			
			return results ; 
		}
	
		
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                Delete an element                                             //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	// Delete a user to the database according to the username 
	public void deleteUserByName(String username) {
		String query = "DELETE FROM UsernameToIP WHERE Username='" + username + "' ;" ;	
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	// Delete a user to the database according to the IP address 
	public void deleteUserByIP(InetAddress IP) {
		String IPString = IP.toString();
		if (IPString.charAt(0) == ('/')) {
			IPString = IPString.substring(1);
		}
		String query = "DELETE FROM UsernameToIP WHERE IP='" + IPString + "' ;" ;		
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ; 
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                 Drop the database                                            //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void dropDatabase() {
		String query = "DROP TABLE UsernameToIP ;" ;
		
		try {
			// Execute the statement 
			this.statement.executeUpdate(query) ;
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                         Close the connection of database                                     //
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void closeConnection() {
		try {
			this.connection.close() ;
		} 
		catch (SQLException e) {
			System.out.println(e) ; 
		}
		
	}	
	

	public static void main(String[] args) {
		
	}
		

}