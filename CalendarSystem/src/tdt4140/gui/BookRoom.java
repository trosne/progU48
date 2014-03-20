package tdt4140.gui;

import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class BookRoom {
	private JDialog bookDialog;
	static Container pane;
	
	public BookRoom(JDialog dialog, boolean modal, String dialogName){
		
		bookDialog = new JDialog(dialog, dialogName, modal);
		bookDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		bookDialog.setBounds(1200, 100, 300, 160);
		
		pane = bookDialog.getContentPane();
		pane.setLayout(null);
		
		//Create and set working Content Panel 
		JPanel contentPanel = new JPanel(null);
		pane.add(contentPanel);
		contentPanel.setBounds(0, 0, 284, 125);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				bookDialog.setVisible(false);
				bookDialog.dispose();
			}
		});
		btnCancel.setBounds(195, 102, 89, 23);
		contentPanel.add(btnCancel);
		
		JButton btnBook = new JButton("Book it !");
		btnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				Main Logic Here
//				
//				
//				
//				
			}
		});
		btnBook.setBounds(195, 75, 89, 23);
		contentPanel.add(btnBook);
		
		JComboBox cmbxRoom = new JComboBox();
		cmbxRoom.setBounds(10, 27, 89, 20);
		contentPanel.add(cmbxRoom);
		
		JLabel lblChooseRoom = new JLabel("Choose room");
		lblChooseRoom.setBounds(10, 11, 89, 14);
		contentPanel.add(lblChooseRoom);
		bookDialog.setVisible(true);
		
		
	}
}
