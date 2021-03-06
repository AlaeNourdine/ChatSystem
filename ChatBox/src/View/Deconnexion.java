package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.*;
import Network.*;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;




public class Deconnexion extends JFrame {

	private static final long serialVersionUID = 1L;
	private Controller app;
	private JPanel contentPane;

	
	/**
	 * Constructeur de la page Disconnect 
	 */
	public Deconnexion(Controller app) {
		setBackground(new Color(204, 204, 255));
		this.app=app;
		initialize();
	}
	

	public void initialize() {
		//Frame
		setTitle("Deconnexion");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 421, 221);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Bouton oui
		JButton btnNewButton = new JButton("Oui");
		btnNewButton.setBackground(new Color(204, 204, 255));
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setFont(new Font("Comfortaa", Font.BOLD, 15));
		btnNewButton.setBounds(118, 107, 89, 23);
		contentPane.add(btnNewButton);
		
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.getServerUDP().setOuvert(false);
				TCPReceive.setOuvert(false);
				app.getcSystem().Deconnexion();;
				System.exit(0);
				General.dispose();
				
			}
		});

		
		//Bouton Non
		JButton btnNo = new JButton("Non");
		btnNo.setBackground(new Color(204, 204, 255));
		btnNo.setFont(new Font("Comfortaa", Font.BOLD, 15));
		btnNo.setBounds(217, 107, 89, 23);
		btnNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				
			}
		});
		contentPane.add(btnNo);
		
		JLabel lblNewLabel = new JLabel("Êtes-vous certain de vouloir vous déconnecter?");
		lblNewLabel.setForeground(new Color(204, 204, 255));
		lblNewLabel.setFont(new Font("Comfortaa", Font.BOLD, 15));
		lblNewLabel.setBounds(30, 43, 377, 72);
		contentPane.add(lblNewLabel);
		
	
		
		setVisible(true);
	}
}
