package View;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.*;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.SystemColor;

/**
 * Classe qui represente la fenetre pour changer le pseudp
 * Cette fenetre est affichee lorsque l'utilisateur clique sur le bouton "Changer Pseudo"
 */

public class ChangerPseudo extends JFrame {


	private static final long serialVersionUID = 1L;
	private Controller app;
	private JPanel contentPane;
	private JTextField textField;
	
	/**
	 * Constructeur de la classe ChangerPseudo 
	 */
	public ChangerPseudo(Controller app) {
		setBackground(new Color(204, 204, 255));
		this.app=app;
		initialize();
	}
	

	public void initialize() {
		//frame
		setTitle("Changer Pseudo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 516, 275);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Message pour choisir le nouveau pseudo
		JLabel lblNewLabel = new JLabel("Choisis ton nouveau pseudo\r\n");
		lblNewLabel.setForeground(new Color(204, 204, 255));
		lblNewLabel.setBackground(new Color(204, 204, 255));
		lblNewLabel.setFont(new Font("Comfortaa", Font.BOLD, 15));
		lblNewLabel.setBounds(114, 65, 232, 43);
		contentPane.add(lblNewLabel);
		
		
		//textField pour taper son nouveau pseudo
		textField = new JTextField();
		textField.setBackground(new Color(255, 255, 240));
		textField.setBounds(114, 102, 224, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.addMouseListener(new MouseListener() {           
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
		textField.addActionListener(new Connect());

		//Bouton Confirmer
		JButton btnNewButton = new JButton("Confirmer");
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setBackground(new Color(204, 204, 255));
		btnNewButton.setFont(new Font("Comfortaa", Font.BOLD, 13));
		btnNewButton.setBounds(345, 108, 103, 23);
		btnNewButton.addActionListener(new Connect());
		contentPane.add(btnNewButton);
		setVisible(true);
	}
	
	/**
	 * Methode pour verifier l'unicite du pseudp
	 *
	 */
	public class Connect implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String pseudo=textField.getText();
			
			if(pseudo.length()>12) {
				JTextPane txtlongpseudo = new JTextPane();
				txtlongpseudo.setText("Pseudo trop long.");
				txtlongpseudo.setForeground(new Color(255, 51, 51));
				txtlongpseudo.setFont(new Font("Bahnschrift", Font.BOLD | Font.ITALIC, 11));
				txtlongpseudo.setBackground(SystemColor.menu);
				txtlongpseudo.setBounds(103, 80, 204, 14);
				contentPane.add(txtlongpseudo);
				textField.addMouseListener(new MouseListener() {           
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
				if (app.getcSystem().editNickname(pseudo, 4445)) {
					app.getMe().setNickname(pseudo);
					General.pseudoModif();

					dispose(); //ferme la fenetre
				} else {
					JTextPane txtpnPseudonymAlreadyIn = new JTextPane();
					txtpnPseudonymAlreadyIn.setText("Pseudo déjà utilisé. Veuillez en choisir un autre");
					txtpnPseudonymAlreadyIn.setBackground(new Color(0,0,0));
					txtpnPseudonymAlreadyIn.setForeground(new Color(204, 204, 255));
					txtpnPseudonymAlreadyIn.setFont(new Font("Bahnschrift", Font.BOLD | Font.ITALIC, 11));
					txtpnPseudonymAlreadyIn.setBounds(103, 80, 350, 14);

					contentPane.add(txtpnPseudonymAlreadyIn);
					textField.addMouseListener(new MouseListener() {           
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
}
