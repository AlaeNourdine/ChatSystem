package View;

import Controller.*;
import Model.*;
import Network.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.JLabel;

/**
 * Classe view correpond à la première fenetre de connexion du systeme
 * Elle affiche un message de bienvenue et permet à l'utilisateur de saisir son pseudo et se connecter
 */

public class View {
	
	private Controller app;
	private JFrame frame;
	private JTextField Pseudo;
	private JLabel lblBienvenue;
	private JLabel lblChoisisTonPseudo;
	private JLabel lblChatbox;

	/**
	 * Constructeur de la classe view
	 */
	public View() {
		User u1= new User();
		app= new Controller(u1);
		app.setServerUDP(new UDPReceive(app));
		app.setcSystem(new ControllerChat(app));
		app.setDb(new BDD(app));
		initialize();
	}
	
	/**
	 * Lancement de la fenetre de connexion
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		//frame Connexion
		frame = new JFrame("Connexion");
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBackground(new Color(204, 204, 255));
		frame.getContentPane().setForeground(new Color(255, 255, 255));
		frame.setBounds(260, 62, 365, 401);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);
		frame.getContentPane().setLayout(null);
		
		//bouton de connexion
		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.setBounds(118, 300, 125, 44);
		btnNewButton.setBorder(null);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.setBackground(new Color(204, 204, 255));
		btnNewButton.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
		btnNewButton.setForeground(Color.GRAY);
		
		//champ pour ecrire le pseudo
		Pseudo = new JTextField("");
		Pseudo.setBounds(43, 251, 270, 37);
		Pseudo.setSelectionColor(new Color(204, 204, 255));
		frame.getContentPane().add(Pseudo);
		Pseudo.setBackground(new Color(255, 255, 240));
		Pseudo.setHorizontalAlignment(SwingConstants.CENTER);
		Pseudo.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
		Pseudo.setColumns(10);
		Pseudo.getFont().deriveFont(Font.ITALIC);
		Pseudo.setForeground(Color.gray);
		
		//message de bienvenue 
		lblBienvenue = new JLabel("Bienvenue dans notre");
		lblBienvenue.setAlignmentY(1.0f);
		lblBienvenue.setAlignmentX(1.0f);
		lblBienvenue.setFont(new Font("Comfortaa", Font.BOLD, 12));
		lblBienvenue.setBounds(100, 57, 184, 63);
		frame.getContentPane().add(lblBienvenue);
		lblBienvenue.setBackground(new Color(204, 204, 255));
		lblBienvenue.getFont().deriveFont(Font.ITALIC);
		lblBienvenue.setForeground(new Color(204, 204, 255));
		lblChatbox = new JLabel("ChatBox ");
		lblChatbox.setForeground(new Color(204, 204, 255));
		lblChatbox.setFont(new Font("Comfortaa", Font.BOLD | Font.ITALIC, 16));
		lblChatbox.setBackground(new Color(204, 204, 255));
		lblChatbox.setBounds(130, 75, 154, 63);
		frame.getContentPane().add(lblChatbox);
		
		//label pour choisir le pseudo
		lblChoisisTonPseudo = new JLabel("Choisis ton pseudo");
		lblChoisisTonPseudo.setForeground(new Color(204, 204, 255));
		lblChoisisTonPseudo.setBounds(100, 224, 170, 15);
		frame.getContentPane().add(lblChoisisTonPseudo);

		
		Pseudo.addMouseListener(new MouseListener() {           
			@Override
			public void mouseReleased(MouseEvent e) {}         
			@Override
			public void mousePressed(MouseEvent e) {}          
			@Override
			public void mouseExited(MouseEvent e) {}           
			@Override
			public void mouseEntered(MouseEvent e) {}          
			@Override
			public void mouseClicked(MouseEvent e) {
				JTextField texteField = ((JTextField)e.getSource());
				texteField.setText("");
				texteField.getFont().deriveFont(Font.PLAIN);
				texteField.setForeground(Color.black);
				texteField.removeMouseListener(this);
			}
		});
		
		//possibilite d'utiliser le bouton "entrer" au lieu de cliquer sur le bouton connexion
		Pseudo.addActionListener(new Connect());
		btnNewButton.addActionListener(new Connect());
		
	}
	
	/** 
	 * Test de l'unicite du pseudo :
	 * Si oui, on se connecte
	 * Si non, cela renvoie un message
	 */
	public class Connect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String pseudo= Pseudo.getText();
			
			if(pseudo.length()>12) {
				JTextPane txtlongpseudo = new JTextPane();
				txtlongpseudo.setBackground(new Color(0, 0, 0));
				txtlongpseudo.setText("Votre pseudo doit faire moins de 12 caracteres.");
				txtlongpseudo.setForeground(new Color(204, 204, 255));
				txtlongpseudo.setFont(new Font("Bahnschrift", Font.BOLD | Font.ITALIC, 11));
				txtlongpseudo.setBounds(0, 224, 400, 15);
				frame.getContentPane().add(txtlongpseudo);
				Pseudo.addMouseListener(new MouseListener() {           
					@Override
					public void mouseReleased(MouseEvent e) {}         
					@Override
					public void mousePressed(MouseEvent e) {}          
					@Override
					public void mouseExited(MouseEvent e) {}           
					@Override
					public void mouseEntered(MouseEvent e) {}          
					@Override
					public void mouseClicked(MouseEvent e) {
						JTextField texteField = ((JTextField)e.getSource());
						texteField.setText("");
						texteField.getFont().deriveFont(Font.PLAIN);
						texteField.setForeground(Color.black);
						texteField.removeMouseListener(this);
					}
				});
			}
			else {
				//connexion
				if (app.getcSystem().Connexion(pseudo)) {
					app.getMe().setNickname(pseudo);
					pageHome();
				} else {
					JTextPane txtpnPseudonymAlreadyIn = new JTextPane();
					txtpnPseudonymAlreadyIn.setBackground(new Color(0, 0, 0));
					txtpnPseudonymAlreadyIn.setText("Pseudo déjà utilisé. Veuillez en choisir un autre.");
					txtpnPseudonymAlreadyIn.setForeground(new Color(255, 51, 51));
					txtpnPseudonymAlreadyIn.setFont(new Font("Bahnschrift", Font.BOLD | Font.ITALIC, 11));
					txtpnPseudonymAlreadyIn.setBounds(30, 224, 400, 15);
					frame.getContentPane().add(txtpnPseudonymAlreadyIn);
					Pseudo.addMouseListener(new MouseListener() {           
						@Override
						public void mouseReleased(MouseEvent e) {}         
						@Override
						public void mousePressed(MouseEvent e) {}          
						@Override
						public void mouseExited(MouseEvent e) {}           
						@Override
						public void mouseEntered(MouseEvent e) {}          
						@Override
						public void mouseClicked(MouseEvent e) {
							JTextField texteField = ((JTextField)e.getSource());
							texteField.setText("");
							texteField.getFont().deriveFont(Font.PLAIN);
							texteField.setForeground(Color.black);
							texteField.removeMouseListener(this);
						}
					});
				}
			}
		}
	}
	
	/**
	 * Methode pour ouvrir la fenetre generale General
	 */
    private void pageHome () {
    	frame.setVisible(false);
		new General(app);
		frame.dispose();
    }
}
