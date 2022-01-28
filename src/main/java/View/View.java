package View;

import java.net.URL;

import Controller.Controller;
import Controller.ControllerChat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;

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
	
	public View(String nickname) throws IOException  {
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
		
		JTextField pseudo = new JTextField();
		pseudo.setText("Pseudo");
		pseudo.setColumns(10);
		pseudo.setBounds(188, 100, 160, 30);
		viewFrame.add(pseudo);
			
	
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(356, 100, 160, 30);
		viewFrame.add(btnConnexion);
	
		btnConnexion.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						String nom = pseudo.getText(); //On récupère le pseudo choisi par l'utilisateur
						boolean used;
						try {
							used = Manager.validUsername(nom);
							if (! used) {
								msg= new JLabel ("Pseudo invalide. Veuillez en choisir un autre.");
								JOptionPane.showMessageDialog(viewFrame, msg);
							}else {
								viewFrame.setVisible(false);
								General general = new General (nickname);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
	
	public static void main (String[] args) throws IOException {
		new View(username) ;
		
	}
	
}
