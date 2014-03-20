package tdt4140.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LogIn extends JFrame {
	
	
	private JPanel contentPane;
	private JTextField textFUsr;
	private JTextField textFPass;
	private JButton btnEnter;
	//static 
	
	//static JPanel LoginPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn frame = new LogIn();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the LogIn frame.
	 */
	public LogIn() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 291, 208);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JPanel LoginPanel = new JPanel(null);
		JLabel PassLabel = new JLabel("Password");
		JLabel UserLabel = new JLabel("Username");
		
		
		
		contentPane.add(LoginPanel);
		LoginPanel.setBounds(0, 0, 267, 173);
		LoginPanel.setBorder(BorderFactory.createTitledBorder("Enter username/pasword :"));
		
		
		UserLabel.setBounds(10, 48, 69, 14);
		LoginPanel.add(UserLabel);
		

		PassLabel.setBounds(10, 85, 69, 14);
		LoginPanel.add(PassLabel);
		
		textFUsr = new JTextField();
		textFUsr.setBounds(137, 48, 86, 20);
		LoginPanel.add(textFUsr);
		textFUsr.setColumns(10);
		
		textFPass = new JTextField();
		textFPass.setBounds(137, 85, 86, 20);
		LoginPanel.add(textFPass);
		textFPass.setColumns(10);
		
		
		btnEnter = new JButton("Enter");
		btnEnter.addActionListener(bl);
	
		btnEnter.setBounds(84, 128, 89, 23);
		LoginPanel.add(btnEnter);
		
		
		

	}
	
	private ActionListener bl = new ActionListener() { 
		public void actionPerformed(ActionEvent e) { 
			
			// Close the login window
			setVisible(false);
			dispose();
			// Open the MainFrame
			MainFrame nextFrame = new MainFrame();
			nextFrame.setVisible(true);
			nextFrame.setUsername(textFUsr.getText());
	
		} 
		}; 
}
