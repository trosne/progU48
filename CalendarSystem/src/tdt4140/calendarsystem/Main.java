package tdt4140.calendarsystem;

import java.awt.EventQueue;

import tdt4140.gui.LogIn;

public class Main {

	private static RoomManager _roomManager;
	private static CalendarManager _calendarManager;
	private static UserManager _userManager;

	public static void main(String[] args) {
		_userManager = new UserManager();
		_calendarManager = new CalendarManager();
		_roomManager = new RoomManager();
		
		_userManager.parseFromXML();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn frame = new LogIn(UserManager.getInstance(), CalendarManager.getInstance(), RoomManager.getInstance());
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
