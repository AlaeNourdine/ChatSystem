package Model;

import java.sql.Statement;
import java.util.ArrayList;

import Controller.Controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe representant la base de donnees stockant l'historique des conversations 
 */

public class BDD {
	
	private static Controller app;
	
	private String DB_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_020?" ;

    private Connection connection = null;

    private String login = "tp_servlet_020";

    private String mdp = "Or4Xaigh";
    
	//Constructor
    public BDD(Controller app) {
    	this.setApp(app);
    	createNewDatabase(); 
    	
    }
    
    Statement statement;
    
  
	public void createNewDatabase() {
	   	try {
	   		//Class.forName("org.sqlite.JDBC");
	   		Class.forName("com.mysql.cj.jdbc.Driver") ; 
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	   	
	   	try (Connection conn = DriverManager.getConnection(this.DB_URL, this.login, this.mdp)) {
	   		if (conn != null) {
	   			DatabaseMetaData meta = conn.getMetaData();
	   			//System.out.println("The driver name is " + meta.getDriverName());
	   			//System.out.println("A new database has been created.");
	   		}

	   	} catch (SQLException e) {	
	   		System.out.println(e.getMessage());
	   	}
	}
	
	/**
	 * Connection to database	
	 */
	private Connection connect() {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.DB_URL, this.login, this.mdp);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
 	}	 
	 
	 
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
	

	public ArrayList<Messages> recupHistory(String ip2) {
        ArrayList<Messages> historique = new ArrayList<Messages>();
		String nomtable= getNomTable(ip2);
//		String sql = "SELECT id, time, message, sender FROM `"+nomtable+"`";
		String sql = "SELECT time, message, sender FROM `"+nomtable+"`";

	    try (Connection conn = this.connect();
			 Statement stmt  = conn.createStatement();
	         ResultSet rs    = stmt.executeQuery(sql)){

	    	// loop through the result set
	    	while (rs.next()) {
	    		Messages msg= new Messages();
	    		msg.setData(rs.getString("message"));
	    		msg.setTimeString(rs.getString("time"));
	    		if (rs.getInt("sender")==0) {
	    			msg.setEmetteur(getApp().getMe());
		    		msg.setDestinataire(getApp().getActifUsers().getUserfromIP(ip2));
	    		}
	    		else {
	    			msg.setEmetteur(getApp().getActifUsers().getUserfromIP(ip2));
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



	public void addMessage(String ip2, Messages msg) {
		String nomtable= getNomTable(ip2);
		String sql = "INSERT INTO `"+nomtable+"`(time,message,sender) VALUES(?,?,?)";


		try (Connection conn =  this.connect() ; PreparedStatement pstmt = conn.prepareStatement(sql)) {
//			pstmt.setString(1, "");
			pstmt.setString(1, msg.getTimeString());
	        pstmt.setString(2, msg.getData());
	        // 0 -> j'ai envoye le message
	        if (msg.getEmetteur().equals(getApp().getMe())) {
	        	pstmt.setInt(3, 0);
	        }
	        // 1 -> j'ai recu le message
	        else {
	        	pstmt.setInt(3, 1);

	        }
	    	//System.out.println("on ajoute le msg");

	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	System.out.println("error at addMessage\n");
	    	System.out.println(e.getMessage());
	    }
	}

	public String getNomTable(String ip2) {
		return "Chatwith_"+ip2;
	}
	
	

	    
	//-------------------- GETTEURS & SETTEURS -----------------------------//

	public static Controller getApp() {
		return app;
	}
	
	public void setApp(Controller app) {
		BDD.app = app;
	}
		
}

