package View;

import java.net.URL;

import Controller.Controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.*;

import Model.*;
import Controller.*;

/** 
 * Classe representant la page principale. 
 */
public class View {
	
	
	private static final long serialVersionUID = 7128287592232261943L;
	private static String username ;
	private ObjectOutputStream out ;
	private String ipAddress ;
	private JLabel lblNewLabel_1 ;
	private JPanel viewPanel ;
	private static JFrame viewFrame ; 
	private JLabel msg;
	
	private final JTextField pseudo = new JTextField();
	
	public View(final String nickname)  {
		View.username = nickname ;

		viewFrame = new JFrame(nickname) ;
		viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewFrame.setSize(600, 400);
		viewFrame.setLocationRelativeTo(null);
		
		JLabel lblNewLabel = new JLabel("Bienvenue !");
		lblNewLabel.setBounds(225, 20, 160, 50);
		viewFrame.add(lblNewLabel);
		
		
		JLabel lblNewLabel_1 = new JLabel("Choisis ton pseudo");
		lblNewLabel_1.setBounds(200, 40, 160, 30);
		viewFrame.add(lblNewLabel_1);
		viewFrame.setVisible(true);
		
		final JTextField pseudo = new JTextField();
		pseudo.setColumns(10);
		pseudo.setBounds(188, 100, 160, 30);
		viewFrame.add(pseudo);
			
	
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(356, 100, 160, 30);
		viewFrame.add(btnConnexion);
	
		btnConnexion.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						String nom = pseudo.getText(); //On r�cup�re le pseudo choisi par l'utilisateur
						boolean used = Controller.validUsername(nom);
							if (! used) {
								msg= new JLabel ("Pseudo invalide. Veuillez en choisir un autre.");
								JOptionPane.showMessageDialog(viewFrame, msg);
							}else {
									Controller.connection(nom);
									viewFrame.setVisible(false);
									
							}
					}
				}
				
				);
		viewFrame.getRootPane().setDefaultButton(btnConnexion);
		


		
	}


	public void actionPerformed(ActionEvent event) {
		username = lblNewLabel_1.getText();
		System.out.println("Login Username : " + username);
	}
	
	public static void main (String[] args) {
		new View(username) ;
		
	}
	
}
