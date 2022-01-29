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

public class General {
	private JFrame generalFrame ;
	
	private JPanel actifUsers = new JPanel (new FlowLayout(FlowLayout.RIGHT)) ;
	private JPanel broadcast = new JPanel (new FlowLayout(FlowLayout.LEFT)) ;
	private JPanel header= new JPanel (new FlowLayout(FlowLayout.CENTER)) ;
	
	private JButton btnDisconect = new JButton ("D�connexion");
	private JButton btnChangeNickname = new JButton ("Changer pseudo"); 
	private JButton btnOnline = new JButton ("Utilisateurs en ligne");
	
	JScrollPane scrollPane ;
	
	protected ArrayList<String> usersAll = new ArrayList <String>();
	public int usersNbre ;
	protected Object tableUsers [][]=new Object[usersNbre][1];
	
	final Object label[]= {"Utilisateur en ligne"};
	final JTable table ;
	
	public static String username;
	
	//Constructeur
	public General (String nickname) {
		username = nickname;
		
		generalFrame = new JFrame ("General");
		generalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		generalFrame.setSize(1000, 600);
		generalFrame.setLocationRelativeTo(null);
		
		generalFrame.setVisible(true);
		
		btnChangeNickname.setBounds(356, 82, 160, 30);
		header.add(btnChangeNickname);
		btnChangeNickname.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						generalFrame.setVisible(false);
						try {
							ChangeNickname changeNickname = new ChangeNickname (nickname) ;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				);
		generalFrame.getRootPane().setDefaultButton(btnChangeNickname);
		
		

		btnDisconect.setBounds(356, 82, 160, 30);
		header.add(btnDisconect);
		btnDisconect.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						generalFrame.setVisible(false);
							View view = new View(username);
						
					}
					
				}
				);
		generalFrame.getRootPane().setDefaultButton(btnDisconect);
		
		
		
		btnOnline.setBounds(356, 82, 160, 30);
		actifUsers.add(btnOnline);
		btnOnline.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					
					}
				}
				);
		
		
		
		
		for (int i=0; i<usersNbre; i++) {
			tableUsers[i][0]=new String(usersAll.get(i));
		}
		table = new JTable(tableUsers, label);
		scrollPane = new JScrollPane(table);
		actifUsers.add(scrollPane, BorderLayout.EAST);
		
		
		
		
		generalFrame.add(header, BorderLayout.EAST);
		generalFrame.add(actifUsers, BorderLayout.SOUTH);
		generalFrame.add(broadcast, BorderLayout.WEST);
		


	}
	
	public static void main (String[] args) {
		new General(username) ;
	}
}
