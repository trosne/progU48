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
import tdt4140.calendarsystem.RoomManager;
import tdt4140.calendarsystem.UserManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
	
		System.out.println(usr);
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
	public MainFrame(UserManager uM, CalendarManager cM, RoomManager rM) {
		
		this._userManager = uM;
		this._calendarManager = cM;
		this._roomManager = rM;
		
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
	    		    		new ArrDialog(mainFrame, true, "Appointment preferences", null);
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
        String[] headers = {"Start", "End", "Location", "Description", "Participant"}; //All headers
        for (int i=0; i<5; i++){
        	mtblArrange.addColumn(headers[i]);
        }
        //Set table background
        tblArrange.getParent().setBackground(tblArrange.getBackground()); 
        //No resize/reorder
        tblArrange.getTableHeader().setResizingAllowed(false);
        tblArrange.getTableHeader().setReorderingAllowed(false);
        
        mtblArrange.setColumnCount(5);
       // mtblArrange.setRowCount(6);
        //mtblArrange.setr
        
        
        
        //Set MainFrame visible
        refreshAppoint();
       // mtblArrange.ad
        setVisible(true);
        
       
        
        
	}
	
	private static void refreshAppoint(){
		for (int i = 0; i < mtblArrange.getRowCount(); i++) {
	        for (int j = 0; j < mtblArrange.getColumnCount(); j++) {
	        	mtblArrange.setValueAt(null, i, j);
	        }
	    }
				
		// Populate the appointment table
		
		String date = calendarPanel.getCurrentDate();
		Calendar currCal = new GregorianCalendar();
		Date current = null;
		try {
			current = new SimpleDateFormat("dd.MM.YYYY").parse(date);
			currCal.setTime(current);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		for (Appointment appointment : _calendarManager.getAppointments()) {
			Calendar appCal = new GregorianCalendar();
			appCal.setTime(appointment.getStart());
			if (appCal.get(Calendar.YEAR) == currCal.get(Calendar.YEAR) && appCal.get(Calendar.MONTH) == 
					currCal.get(Calendar.MONTH) && appCal.get(Calendar.DAY_OF_MONTH) == currCal.get(Calendar.DAY_OF_MONTH)) {
				Vector row = new Vector();
				Object[][] rowData = {{appointment.getStart()}};
			}
		}
		Vector as = new Vector();
		Object[][] rowData = {{"Hello", "World",true}};
		as.add("Vector Hello");
		as.add("Vector World");
		as.add(true);
		

		 for (int j = 0; j< 3; j++){
			 mtblArrange.addRow(as);
			 //mtblArrange.setValueAt(i+j, i, j);
		    	}
		   
		    
		
		
	}
	
	
	
	
	
	
}
