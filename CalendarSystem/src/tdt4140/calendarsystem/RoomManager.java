package tdt4140.calendarsystem;

import java.util.ArrayList;
import java.util.Date;

public class RoomManager extends Manager {

	private ArrayList<MeetingRoom> rooms;
	private ArrayList<Reservation> bookings;
	
	public RoomManager()
	{
		
	}
	
	public void reserveRoom(MeetingRoom room, Date start, Date end)
	{
		
	}
	
	public void removeReservation(Reservation res)
	{
		
	}
	
	public ArrayList generateAvailableRooms(Date start, Date end)
	{
		return new ArrayList<MeetingRoom>();
	}
	
	private boolean checkAvailability(MeetingRoom room, Date start, Date end)
	{
		return false;
	}

	@Override
	public String parseToXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parseFromXML(String XMLString) {
		// TODO Auto-generated method stub
		
	}
}
