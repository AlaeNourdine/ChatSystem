package Model;

import java.sql.Statement;
import java.util.ArrayList;

import Controller.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe BDD qui represente la base de donnees pour stocker les historiques 
 * Une table de donnee est creer pour chaque conversation 
 */

public class BDD {
		
	private static Controller app;
    
	/* URL de la BDD*/
	private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_020?" ;

    /* Login pour se connecter a la BDD */
    private String login = "tp_servlet_020";

    /* Mdp pour se connecter a la BDD */
    private String mdp = "Or4Xaigh";
    
    
	/**
	 * Constructeur de la classe BDD : 
	 * @param app Controller
	 */
    public BDD(Controller app) {
    	this.setApp(app);
    	createNewDatabase(); 
    	
    }
    
	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public static Controller getApp() {
		return app;
	}
	
	public void setApp(Controller app) {
		BDD.app = app;
	}
    
   /**
	* Methode pour creer la base de donnee
	*/
	public void createNewDatabase() {
	   	try {
			Class.forName("com.mysql.cj.jdbc.Driver") ; 
	   		//Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	   	
	   	try (Connection conn = DriverManager.getConnection(this.DB_URL, this.login, this.mdp)) {
	   		if (conn != null) {
	   			DatabaseMetaData meta = conn.getMetaData();
	   		}
	   	} catch (SQLException e) {	
	   		System.out.println(e.getMessage());
	   	}
	}
	
	
	private Connection connect() {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.DB_URL, this.login, this.mdp);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
 	}	 
	 
	
	/**
	 * Creation de la table de conversation
	 * @param ip2 String
	 */
	public void createTableConvo(String ip2) {
        String sqlconvo= "CREATE TABLE IF NOT EXISTS `" +getNomTable(ip2)+"`(\n"
                + "	time text NOT NULL, \n"
        		+ " message text NOT NULL, \n"
                + " sender integer NOT NULL"
                + ");"; 
        
        try (Connection conn = DriverManager.getConnection(this.DB_URL, this.login, this.mdp);
        		Statement stmt = conn.createStatement()) {
        		stmt.execute(sqlconvo);
        } catch (SQLException e) {
         	System.out.println("erreur at createTableConvo\n");
         	System.out.println(e.getMessage());
        }
	}
	
	/**
     * Methode pour recuperer l'historique
     *
     */
	public ArrayList<Message> recupHistory(String ip2) {
        ArrayList<Message> historique = new ArrayList<Message>();
		String nomtable= getNomTable(ip2);
		String sql = "SELECT time, message, sender FROM `"+nomtable+"`";

	        
	    try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
	         ResultSet rs    = stmt.executeQuery(sql)){
	    	while (rs.next()) {
	    		Message msg= new Message();
	    		msg.setData(rs.getString("message"));
	    		msg.setTimeString(rs.getString("time"));
	    		if (rs.getInt("sender")==0) {
	    			msg.setEmetteur(getApp().getMe());
		    		msg.setDestinataire(getApp().getActifsUsers().getUserfromIP(ip2));
	    		}
	    		else {
	    			msg.setEmetteur(getApp().getActifsUsers().getUserfromIP(ip2));
		    		msg.setDestinataire(getApp().getMe());
	    		}
	    		historique.add(msg);
	         	}
	        } catch (SQLException e) {
	            System.out.println("error at recupHistory\n");
	            System.out.println(e.getMessage());
	        }
		return historique;
	}


	/**
	 * Methode pour ajouter un message a la table
     * @param ip2 String 
     * @param msg String 
     */
	public void addMessage(String ip2, Message msg) {
		String nomtable= getNomTable(ip2);
		String sql = "INSERT INTO `"+nomtable+"`(time,message,sender) VALUES(?,?,?)";


		try (Connection conn =  this.connect() ; PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, msg.getTimeString());
	        pstmt.setString(2, msg.getData());
	        if (msg.getEmetteur().equals(getApp().getMe())) {
	        	pstmt.setInt(3, 0); // 0 -> C'est moi qui envoie le message

	        }else {
	        	pstmt.setInt(3, 1); // 1 -> C'est moi qui recoit le message

	        }

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	System.out.println("error at addMessage\n");
	    	System.out.println(e.getMessage());
	    }
	}
	
	/**
	 * Methode pour recuperr le nom de la table
	 * @param ip2 String
	 * @return String
	 */
	public String getNomTable(String ip2) {
		return "Chatwith_"+ip2;
	}
	

	    

		
}

