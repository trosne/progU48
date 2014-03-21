package tdt4140.gui;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable tblCalendar;
	static JComboBox cmbYear;
	static DefaultTableModel mtblCalendar;
	static JScrollPane stblCalendar;
	static int realYear, realMonth, realDay, currentYear, currentMonth;
	private static String currentDate;

	public CalendarPanel() {

		setLayout(null);
		setLookAndFeel();
		createControls();
		setBorder();
		registerActionListeners();
		addControls();
		setBounds();
		setDate();
		addHeaders();
		setBackground();
		setTableProperties();
		populateTable();
		refreshCalendar(realMonth, realYear);

	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}
	}

	private void createControls() {
		lblMonth = new JLabel("January");
		lblYear = new JLabel("Change year:");
		cmbYear = new JComboBox();
		btnPrev = new JButton("<");
		btnNext = new JButton(">");
		mtblCalendar = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}

		};
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);
	}

	private void setBorder() {
		this.setBorder(BorderFactory.createTitledBorder("Calendar"));
	}

	private void registerActionListeners() {
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		// New window on cell double click - action listener
		tblCalendar.addMouseListener(new mouseClickedOnCell());
	}

	private void addControls() {
		this.add(lblMonth);
		this.add(lblYear);
		this.add(cmbYear);
		this.add(btnPrev);
		this.add(btnNext);
		this.add(stblCalendar);
	}

	private void setBounds() {
		this.setBounds(0, 0, 320, 335);
		lblMonth.setBounds(160 - lblMonth.getPreferredSize().width / 2, 25,
				100, 25);
		lblYear.setBounds(10, 305, 80, 20);
		cmbYear.setBounds(230, 305, 80, 20);
		btnPrev.setBounds(10, 25, 50, 25);
		btnNext.setBounds(260, 25, 50, 25);
		stblCalendar.setBounds(10, 50, 300, 250);
	}

	private void setDate() {
		GregorianCalendar cal = new GregorianCalendar(); // Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); // Get day
		realMonth = cal.get(GregorianCalendar.MONTH); // Get month
		realYear = cal.get(GregorianCalendar.YEAR); // Get year
		currentMonth = realMonth; // Match month and year
		currentYear = realYear;
		currentDate = cal.get(GregorianCalendar.DATE) + "/" + (currentMonth+1) + "/"
				+ currentYear;
	}

	private void addHeaders() {
		String[] headers = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		for (int i = 0; i < 7; i++) {
			mtblCalendar.addColumn(headers[i]);
		}
	}

	private void setBackground() {
		tblCalendar.getParent().setBackground(tblCalendar.getBackground());
	}

	private void setTableProperties() {
		// No resize/reorder
		tblCalendar.getTableHeader().setResizingAllowed(false);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		// Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setCellSelectionEnabled(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Set row/column count
		tblCalendar.setRowHeight(38);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);
	}

	private void populateTable() {
		for (int i = realYear - 100; i <= realYear + 100; i++) {
			cmbYear.addItem(String.valueOf(i));
		}
	}

	private static void refreshCalendar(int month, int year) {
		// Variables
		String[] months = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		int nod, som; // Number Of Days, Start Of Month

		// Allow/disallow buttons
		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= realYear - 10) {
			btnPrev.setEnabled(false);
		} // Too early
		if (month == 11 && year >= realYear + 100) {
			btnNext.setEnabled(false);
		} // Too late
		lblMonth.setText(months[month]); // Refresh the month label (at the top)

		// Re-align label with calendar
		lblMonth.setBounds(160 - lblMonth.getPreferredSize().width / 2, 25,
				180, 25);
		// Select the correct year in the combo box
		cmbYear.setSelectedItem(String.valueOf(year));

		// Clear table
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				mtblCalendar.setValueAt(null, i, j);
			}
		}

		// Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		if (som == GregorianCalendar.SUNDAY) {
			som = 7;
		} else {
			som--;
		}

		// Draw calendar
		for (int i = 1; i <= nod; i++) {
			int row = new Integer((i + som - 2) / 7);
			int column = (i + som - 2) % 7;
			mtblCalendar.setValueAt(i, row, column);
		}

		// Apply renderers
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0),
				new tblCalendarRenderer());
	}

	public String getCurrentDate() {
		return currentDate;
	}
	public int getMonth() {
		return (currentMonth+1);
	}

	static class tblCalendarRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean selected, boolean focused, int row,
				int column) {

			super.getTableCellRendererComponent(table, value, selected,
					focused, row, column);
			
			if ((column == 5) || (column == 6)) { // Week-end
				setBackground(new Color(255, 220, 220));
			} else { // Week
				setBackground(new Color(255, 255, 255));
			}

			if (value != null) {
				
				
				if ((Integer.parseInt(value.toString()) == realDay)
						&& (currentMonth == realMonth)
						&& (currentYear == realYear)) { // Today
					setBackground(new Color(220, 220, 255));
				}
				
				for(String s : MainFrame.rowsExist()) {
					
					if ( (Integer.parseInt(value.toString())) == (Integer.parseInt(s.toString()))){
						setBackground(new Color(233, 32, 32));
					}
		
				}
					
			}
			if (selected)
				setBackground(new Color(255, 228, 181));


			setBorder(null);
			setForeground(Color.black);
			return this;
		}
	}

	static class btnPrev_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (currentMonth == 0) { // Back one year
				currentMonth = 11;
				currentYear -= 1;
			} else { // Back one month
				currentMonth -= 1;
			}
			
			refreshCalendar(currentMonth, currentYear);
		}
	}

	static class btnNext_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (currentMonth == 11) { // Forward one year
				currentMonth = 0;
				currentYear += 1;
			} else { // Forward one month
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}

	static class cmbYear_Action implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (cmbYear.getSelectedItem() != null) {
				String b = cmbYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(b); // Get the numeric value
				refreshCalendar(currentMonth, currentYear); // Refresh
			}
		}
	}

	static class mouseClickedOnCell extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 1) {
				
				
				JTable target = (JTable) e.getSource();
				int row = tblCalendar.getSelectedRow();
				int column = tblCalendar.getSelectedColumn();

				if ((tblCalendar.getValueAt(row, column)) != null) {
					String month = "0";

					switch (lblMonth.getText().toLowerCase()) {
					case "january":
						month = "1";
						break;
					case "february":
						month = "2";
						break;
					case "march":
						month = "3";
						break;
					case "april":
						month = "4";
						break;
					case "may":
						month = "5";
						break;
					case "june":
						month = "6";
						break;
					case "july":
						month = "7";
						break;
					case "august":
						month = "8";
						break;
					case "september":
						month = "9";
						break;
					case "october":
						month = "10";
						break;
					case "november":
						month = "11";
						break;
					case "december":
						month = "12";
						break;
					default:
						month = "0";
						break;
					}
					currentDate = tblCalendar.getValueAt(row, column) + "/" + month + "/"
							+ currentYear;
					//System.out.println(currentDate);

				}
				MainFrame.refreshAppoint();
				refreshCalendar(currentMonth, currentYear);
			}

		}

	}
}
