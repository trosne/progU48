package tdt4140.calendarsystem;

public class MeetingRoom {

	private String roomID;
	private int cap; //capacity of room
	
	public MeetingRoom()
	{
		
	}

	/**
	 * @return the roomID
	 */
	public String getRoomID() {
		return roomID;
	}

	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	/**
	 * @return the cap
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap the cap to set
	 */
	public void setCap(int cap) {
		this.cap = cap;
	}
	
}
