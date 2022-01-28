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
public class Clavardage {
	private JFrame clavardageFrame ;
	
	private JPanel clavardagePanel ;
	private JPanel header ;
	
	private JButton btnDisconect = new JButton ("Déconnexion");
	private JButton btnChangeNickname = new JButton ("Changer pseudo"); 

	
	private JTextField clavardageField ;
	private JButton btnSend ;
	private JTextArea clavardageArea ;
	
	public static String nickname;

	public Clavardage(String  username) throws IOException {
		Clavardage.nickname = username ;
		
		clavardageFrame = new JFrame ("Clavardage");
		
		
	}
	
	public static void main (String[] args) throws IOException{
		new Clavardage(nickname) ;
	}
	
}
