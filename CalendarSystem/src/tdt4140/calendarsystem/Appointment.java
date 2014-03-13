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
	private Reservation res; //includes room and time when a room is booked for this meeting
	
	/**
	 * Default constructor
	 */
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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the participants
	 */
	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}

	/**
	 * @return the extParticipants
	 */
	public ArrayList<String> getExtParticipants() {
		return extParticipants;
	}

	/**
	 * @param extParticipants the extParticipants to set
	 */
	public void setExtParticipants(ArrayList<String> extParticipants) {
		this.extParticipants = extParticipants;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the start time
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @param start the start time to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return the end time
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end time to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * @return the reservation
	 */
	public Reservation getRes() {
		return res;
	}

	/**
	 * @param res the reservation to set
	 */
	public void setRes(Reservation res) {
		this.res = res;
	}
	
	
}
