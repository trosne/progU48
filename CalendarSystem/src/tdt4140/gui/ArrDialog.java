package tdt4140.gui;



import tdt4140.calendarsystem.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;


public class ArrDialog{

	//private JPanel contentPanel;
    static Container pane;
    private JTextField txtDescr;
    private JTextField txtLocation, txtReservation;
    private JButton btnBookARoom;
    private JComboBox<Integer> cmbDay, cmbYear;
    private JComboBox<String> cmbMonth;
    DefaultListModel mFromLst = new DefaultListModel();
    DefaultListModel mToLst = new DefaultListModel();
    DefaultListModel mExtLst = new DefaultListModel();
    private JTextField txtExtPart;
    private JDialog d, dReserv;
    private JRadioButton rbDur, rbTEnd;
    private JFormattedTextField ftxtTStart, ftxtTEnd, ftxtDur;
    private boolean endUseDuration;
    private boolean isChange, fieldsHaveChanged;
    private AppointmentStatusPanel pnlStatus;

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

        fieldsHaveChanged = false;
        //create appointment:
        if (a == null)
        {
            a = new Appointment();
            a.addParticipant(UserManager.getInstance().getCurrentUser());
            a.setStart(new Date(System.currentTimeMillis()));
            a.setEnd(new Date(System.currentTimeMillis() + 1000 * 60 * 60));//+1hr
            isChange = false;
        }
        else
            isChange = true;

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

        for (String extUser : appointment.getExtParticipants())
                mExtLst.add(0, extUser);

