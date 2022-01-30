package View;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class Exit {
	private JFrame exitFrame ; 
	
	private JPanel exitPanel = new JPanel (new FlowLayout(FlowLayout.CENTER));
	private JButton btnExit = new JButton ("Confirmer");
	private JLabel exitLabel ;
	
	private static String username ;
	
	public Exit(String nickname) throws IOException {
		username=nickname;
		
		exitFrame = new JFrame ("Modifier pseudo"); 	
		
		exitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		exitFrame.setSize(600, 400);
		exitFrame.setLocationRelativeTo(null);
		exitFrame.setVisible(true);
		
		exitLabel = new JLabel ("Attention, vous allez changer votre pseudo, êtes-vous sûr?");
		exitLabel.setBounds(188,82,160,30);
		exitPanel.add(exitLabel);
		btnExit.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent MyActionEvent) {
						exitFrame.setVisible(false);
							General general = new General (nickname);
					}
					
				}
				);
		exitFrame.getRootPane().setDefaultButton(btnExit);

		
		btnExit.setBounds(188, 90, 160, 30);
		exitPanel.add(btnExit);
		
	
		exitFrame.add(exitPanel);
		}
	
	public static void main (String[] args) throws IOException {
		new Exit(username);	;
	}
}
