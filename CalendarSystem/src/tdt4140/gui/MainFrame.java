package tdt4140.gui;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JDialog;

import tdt4140.calendarsystem.Appointment;
import tdt4140.calendarsystem.CalendarManager;
import tdt4140.calendarsystem.Participant;
import tdt4140.calendarsystem.RoomManager;
import tdt4140.calendarsystem.UserManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;


public class MainFrame extends JFrame {

	private static UserManager _userManager;
	private static RoomManager _roomManager;
	private static CalendarManager _calendarManager;
	
	private JPanel contentPane;
	private static CalendarPanel calendarPanel;
	static MainFrame mainFrame; 
	static JTable tblArrange;
	static DefaultTableModel mtblArrange;
    static JScrollPane stblArrange;
    static JDialog dlgArrPref;

	
	public void setUsername(String usr){
		//System.out.println(usr);
		setTitle("Username" + "-" + usr);	
	}
	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//						mainFrame = new MainFrame();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		this._userManager = UserManager.getInstance();
		this._calendarManager = CalendarManager.getInstance();
		this._roomManager = RoomManager.getInstance();
		
		//Mainframe properties
		
		setTitle("Main calendar window");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
		setResizable(false);
		
		//The basic Content Pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//Add Calendar Panel (CalendarPanel.java)
		calendarPanel = new CalendarPanel();
		calendarPanel.setLocation(209, 11);
		contentPane.add(calendarPanel);
		
		//Add new panel with for appointment table
	    JPanel ArrPanel = new JPanel(null);
		ArrPanel.setBounds(0, 340, 790, 335);
		ArrPanel.setBorder(BorderFactory.createTitledBorder("Appointments"));
		ArrPanel.setBackground(contentPane.getBackground());
		contentPane.add(ArrPanel);
		
		// New table model with non-editable cells
		mtblArrange = new DefaultTableModel(){
			public boolean isCellEditable(int rowIndex, int mColIndex){
				return false;
			}
			public Class getColumnClass(int c) {
	        	/*if (c==2)
	        		return Boolean.class;*/
	        	for (int row = 0; row < getRowCount(); row++)
	            {
	                Object o = getValueAt(row, c);

	                if (o != null)
	                {
	                    return o.getClass();
	                }
	            }

	            return String.class;
	        }
			
		};
		
		// New Table on Default Table Model = data 
		tblArrange = new JTable(mtblArrange); // New table with default model mtblArrange
	    stblArrange = new JScrollPane(tblArrange); //Table scroll
	    stblArrange.setBounds(10, 20, 770, 275);
	    
	    //Add scroller container with appointments table 
	    ArrPanel.add(stblArrange);
	    
	    //Add button+label which opens appointment dialog and it to table panel
	    JButton btnAddA = new JButton("+");
	    btnAddA.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		
	    		EventQueue.invokeLater(new Runnable() {
	    			public void run() {
	    				try {
	    					//System.out.println(CalendarPanel.currentMonth);
	    		    		new ArrDialog(mainFrame, true, "Appointment preferences");
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	    		});
	    	}
	    });
	    btnAddA.setBounds(691, 301, 89, 23);
	    ArrPanel.add(btnAddA);
	    
	    JLabel lblAddNewA = new JLabel("Add new appointment");
	    lblAddNewA.setBounds(20, 305, 119, 14);
	    ArrPanel.add(lblAddNewA);
	   
	    //Add headers to appointments table
        String[] headers = {"Date", "Start", "End", "Location", "Description", "Status", "Participant"}; //All headers
        for (int i=0; i<7; i++){
        	mtblArrange.addColumn(headers[i]);
        }
        //Set table background
        tblArrange.getParent().setBackground(tblArrange.getBackground()); 
        //No resize/reorder
        tblArrange.getTableHeader().setResizingAllowed(false);
        tblArrange.getTableHeader().setReorderingAllowed(false);
        
        mtblArrange.setColumnCount(7);
       // mtblArrange.setRowCount(6);
        //mtblArrange.setr
        
        
        
        //Set MainFrame visible
        refreshAppoint();
       // mtblArrange.ad
        setVisible(true);
        
       
        
        
	}
	
	public static void refreshAppoint(){
		for (int i = 0; i < mtblArrange.getRowCount(); i++) {
	        for (int j = 0; j < mtblArrange.getColumnCount(); j++) {
	        	mtblArrange.setValueAt(null, i, j);
	        }
	    }
				
		// Populate the appointment table
		
		String date = calendarPanel.getCurrentDate();
		//System.out.println(date);
		Calendar currCal = GregorianCalendar.getInstance();
		String[] dateStrings = date.split("/");
		currCal.set(Integer.parseInt(dateStrings[2]), Integer.parseInt(dateStrings[1]), Integer.parseInt(dateStrings[0]));

		for (Appointment appointment : _calendarManager.getAppointments()) {
			Calendar appCalS = new GregorianCalendar();
			Calendar appCalE = new GregorianCalendar();
			appCalS.setTime(appointment.getStart());
			if (appCalS.get(Calendar.YEAR) == currCal.get(Calendar.YEAR) && appCalS.get(Calendar.MONTH) == 
					currCal.get(Calendar.MONTH) && appCalS.get(Calendar.DAY_OF_MONTH) == currCal.get(Calendar.DAY_OF_MONTH)) {
				
				Vector row = new Vector();
				String appDate = appCalS.get(Calendar.DATE) + "." + (appCalS.get(Calendar.MONTH)+1) + "." + appCalS.get(Calendar.YEAR);
				String start = null;
				if (appCalS.get(Calendar.MINUTE) < 10)
					start = appCalS.get(Calendar.HOUR_OF_DAY) + ":0" + appCalS.get(Calendar.MINUTE);
				else
					start = appCalS.get(Calendar.HOUR_OF_DAY) + ":" + appCalS.get(Calendar.MINUTE);
				
				String end = null;
				if (appCalE.get(Calendar.MINUTE) < 10)
					end = appCalE.get(Calendar.HOUR_OF_DAY) + ":0" + appCalE.get(Calendar.MINUTE);
				else
					end = appCalE.get(Calendar.HOUR_OF_DAY) + ":" + appCalE.get(Calendar.MINUTE);
					
				String status = null;
				boolean isParticipant = false;
				
				for (Participant participant : appointment.getParticipants()) {
					if (participant.getaUser().isEqual(_userManager.getCurrentUser())) {
						isParticipant = true;
						status = participant.getStatus();
						break;
					}
				}
				if (!isParticipant)
					status = "";
				
				Vector<Object> rowData = new Vector<>();
				if (appointment.getRes() == null) {
					rowData.add(appDate);
					rowData.add(start);
					rowData.add(end);
					rowData.add(appointment.getLocation());
					rowData.add(appointment.getDescription());
					rowData.add(status);
					rowData.add(isParticipant);					
				}
				else {
					rowData.add(appDate);
					rowData.add(start);
					rowData.add(end);
					rowData.add(appointment.getRes().getRoom().getRoomID());
					rowData.add(appointment.getDescription());
					rowData.add(status);
					rowData.add(isParticipant);
				}
				
				mtblArrange.addRow(rowData);
			}
		}	
	}	
}
