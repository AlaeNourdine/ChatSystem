package View;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.util.ArrayList;

import Model.*;
import Network.* ;

public class ChangeNickname {
	private JFrame changeFrame ;
	
	private JPanel header = new JPanel (new FlowLayout(FlowLayout.RIGHT));
	private JPanel changePanel = new JPanel (new FlowLayout(FlowLayout.CENTER));

	private JButton btnDisconect = new JButton ("Déconnexion");
	private JButton btnAnnuler = new JButton ("Annuler");
	private JButton btnChange = new JButton ("Confirmer");
	
	public static String username ;
	
	public ChangeNickname (String nickname) throws IOException {
		username = nickname ;
		
		changeFrame = new JFrame ("Changer pseudo");
		changeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeFrame.setSize(1000, 600);
		changeFrame.setLocationRelativeTo(null);
		
		changeFrame.setVisible(true);
		
		btnDisconect.setBounds(356, 82, 160, 30);
		header.add(btnDisconect);
		btnDisconect.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						changeFrame.setVisible(false);
						try {
							View view = new View(username);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				);
		changeFrame.getRootPane().setDefaultButton(btnDisconect);
		
		
		btnAnnuler.setBounds(356, 100, 160, 30);
		changePanel.add(btnAnnuler);
		btnAnnuler.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {						changeFrame.setVisible(false);
						try {
							General general = new General (nickname);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				);
		changeFrame.getRootPane().setDefaultButton(btnAnnuler);
	
		btnChange.setBounds(356, 100, 160, 30);
		changePanel.add(btnChange);
		btnChange.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						changeFrame.setVisible(false);
						try {
							Exit exit = new Exit(nickname) ;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				}
				);
		changeFrame.getRootPane().setDefaultButton(btnChange);
		
		changeFrame.add(header, BorderLayout.EAST);
		changeFrame.add(changePanel, BorderLayout.WEST);
	}
	
	public static void main (String[] args) throws IOException{
		new ChangeNickname(username)	;
	}
}