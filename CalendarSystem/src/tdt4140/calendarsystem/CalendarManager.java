package tdt4140.calendarsystem;

import java.util.ArrayList;

public class CalendarManager extends Manager {

	private ArrayList<Appointment> appointments;
	
	public CalendarManager()
	{
		
	}
	
	public ArrayList generateCalendar(User aUser)
	{
		return new ArrayList<Appointment>();
	}
	
	public void makeAppointment(Appointment anAppointment)
	{
		appointments.add(anAppointment);
	}
	
	/**
	 * Removes an appointment from the calendar
	 * @param anAppointment
	 * @return the number of appointments removed, -1 if it does not exist
	 */
	public int removeAppointment(Appointment anAppointment)
	{
		if (appointments.size() > 0) {
			int i = appointments.indexOf(anAppointment);
			
			if (i != -1) {
				appointments.remove(i);
				return 1;
			}
		}
		return -1;
	}
	
	@Override
	public String parseToXML(ArrayList<Object> objects)
	{
		return "";
	}
	
	@Override
	public void parseFromXML(String XMLString)
	{
		
	}
}
