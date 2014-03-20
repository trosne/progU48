package tdt4140.gui;



import tdt4140.calendarsystem.*;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;


public class ArrDialog{

	//private JPanel contentPanel;
    static Container pane;
    private JTextField txtDescr;
    private JTextField txtLocation;
    private JTextField txtDateDay, txtDateMonth, txtDateYear;
    DefaultListModel mFromLst = new DefaultListModel();
    DefaultListModel mToLst = new DefaultListModel();
    DefaultListModel mExtLst = new DefaultListModel();
    private JTextField txtExtPart;
    private JDialog d, dReserv;
    private JRadioButton rbNoAnswer, rbHideAppointment, rbAccepted;

    //private Date startDate, endDate;
    private Appointment appointment;

    
    protected MaskFormatter createFormatter(String s, String looks) {
		
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setValidCharacters("#1234567890");
            formatter.setPlaceholder(looks);
            formatter.setOverwriteMode(true);
            
    	    
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }

    private int formatterGetHour(String value)
    {
        return Integer.decode(value.substring(0, 2));
    }
    private int formatterGetMin(String value)
    {
        return Integer.decode(value.substring(3));
    }

    private String formatterToString(int hour, int min)
    {
        String result = "";
        if (hour < 10)
            result +="0";
        result += hour + ":";
        if (min < 10)
            result +="0";
        result += min;
        return result;
    }
    /**
	 * Create the dialog.
	 */
    
