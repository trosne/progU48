package tdt4140.calendarsystem;

import java.util.ArrayList;
import java.util.Date;

public class Appointment {

	private String description;
	private ArrayList<Participant> participants;
	private ArrayList<String> extParticipants; //external participants
	private String location;
	private Date start;
	private Date end;
	private Reservation res; //includes room if a room is booked for this meeting
	
	public Appointment()
	{
		
	}
	
	public void setStatus(User aUser, String status)
	{
		
	}
	
	public void addParticipant(Participant x)
	{
		
	}
	
	public void removeParticipant(Participant x)
	{
		
	}
	
	public void editAppointment(Date start, Date end, String location, String description)
	{
		
	}
}
