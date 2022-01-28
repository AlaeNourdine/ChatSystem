/*package View;

import java.net.URL;

import Controller.Controller;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import Model.Messages;


/** 
 * Classe representant la page principale. 
 */

/*
public class View extends JPanel implements ActionListener {
	
	
	private static final long serialVersionUID = 7128287592232261943L;
	private static String nickname ;
	private ObjectOutputStream out ;
	private String ipAddress ;
	
	private JLabel lblNewLabel_1 ;
	private JPanel viewPanel ;
	private static JFrame viewFrame ; 
	
	public View()  {
		
		
		super ("ChatApp");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		
		//this.out = out ;
		
		//viewFrame = new JFrame ("View Window");
		//viewPanel = new JPanel(new GridLayout (10,10)) ;

		
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
	
		btnConnexion.addActionListener(this);
		contentPane.getRootPane().setDefaultButton(btnConnexion);
		
		
}	
	
	public void actionPerformed(ActionEvent event) {
		nickname = lblNewLabel_1.getText();
		System.out.println("Login Username : " + nickname);
	}
	
	public static void main (String[] args) {
		View view = new View() ;
		view.setVisible(true);
	}
	
}
*/

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

import Model.Messages;


/** 
 * Classe representant la page principale. 
 */
public class View {
	
	
	private static final long serialVersionUID = 7128287592232261943L;
	private static String nickname ;
	private ObjectOutputStream out ;
	private String ipAddress ;
	private JLabel lblNewLabel_1 ;
	private JPanel viewPanel ;
	private static JFrame viewFrame ; 
	
	public View(String username) throws IOException  {
		View.nickname = username ;

		viewFrame = new JFrame(nickname) ;
		viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewFrame.setSize(600, 400);
		viewFrame.setLocationRelativeTo(null);
		
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(356, 82, 160, 30);
		viewFrame.add(btnConnexion);
	
		btnConnexion.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
					//	ControllerChat.connexion(nickname);
						viewFrame.setVisible(false);
						new Clavardage(nickname);
						
					}
					
				}
				);
		viewFrame.getRootPane().setDefaultButton(btnConnexion);
		
		JTextField pseudo = new JTextField();
		pseudo.setText("Pseudo");
		pseudo.setColumns(10);
		pseudo.setBounds(188, 82, 160, 30);
		viewFrame.add(pseudo);
		
		JLabel lblNewLabel = new JLabel("Bienvenue !");
		lblNewLabel.setBounds(225, 20, 160, 50);
		viewFrame.add(lblNewLabel);
		
		
		JLabel lblNewLabel_1 = new JLabel("Choisis ton pseudo");
		lblNewLabel_1.setBounds(200, 40, 160, 50);
		viewFrame.add(lblNewLabel_1);
		viewFrame.setVisible(true);
		
		
	}
	
		
		//this.out = out ;
		
		//viewFrame = new JFrame ("View Window");
		//viewPanel = new JPanel(new GridLayout (10,10)) ;



		
		


	public void actionPerformed(ActionEvent event) {
		nickname = lblNewLabel_1.getText();
		System.out.println("Login Username : " + nickname);
	}
	
	public static void main (String[] args) throws IOException {
		new View(nickname) ;
		
	}
	
}