	public ArrDialog(JFrame frame, boolean modal, String dialogName, Appointment a) {


        //create appointment:
        if (a == null)
        {
            a = new Appointment();
            a.addParticipant(UserManager.getInstance().getCurrentUser());
            a.setStart(new Date(System.currentTimeMillis()));
            a.setEnd(new Date(System.currentTimeMillis() + 1000 * 60 * 60));//+1hr
        }

        this.appointment = a;

        UserManager userManager = UserManager.getInstance();
        ArrayList<User> users = userManager.getUsers();
		for (int i = 0; i < users.size(); i++)
        {
            if (appointment.getParticipant(users.get(i)) == null)
                mFromLst.add(0, users.get(i).getName());
            else
                mToLst.add(0, users.get(i).getName());
        }

        ArrayList<Group> groups = userManager.getGroups();
        for (int i = 0; i < groups.size(); i++)
            mFromLst.add(0, groups.get(i).getName() + " (Group)");



        //final JDialog
		d = new JDialog(frame, dialogName, true);


		
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setBounds(700, 100, 493, 524);
		
		pane = d.getContentPane();
		pane.setLayout(null);
		
		//Create and set working Content Panel 
		JPanel contentPanel = new JPanel(null);
		pane.add(contentPanel);
		contentPanel.setBounds(0, 0, 477, 489);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		
		//Create and place all labels on Content Panel  
		JLabel lblNewLabel = new JLabel("Date");
		lblNewLabel.setBounds(10, 11, 46, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Decription");
		lblNewLabel_1.setBounds(10, 36, 59, 14);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Start");
		lblNewLabel_2.setBounds(10, 61, 46, 14);
		contentPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("End");
		lblNewLabel_3.setBounds(10, 86, 46, 14);
		contentPanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Duration");
		lblNewLabel_4.setBounds(10, 111, 46, 14);
		contentPanel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Location");
		lblNewLabel_5.setBounds(10, 136, 46, 14);
		contentPanel.add(lblNewLabel_5);
		
		//Create and place all editable/non-editable text fields on Content Panel 
		
		txtDescr = new JTextField();
		txtDescr.setBounds(79, 33, 212, 20);
		contentPanel.add(txtDescr);
		txtDescr.setColumns(10);

		
	    //TIME TEXT FIELDS:
	    final JFormattedTextField ftxtTStart = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtTStart.setBounds(79, 58, 91, 20);
		contentPanel.add(ftxtTStart);
        ftxtTStart.setValue(formatterToString(appointment.getStart().getHours(), appointment.getStart().getMinutes()));
		
		final JFormattedTextField ftxtTEnd = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtTEnd.setBounds(79, 83, 91, 20);
		contentPanel.add(ftxtTEnd);
        ftxtTEnd.setValue(formatterToString(appointment.getEnd().getHours(), appointment.getEnd().getMinutes()));
		
		final JFormattedTextField ftxtDur = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtDur.setBounds(79, 108, 91, 20);
		contentPanel.add(ftxtDur);
        ftxtDur.setEditable(false);

        //radiobuttons for duration and end field
        final JRadioButton rbTEnd = new JRadioButton();
        rbTEnd.setBounds(190, 88, 10, 10);
        rbTEnd.setSelected(true);

        final JRadioButton rbDur = new JRadioButton();
        rbDur.setBounds(190, 113, 10, 10);

        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(rbTEnd);
        timeGroup.add(rbDur);
        contentPanel.add(rbTEnd);
        contentPanel.add(rbDur);

        rbTEnd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (rbTEnd.isSelected())
                {
                    ftxtTEnd.setEditable(true);
                    ftxtDur.setEditable(false);
                    ftxtDur.setValue("##:##");
                    ftxtTEnd.setValue(formatterToString(appointment.getEnd().getHours(), appointment.getEnd().getMinutes()));
                }
            }
        });

        rbDur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (rbDur.isSelected())
                {
                    ftxtTEnd.setEditable(false);
                    ftxtDur.setEditable(true);
                    ftxtTEnd.setValue("##:##");
                    ftxtDur.setValue("01:00");
                }
            }
        });

		txtLocation = new JTextField();
		txtLocation.setBounds(79, 133, 91, 20);
		contentPanel.add(txtLocation);
		txtLocation.setColumns(10);
		
		txtDate = new JTextField();
		txtDate.setEditable(false);
		txtDate.setBounds(79, 8, 108, 20);
		contentPanel.add(txtDate);
        txtDate.setText(appointment.getStart().getDate() + "/" + (appointment.getStart().getMonth() + 1) + " " +
                (1900 + appointment.getStart().getYear()));
		txtDate.setColumns(10);
		
		
		// Create and place button to delete current arrangement
		JButton btnNewButton = new JButton("Delete appointment");
		btnNewButton.setBounds(305, 7, 140, 23);
		contentPanel.add(btnNewButton);
		
		//Create and place separate content panel for JLists to add participants 
		JPanel pnlParticipants = new JPanel(null);
		pnlParticipants.setBounds(10, 176, 312, 302);
		pnlParticipants.setBorder(BorderFactory.createTitledBorder("Add participants"));
		pnlParticipants.setBackground(contentPanel.getBackground());
		contentPanel.add(pnlParticipants);
		
		//Create and add Labels for participant's fields
		JLabel lblGroupsusers = new JLabel("Internal groups/users");
		lblGroupsusers.setBounds(10, 29, 219, 14);
		pnlParticipants.add(lblGroupsusers);
		
		JLabel lblParticipants = new JLabel("Internal participants");
		lblParticipants.setBounds(10, 112, 206, 14);
		pnlParticipants.add(lblParticipants);
		
		JLabel lblExtParticipants = new JLabel("External participants");
		lblExtParticipants.setBounds(10, 194, 219, 14);
		pnlParticipants.add(lblExtParticipants);
		
		//Create JList lstFrom (JList populated from model - populated on top) - source data for user groups and users 
		JList lstFrom = new JList(mFromLst);
		lstFrom.setBounds(10, 21, 292, 83);
		lstFrom.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstFrom.setPrototypeCellValue("Group name ##");
	
		lstFrom.addMouseListener(new MouseAdapter ()
		{
			
		 	public void mousePressed( MouseEvent e){
	    		if (e.getClickCount()==2){
	    			
	    		
	    			JList target = (JList)e.getSource();
	    			int index = target.getSelectedIndex();
	    			mToLst.addElement(target.getModel().getElementAt(index));
	    	
	    		}
	    	}
		});
		
		//Create and add scroller as JList-lstFrom container
		JScrollPane sp = new JScrollPane(lstFrom);
		sp.setBounds(10, 43, 292, 58);
		pnlParticipants.add(sp);
		
		//Create  JList lstTo, populated manually with values from the JList above - used to save info about participants
		JList lstTo = new JList(mToLst);
		lstTo.setBounds(10, 118, 292, 80);
		lstTo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstTo.setPrototypeCellValue("Group name ##");
		
		lstTo.addMouseListener(new MouseAdapter ()
		{
			
		 	public void mousePressed( MouseEvent e){
	    		if (e.getClickCount()==2){
	    			
	    		
	    			JList target = (JList)e.getSource();
	    			DefaultListModel model = (DefaultListModel)target.getModel();
	    			int index = target.getSelectedIndex();
	    			if (index !=-1)
	    				model.remove(index);
	    		}
	    	}
		});
		
		//Create and add scroller as JList-lstTo container
		JScrollPane sp2 = new JScrollPane(lstTo);
		sp2.setBounds(10, 125, 292, 58);
		pnlParticipants.add(sp2);
		
		//Create JList with external participants
		JList lstExt = new JList(mExtLst);
		lstExt.setBounds(10, 233, 292, 58);
		lstExt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstExt.setPrototypeCellValue("email length ##");
		
		lstExt.addMouseListener(new MouseAdapter ()
		{
			
		 	public void mousePressed( MouseEvent e){
	    		if (e.getClickCount()==2){
	    			
	    		
	    			JList target = (JList)e.getSource();
	    			DefaultListModel model = (DefaultListModel)target.getModel();
	    			int index = target.getSelectedIndex();
	    			if (index !=-1)
	    				model.remove(index);
	    		}
	    	}
		});
		
		//Create and add scroller as JList-lstExt container
		JScrollPane sp3 = new JScrollPane(lstExt);
		sp3.setBounds(10, 233, 292, 58);
		pnlParticipants.add(sp3);
		
		
		//Create and add text field and button to add external participant's emails
		
		txtExtPart = new JTextField();
		txtExtPart.setToolTipText("Enter email");
		txtExtPart.setBounds(10, 208, 219, 20);
		pnlParticipants.add(txtExtPart);
		txtExtPart.setColumns(10);
		
		JButton btnAddEmail = new JButton("Add");
		btnAddEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txt = txtExtPart.getText();
				//System.out.println((String)txt);
				
				if (!txt.isEmpty())
					mExtLst.addElement((String)txtExtPart.getText());
				
			}
		});
		btnAddEmail.setBounds(240, 207, 62, 23);
		pnlParticipants.add(btnAddEmail);
		
		
		// Create and add button to Cancel current appointment without save
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(369, 455, 108, 23);
		contentPanel.add(btnCancel);
			//ActionListener
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d.setVisible(false);
				d.dispose();
			}
		});
		
		//Create and add button to Save current appointment and exit to MainFrame
		JButton btnSaveAndExit = new JButton("Save and exit");
		btnSaveAndExit.setBounds(369, 432, 108, 23);
		contentPanel.add(btnSaveAndExit);
		//--ActionListener
		
		//Create and add a button to open Room Booking dialog 
		JButton btnBookARoom = new JButton("Book a room");
		btnBookARoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
	    			public void run() {
	    				try {
	    					//System.out.println(CalendarPanel.currentMonth);
	    					new BookRoom(d,true,"Room Booking", appointment, txtLocation);

	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		});
				
				
			}
		});
		btnBookARoom.setBounds(180, 132, 111, 23);
		contentPanel.add(btnBookARoom);

        //status for response
        if (appointment.getParticipants().get(0).getaUser() != UserManager.getInstance().getCurrentUser())//not creator
        {
            rbNoAnswer = new JRadioButton(Participant.STATUS_NOT_RESPONDED);
            rbNoAnswer.setBounds(254, 380, 217, 23);
            contentPanel.add(rbNoAnswer);
            rbNoAnswer.setSelected(true);

            rbHideAppointment = new JRadioButton(Participant.STATUS_DECLINED);
            rbHideAppointment.setBounds(254, 380, 217, 23);
            contentPanel.add(rbHideAppointment);

            rbAccepted = new JRadioButton(Participant.STATUS_ATTENDING);
            rbAccepted.setBounds(254, 354, 97, 23);
            contentPanel.add(rbAccepted);

            ButtonGroup statusGroup = new ButtonGroup();
            statusGroup.add(rbNoAnswer);
            statusGroup.add(rbHideAppointment);
            statusGroup.add(rbAccepted);
        }


		

		
		
		// Make it visible after everything is added !!!!!!
		d.setVisible(true);
		

	}
}
