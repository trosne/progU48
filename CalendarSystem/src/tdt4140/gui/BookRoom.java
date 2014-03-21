package tdt4140.gui;

import tdt4140.calendarsystem.Appointment;
import tdt4140.calendarsystem.MeetingRoom;
import tdt4140.calendarsystem.Reservation;
import tdt4140.calendarsystem.RoomManager;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class BookRoom {
	private JDialog bookDialog;
	static Container pane;
    private JComboBox cmbxRoom;
	private JSpinner spCaps;
    private ArrayList<MeetingRoom> available;


	public BookRoom(JDialog dialog, boolean modal, String dialogName, final Appointment appointment, final JTextField locationField, final JTextField reservationField, final JButton bookButton){


        //if it already has a reservation:
        if (appointment.getRes() != null)
            RoomManager.getInstance().removeReservation(appointment.getRes());


		bookDialog = new JDialog(dialog, dialogName, modal);
		bookDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		bookDialog.setBounds(600, 100, 300, 160);

        cmbxRoom = new JComboBox();
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
		btnCancel.setBounds(195, 92, 89, 23);
		contentPanel.add(btnCancel);
		
		JButton btnBook = new JButton("Book it !");
		btnBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                appointment.setRes(RoomManager.getInstance().
                        reserveRoom(RoomManager.getInstance().getRoom((String) cmbxRoom.getSelectedItem()),
                                appointment.getStart(), appointment.getEnd()));
                locationField.setText("");
                locationField.setEditable(false);
                locationField.setBackground(Color.WHITE);
                reservationField.setText(appointment.getRes().getRoom().getRoomID());
                bookButton.setText("Cancel booking");
                bookDialog.setVisible(false);
            }
        });
		

        //fetch available rooms:
        available = RoomManager.getInstance().
                generateAvailableRooms(appointment.getStart(), appointment.getEnd());

        int maxCap = 1;
        for (int i = 0; i < available.size(); i++)
            if (available.get(i).getCap() > maxCap)
                maxCap = available.get(i).getCap();

        spCaps = new JSpinner(new SpinnerNumberModel((maxCap >= 5)? 5 : 1, 1, maxCap, 1));
        spCaps.setBounds(10, 37, 49, 20);
        spCaps.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                populateRoomList();
            }
        });

        if (available.size() > 0)
        {
            populateRoomList();

            contentPanel.add(cmbxRoom);
            cmbxRoom.setBounds(10, 87, 160, 20);
            JLabel lblChooseRoom = new JLabel("Choose room");
            lblChooseRoom.setBounds(10, 67, 160, 14);
            contentPanel.add(lblChooseRoom);
            JLabel lblChooseCap = new JLabel("Choose minimum capacity");
            lblChooseCap.setBounds(10, 17, 160, 14);
            contentPanel.add(lblChooseCap);
            btnBook.setBounds(195, 65, 89, 23);
            contentPanel.add(btnBook);
            contentPanel.add(spCaps);

            final JLabel lblShowCap = new JLabel("Capacity: " + RoomManager.getInstance().getRoom((String)cmbxRoom.getSelectedItem()).getCap());
            lblShowCap.setBounds(195, 45, 89, 23);
            contentPanel.add(lblShowCap);
            cmbxRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MeetingRoom room = RoomManager.getInstance().getRoom((String) cmbxRoom.getSelectedItem());
                    if (room != null)
                        lblShowCap.setText("Capacity: " + room.getCap());
                }
            });
        }
        else
        {
            JLabel lblNoRooms = new JLabel("No rooms available.");
            lblNoRooms.setBounds(10, 11, 309, 14);
            contentPanel.add(lblNoRooms);
        }

		bookDialog.setVisible(true);
		
		
	}

    /**
     * Wipes the room list and fills it up again. Used both at creation and when minCap is changed
     */
    private void populateRoomList()
    {
        boolean firstTime = cmbxRoom.getItemCount() == 0;
        cmbxRoom.removeAllItems();
        for (int i = 0; i < available.size(); i++)
            if (available.get(i).getCap() >= (int) spCaps.getValue())
                cmbxRoom.addItem(available.get(i).getRoomID());

        //cmbxRoom.setSelectedIndex(0);
/*
        if (!firstTime)
            cmbxRoom.actionPerformed(null);*/
    }
}
