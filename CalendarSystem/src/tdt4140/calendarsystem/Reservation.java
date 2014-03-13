package tdt4140.calendarsystem;

import java.util.Date;

public class Reservation {
	
	private MeetingRoom room;
	Date start;
	Date end;
	
	/**
	 * Default constructor
	 */
	public Reservation() {
		
	}
	
	public Reservation(MeetingRoom room, Date start, Date end)
	{
		this.room = room;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * Checks equality of two Reservation objects. They are equal if room, start and end are equal
	 * @param res Reservation to compare to
	 * @return true if equal, false if not
	 */
	public boolean isEqual(Reservation res) {
		if (this.room.isEqual(res.getRoom()) && this.start.compareTo(res.getStart()) == 0 && this.end.compareTo(res.getEnd()) == 0)
			return true;
		
		return false;
	}

	/**
	 * @return the room
	 */
	public MeetingRoom getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(MeetingRoom room) {
		this.room = room;
	}

	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}	
}