        //listener for changes in time/date
        final ActionListener timeChangedListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                autoUpdateBooking();
                fieldsHaveChanged = true;
            }
        };

        //final JDialog
		d = new JDialog(frame, dialogName, true);

		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setBounds(300, 100, 570, 524);
		
		pane = d.getContentPane();
		pane.setLayout(null);
		
		//Create and set working Content Panel 
		JPanel contentPanel = new JPanel(null);
		pane.add(contentPanel);
		contentPanel.setBounds(0, 0, 677, 489);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);

        pnlStatus = new AppointmentStatusPanel(contentPanel, appointment);

		//Create and place all labels on Content Panel  
		JLabel lblNewLabel = new JLabel("Date");
		lblNewLabel.setBounds(10, 11, 46, 14);
		contentPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Description");
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

        JLabel lblNewLabel_6 = new JLabel("Reservation");
        lblNewLabel_6.setBounds(10, 161, 66, 14);
        contentPanel.add(lblNewLabel_6);
		
		//Create and place all editable/non-editable text fields on Content Panel 

		txtDescr = new JTextField();
		txtDescr.setBounds(79, 33, 212, 20);
		contentPanel.add(txtDescr);
		txtDescr.setColumns(10);
        if (isChange)
            txtDescr.setText(appointment.getDescription());

		
	    //TIME TEXT FIELDS:
	    ftxtTStart = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtTStart.setBounds(79, 58, 91, 20);
		contentPanel.add(ftxtTStart);
        ftxtTStart.setValue(formatterToString(appointment.getStart().getHours(), appointment.getStart().getMinutes()));
        ftxtTStart.addActionListener(timeChangedListener);
		
		ftxtTEnd = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtTEnd.setBounds(79, 83, 91, 20);
		contentPanel.add(ftxtTEnd);
        ftxtTEnd.setValue(formatterToString(appointment.getEnd().getHours(), appointment.getEnd().getMinutes()));
        ftxtTEnd.addActionListener(timeChangedListener);
		
		ftxtDur = new JFormattedTextField(createFormatter("**:**","#####"));
		ftxtDur.setBounds(79, 108, 91, 20);
		contentPanel.add(ftxtDur);
        ftxtDur.setEditable(false);
        ftxtDur.addActionListener(timeChangedListener);

        //radiobuttons for duration and end field
        rbTEnd = new JRadioButton();
        rbTEnd.setBounds(190, 88, 91, 20);
        rbTEnd.setSelected(true);

        rbDur = new JRadioButton();
        rbDur.setBounds(190, 108, 91, 20);

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
                    endUseDuration = false;
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
                    endUseDuration = true;
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
        if (isChange && appointment.getRes() == null)
            txtLocation.setText(appointment.getLocation());

        txtLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fieldsHaveChanged = true;
            }
        });

        txtReservation = new JTextField();
        txtReservation.setBounds(79, 158, 91, 20);
        contentPanel.add(txtReservation);
        txtReservation.setEditable(false);
        if (isChange && appointment.getRes() != null)
            txtReservation.setText(appointment.getRes().getRoom().getRoomID());

        // Date picking:

        //day
        final int maxDay = new GregorianCalendar(appointment.getStart().getYear() + 104, appointment.getStart().getMonth(), 1)
                .getActualMaximum(Calendar.DAY_OF_MONTH);

        Integer[] days = new Integer[maxDay];
        for (int i = 0; i < maxDay; i++)
            days[i] = i + 1;
        cmbDay = new JComboBox<>(days);
        cmbDay.setBounds(79, 8, 60, 20);
        contentPanel.add(cmbDay);
        cmbDay.setSelectedIndex(appointment.getStart().getDate() - 1);
        cmbDay.addActionListener(timeChangedListener);

        //month
        String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();

        cmbMonth = new JComboBox<>(months);
        cmbMonth.setBounds(149, 8, 80, 20);
        contentPanel.add(cmbMonth);
        cmbMonth.setSelectedIndex(appointment.getStart().getMonth());
        cmbMonth.addActionListener(timeChangedListener);

        //year
        Integer[] years = new Integer[30];
        for (int i = 0; i < 30; i++)
            years[i] = i + 2014;
        cmbYear = new JComboBox<>(years);
        cmbYear.setBounds(239, 8, 80, 20);
        contentPanel.add(cmbYear);
        cmbYear.addActionListener(timeChangedListener);


		
		// Create and place button to delete current arrangement
		JButton btnNewButton = new JButton("Delete appointment");
		btnNewButton.setBounds(345, 7, 200, 23);
		contentPanel.add(btnNewButton);
		
		//Create and place separate content panel for JLists to add participants 
		JPanel pnlParticipants = new JPanel(null);
		pnlParticipants.setBounds(10, 186, 312, 302);
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
                    if (!mToLst.contains(mFromLst.get(index)))
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
                JList target = (JList)e.getSource();
                DefaultListModel model = (DefaultListModel)target.getModel();
                int index = target.getSelectedIndex();
	    		switch (e.getClickCount()){
                    case 2:
                        if (index > 0)
                        {
                            model.remove(index);
                            pnlStatus.setUser(null);
                        }
                    break;
                    case 1:
                        String elem = (String) mToLst.get(index);
                        if (!elem.contains("(Group)"))
                        {
                            User u = UserManager.getInstance().getUserFromName(elem);
                            if (u == null)
                                System.out.println("[ArrDialog]: Couldnt find user " + elem + " in user manager.");
                            else
                                pnlStatus.setUser(u);
                        }
                        else
                            pnlStatus.setUser(null);

	    		}

	    	}
		});
		
		//Create and add scroller as JList-lstTo container
		JScrollPane sp2 = new JScrollPane(lstTo);
		sp2.setBounds(10, 125, 292, 58);
		pnlParticipants.add(sp2);
		
		//Create JList with external participants
		final JList lstExt = new JList(mExtLst);
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
	    			if (index != -1)
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
		btnCancel.setBounds(329, 455, 208, 23);
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
		btnSaveAndExit.setBounds(329, 432, 208, 23);
		contentPanel.add(btnSaveAndExit);

        //Exit function:
        btnSaveAndExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateDateFromFields();

                boolean notEnoughInfo = false;
                //checks to find out if actually viable:
                if (txtDescr.getText().isEmpty())
                {
                    txtDescr.setBackground(Color.YELLOW);
                    notEnoughInfo = true;
                }
                if (txtLocation.getText().isEmpty() && appointment.getRes() == null)
                {
                    txtLocation.setBackground(Color.YELLOW);
                    notEnoughInfo = true;
                }
                if (appointment.getStart().after(appointment.getEnd()))
                {
                    ftxtTEnd.setBackground(Color.YELLOW);
                    if (endUseDuration)
                        ftxtDur.setBackground(Color.YELLOW);
                    else
                        ftxtTEnd.setBackground(Color.YELLOW);
                    notEnoughInfo = true;
                }

                if (notEnoughInfo)
                    return;

                //set fields:
                appointment.setDescription(txtDescr.getText());
                if (appointment.getRes() == null)
                    appointment.setLocation(txtLocation.getText());

                //external participants:
                ArrayList<String> extPart = new ArrayList<>();
                for (int i = 0; i < mExtLst.size(); i++)
                    extPart.add((String) mExtLst.get(i));

                appointment.setExtParticipants(extPart);

                if (!isChange)
                {
                    //TODO: Patrik: ny avtale, send mail til alle extparticipants
                }
                else
                    pnlStatus.finalize();


                for (int i = 0; i < mToLst.size(); i++)
                {
                    String elem = (String) mToLst.get(i);
                    if (elem.contains("(Group)"))
                    {
                        Group g = UserManager.getInstance().getGroup(elem.substring(0, elem.lastIndexOf(" ")));
                        if (g != null)
                        {
                            ArrayList<User> usersInGroup = g.getAllUsers();
                            for (int j = 0; j < usersInGroup.size(); j++)
                                appointment.addParticipant(usersInGroup.get(i));
                        }
                    }
                    else
                    {
                        User u = UserManager.getInstance().getUserFromName(elem);
                        if (u == null)
                            System.out.println("[ArrDialog]: Couldnt find user " + elem + " in user manager.");
                        else
                            appointment.addParticipant(u);
                    }
                }

                if (!isChange)
                    CalendarManager.getInstance().makeAppointment(appointment);
                else if (fieldsHaveChanged) {
                	try {
						MailHandler mh = new MailHandler();
						Calendar cal = GregorianCalendar.getInstance();
						cal.setTime(appointment.getStart());
						String date = cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
						mh.setSubject("APPOINTMENT ON " + date + " IS CHANGED");
						mh.setContent("Something in the appointment on " + date + " has changed and needs your attention.</br>"
								+ "Please log in to the calendar system and check it out.");
						for (Participant participant : appointment.getParticipants()) {
							mh.setRecipient(participant.getaUser().getEmail());
							mh.sendMail();
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
                }
                
                if (!appointment.getExtParticipants().isEmpty()) {
                	try {
						MailHandler mh = new MailHandler();
						Calendar calS = GregorianCalendar.getInstance();
						calS.setTime(appointment.getStart());
						Calendar calE = GregorianCalendar.getInstance();
						calE.setTime(appointment.getEnd());
						
						String date = calS.get(Calendar.DATE) + "/" + calS.get(Calendar.MONTH) + "/" + calS.get(Calendar.YEAR);
						String start = null;
						if (calS.get(Calendar.MINUTE) < 10)
							start = calS.get(Calendar.HOUR_OF_DAY) + ":0" + calS.get(Calendar.MINUTE);
						else
							start = calS.get(Calendar.HOUR_OF_DAY) + ":" + calS.get(Calendar.MINUTE);
						
						String end = null;
						if (calE.get(Calendar.MINUTE) < 10)
							end = calE.get(Calendar.HOUR_OF_DAY) + ":0" + calE.get(Calendar.MINUTE);
						else
							end = calE.get(Calendar.HOUR_OF_DAY) + ":" + calE.get(Calendar.MINUTE);
						
						String location = null;
						if (appointment.getRes() == null)
							location = appointment.getLocation();
						else
							location = appointment.getRes().getRoom().getRoomID();
						
						mh.setSubject("MEETING INVITATION!");
						String content = "You have been invited to participate in a meeting on " + date + ".</br>"
								+ "Meeting details:</br>"
								+ "Start time - " + start + "</br>"
								+ "End time - " + end + "</br>"
								+ "Location - " + location;
						mh.setContent(content);
						for (String extParticipant : appointment.getExtParticipants()) {
							mh.setRecipient(extParticipant);
							mh.sendMail();
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
                }

                MainFrame.refreshAppoint();
                d.setVisible(false);
            }
        });
		
		//Create and add a button to open Room Booking dialog 
        btnBookARoom = new JButton("Book a room");
		btnBookARoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if (btnBookARoom.getText().equals("Book a room"))
                {
                    updateDateFromFields();
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                //System.out.println(CalendarPanel.currentMonth);
                                new BookRoom(d,true,"Room Booking", appointment, txtLocation, txtReservation, btnBookARoom);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                else
                {
                    RoomManager.getInstance().removeReservation(appointment.getRes());
                    appointment.setRes(null);
                    btnBookARoom.setText("Book a room");
                    txtReservation.setText("");
                    txtLocation.setText("");
                    txtLocation.setEditable(true);
                }
                fieldsHaveChanged = true;
				
			}
		});
		btnBookARoom.setBounds(180, 157, 111, 23);
		contentPanel.add(btnBookARoom);




        //status for response


		

		
		
		// Make it visible after everything is added !!!!!!
		d.setVisible(true);
	}

    private void updateDateFromFields()
    {
        Date startDate = appointment.getStart();
        startDate.setDate(cmbDay.getSelectedIndex() + 1);
        startDate.setMonth(cmbMonth.getSelectedIndex());
        startDate.setYear(cmbYear.getSelectedIndex() + 114);
        startDate.setHours(formatterGetHour((String) ftxtTStart.getValue()));
        startDate.setMinutes(formatterGetMin((String) ftxtTStart.getValue()));


        Date endDate = appointment.getEnd();
        endDate.setDate(cmbDay.getSelectedIndex() + 1);
        endDate.setMonth(cmbMonth.getSelectedIndex());
        endDate.setYear(cmbYear.getSelectedIndex() + 114);
        if (endUseDuration)
        {
            endDate.setHours(formatterGetHour((String) ftxtTStart.getValue()) + formatterGetHour((String) ftxtDur.getValue()));
            endDate.setMinutes(formatterGetMin((String) ftxtTStart.getValue()) + formatterGetMin((String) ftxtDur.getValue()));
        }
        else
        {
            endDate.setHours(formatterGetHour((String) ftxtTEnd.getValue()));
            endDate.setMinutes(formatterGetMin((String) ftxtTEnd.getValue()));
        }

    }

    /**
     * Function called each time the time is changed, and a room is booked.
     * Changes the time of the booking, and tries to reserve the room at the new time.
     */
    private void autoUpdateBooking()
    {

        //update number of days in month:
        int maxDays = new GregorianCalendar(cmbYear.getSelectedIndex() + 2014, cmbMonth.getSelectedIndex(), 1)
                .getActualMaximum(Calendar.DAY_OF_MONTH);

        if (cmbDay.getItemCount() < maxDays) // not enough days
        {
            for (int i = cmbDay.getItemCount(); i < maxDays; i++)
                cmbDay.addItem(i + 1);
        }
        else // too many days
        {
            for (int i = cmbDay.getItemCount() - 1; i >= maxDays; i--)
                cmbDay.removeItemAt(i);

        }

        updateDateFromFields();

        if (appointment.getRes() == null)
            return;

        RoomManager.getInstance().removeReservation(appointment.getRes());
        //if room is busy:
        if (RoomManager.getInstance().generateAvailableRooms(appointment.getStart(), appointment.getEnd())
                .contains(appointment.getRes().getRoom()))
        {
            appointment.setRes(RoomManager.getInstance()
                    .reserveRoom(appointment.getRes().getRoom(), appointment.getStart(), appointment.getEnd()));
        }
        else
        {
            appointment.setRes(null);
            btnBookARoom.setText("Book a room");
            txtReservation.setText("");
            txtLocation.setText("");
            txtLocation.setEditable(true);
        }
    }
}
