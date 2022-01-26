package Model;

//import Controller.Controller;
import java.sql.*;

public class DataBase {
	private String BDD_URL = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_020";
	private String login = "tp_servlet_020";
	private String password = "Or4Xaigh";
	
//	private Controller controller;
	
	public DataBase() {
		//Open a connection
		try (Connection conn = DriverManager.getConnection(BDD_URL, login, password);
			Statement stmt = conn.createStatement();	
			) {
			String sql = "CREATE DATABASE USERS";
			stmt.execute(sql);
			System.out.println("Database created successfully...");
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	/*Create a table*/


}
