package tdt4140.calendarsystem;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Appointment {

	private String description;
	private ArrayList<Participant> participants;
	private ArrayList<String> extParticipants; //external participants
	private String location;
	private Date start;
	private Date end;
	private Reservation res; //includes room and time when a room is booked for this meeting

    private int id;
	
	/**
	 * Default constructor
	 */
	public Appointment()
	{
		participants = new ArrayList<Participant>();
        extParticipants = new ArrayList<String>();
        id = CalendarManager.getInstance().generateUniqueID();
	}
	
	/**
	 * Constructor setting the location, start and end time, and description. Used for external meetings
	 */
	public Appointment(String description, String location, Date start, Date end) {
        participants = new ArrayList<Participant>();
        extParticipants = new ArrayList<String>();
		this.description = description;
		this.location = location;
		this.start = start;
		this.end = end;
        this.id = CalendarManager.getInstance().generateUniqueID();
	}
	
	/**
	 * Method sets the status of an internal participant
	 * @param aUser User whos status should be changed
	 * @param status Status to set
	 */
	public void setStatus(User aUser, String status)
	{
		for (int i = 0; i < participants.size(); i++) {
			if (participants.get(i).getaUser().getUsername().equals(aUser.getUsername())) {
				if (participants.get(i).getStatus().equals(Participant.STATUS_DECLINED) && status.equals(Participant.STATUS_DECLINED)) {
					try {
						MailHandler mh = new MailHandler();
						Calendar cal = GregorianCalendar.getInstance();
						cal.setTime(this.start);
						String date = cal.get(Calendar.DATE) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
						mh.setSubject("STATUS CHANGE FOR APPOINTMENT ON " + date);
						mh.setContent("Someone has declined your meeting on " + date + ":</br></br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;" + aUser.getName());
						for (Participant participant : participants) {
							if (!participant.getaUser().isEqual(aUser)) {
								mh.setRecipient(participant.getaUser().getEmail());
								mh.sendMail();
							}
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				participants.get(i).setStatus(status);
                break;
			}
		}
	}
	
	/**
	 * Adds a participant to the calendar appointment
	 * @param x The user to add
	 */
	public void addParticipant(User x)
	{
        //first check to avoid duplicates
        if (getParticipant(x) == null)
        {
            Participant p = new Participant(x);

            if (participants.size() == 0)
                p.setStatus(Participant.STATUS_CREATOR);

            participants.add(p);
        }
	}

    /**
     * Old version, adds a participant to the calendar appointment.
     * @param x The participant to add
     */
    public void addParticipant(Participant x)
    {
        if (participants.size() == 0)
            x.setStatus(Participant.STATUS_CREATOR);
        participants.add(x);
    }
	
	/**
	 * Removes a participant from the calendar appointment
	 * @param x The participant to remove
	 * @return true if successful, false if participant does not exist
	 */
	public boolean removeParticipant(Participant x)
	{
		for (int i = 0; i < participants.size(); i++) {
			if (participants.get(i).isEqual(x)) {
				participants.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Edits an appointment
	 * @param start Start time
	 * @param end End time
	 * @param location New location
	 * @param description New description
	 */
	public void editAppointment(Date start, Date end, String location, String description)
	{
		this.start = start;
		this.end = end;
		this.location = location;
		this.description = description;
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

    public Participant getParticipant(User user)
    {
        for (int i = 0; i < participants.size(); i++)
            if (participants.get(i).getaUser() == user)
                return participants.get(i);

        return null;
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

    /**
     * Function to automatically create a reservation for the appointment.
     * @return true if success, false if no rooms are available
     */
    public boolean createRes()
    {
        RoomManager rooms = RoomManager.getInstance();
        ArrayList<MeetingRoom> availableRooms = rooms.generateAvailableRooms(start, end);

        MeetingRoom bestFit = null;
        for (int i = 0; i < availableRooms.size(); i++)
        {
            MeetingRoom temp = availableRooms.get(i);

            if ((bestFit == null || temp.getCap() < bestFit.getCap()) && temp.getCap() >= participants.size())
                bestFit = temp;
        }

        if (bestFit != null)
        {
            res = rooms.reserveRoom(bestFit, start, end);
            return true;
        }
        else
            return false;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
