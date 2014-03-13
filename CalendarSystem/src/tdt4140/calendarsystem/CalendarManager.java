package tdt4140.calendarsystem;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CalendarManager extends Manager {

	private ArrayList<Appointment> appointments;
	
	public CalendarManager()
	{
		
	}
	
	public ArrayList<Appointment> generateCalendar(User aUser)
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
	public String parseToXML()
	{
		String result = "";
		try {
			DocumentBuilder f = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = f.newDocument();
		
			Element root = d.createElement("appointments");
			Element e = null, ee = null;
			for (int i = 0; i < appointments.size(); i++)
			{
				Appointment appointment = appointments.get(i);
				
				e = d.createElement("description");
				e.appendChild(d.createTextNode(appointment.getDescription()));
				root.appendChild(e);

				//participants:
				ArrayList<Participant> participants = appointment.getParticipants();
				for (int j = 0; j < participants.size(); j++)
				{
					e = d.createElement("participant");
					e.appendChild(d.createTextNode(participants.get(j).getaUser().getUsername()));
					root.appendChild(e);
				}
				//ext participants:
				ArrayList<String> extParticipants = appointment.getExtParticipants();
				for (int j = 0; j < extParticipants.size(); j++)
				{
					e = d.createElement("participant");
					e.appendChild(d.createTextNode(extParticipants.get(i)));
					root.appendChild(e);
				}
				//location:
				if (appointment.getRes() == null)
				{
					e = d.createElement("location");
					e.appendChild(d.createTextNode(appointment.getLocation()));
					root.appendChild(e);
				}
				else//reservation
				{
					e = d.createElement("reservation");
					e.appendChild(d.createTextNode(Integer.toString(appointment.getRes().getReservationID())));
					root.appendChild(e);
				}
				//start time
				e = d.createElement("start_date");
				e.appendChild(d.createTextNode(Long.toString(appointment.getStart().getTime())));
				root.appendChild(e);
				//end time
				e = d.createElement("end_date");
				e.appendChild(d.createTextNode(Long.toString(appointment.getEnd().getTime())));
				root.appendChild(e);
			}
		} catch (Exception e)
		{
			
		}
			return "";
	}
	
	@Override
	public void parseFromXML(String XMLString)
	{
		
	}
}
