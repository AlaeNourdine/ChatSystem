package View;

import java.net.URL;

import Controller.Controller;
import java.awt.*;
import javax.swing.*;


/** 
 * Classe representant la page principale. 
 */
public class View extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7128287592232261943L;

	public View() {
		super ("ChatApp");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Bienvenue !");
		lblNewLabel.setBounds(225, 20, 160, 50);
		contentPane.add(lblNewLabel);
		
		
		JLabel lblNewLabel_1 = new JLabel("Choisis ton pseudo");
		lblNewLabel_1.setBounds(200, 40, 160, 50);
		contentPane.add(lblNewLabel_1);
		
		
		
		
		JTextField pseudo = new JTextField();
		pseudo.setText("Pseudo");
		pseudo.setColumns(10);
		pseudo.setBounds(188, 82, 160, 30);
		contentPane.add(pseudo);
		
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(356, 82, 160, 30);
		contentPane.add(btnConnexion);
		
	}

	public static void main(String[] args) {
		
		
		//Start View
		View view = new View() ;
		view.setVisible(true);
		
	}
}
