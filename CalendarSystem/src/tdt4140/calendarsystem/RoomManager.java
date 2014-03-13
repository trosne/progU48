package tdt4140.calendarsystem;

import java.util.ArrayList;
import java.util.Date;

public class RoomManager extends Manager {

	private ArrayList<MeetingRoom> rooms;
	private ArrayList<Reservation> bookings;
	
	public RoomManager()
	{
		
	}
	
	/**
	 * @return the rooms
	 */
	public ArrayList<MeetingRoom> getRooms() {
		return rooms;
	}

	/**
	 * @param rooms the rooms to set
	 */
	public void setRooms(ArrayList<MeetingRoom> rooms) {
		this.rooms = rooms;
	}

	/**
	 * @return the bookings
	 */
	public ArrayList<Reservation> getBookings() {
		return bookings;
	}

	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(ArrayList<Reservation> bookings) {
		this.bookings = bookings;
	}

	public boolean reserveRoom(MeetingRoom room, Date start, Date end)
	{
		return false;
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
		for (int i = 0; i < bookings.size(); i++){
			if (room.isEqual(bookings.get(i).getRoom())){
				if (start.compareTo(bookings.get(i).getEnd()) >= 0 || end.compareTo(bookings.get(i).getStart()) <= 0){
					return false;
				}
			}
		}
		return true;
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
