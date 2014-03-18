package tdt4140.calendarsystem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class RoomManager extends Manager {

	private ArrayList<MeetingRoom> rooms;
	private ArrayList<Reservation> bookings;

    private static final String TAG_ROOM = "room", TAG_RESERVATION = "reservation", TAG_ID = "id",
            TAG_START_DATE = "start_date", TAG_END_DATE = "end_date", TAG_CAPACITY = "capacity", TAG_ROOM_ID = "room_id";

    private static final String roomFile = "rooms.xml";

    private static RoomManager instance;
	
	public RoomManager()
	{
		instance = this;
	}

    /**
     * Convenience function to get the working instance of the room manager.
     * @return the working instance of the manager
     */
    public static RoomManager getInstance() {return instance;}
	
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
     * Get the reservation with the given id
     * @param id the id of the desired booking
     * @return the booking with the matching id, or null if not found
     */
    public Reservation getReservation(int id)
    {
        for (int i = 0; i < bookings.size(); i++)
            if (bookings.get(i).getReservationID() == id)
                return bookings.get(i);
        return null;
    }

    /**
     * Get the room with the given id
     * @param id the id of the desired booking, as a string
     * @return the booking with the matching id, or null if not found
     */
    public MeetingRoom getRoom(String id)
    {
        for (int i = 0; i < rooms.size(); i++)
            if (rooms.get(i).getRoomID().equals(id))
                return rooms.get(i);
        return null;
    }

	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(ArrayList<Reservation> bookings) {
		this.bookings = bookings;
	}

	public boolean reserveRoom(MeetingRoom room, Date start, Date end)
	{
		if (checkAvailability(room, start, end)){
			int resID = bookings.get(bookings.size() - 1).getReservationID() + 1;
			Reservation res = new Reservation(room, resID, start, end);
			bookings.add(res);
			return true;
		}
		return false;
	}
	
	public void removeReservation(Reservation res)
	{
		bookings.remove(res);
	}
	
	public ArrayList<MeetingRoom> generateAvailableRooms(Date start, Date end)
	{
		ArrayList<MeetingRoom> availableRooms = new ArrayList<MeetingRoom>();
		availableRooms.addAll(rooms);
		for (int i = 0; i < bookings.size(); i++){
			if (checkAvailability(bookings.get(i).getRoom(), start, end) == false){
				String bookedRoom = bookings.get(i).getRoom().getRoomID();
				for (int j = 0; j < availableRooms.size(); j++){
					if (availableRooms.get(j).getRoomID().equals(bookedRoom)){
						availableRooms.remove(j);
					}
				}
			}
		}
		
		return availableRooms;
	}
	
	private boolean checkAvailability(MeetingRoom room, Date start, Date end)
	{
		for (int i = 0; i < bookings.size(); i++){
			if (room.isEqual(bookings.get(i).getRoom())){
				if (start.compareTo(bookings.get(i).getEnd()) >= 0 || end.compareTo(bookings.get(i).getStart()) <= 0){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String parseToXML() {
        String result = "";
        Document d = null;
        try {
            DocumentBuilder f = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            d = f.newDocument();

            Element root = d.createElement("roomsAndRes");
            Element e = null, ee = null;
            for (int i = 0; i < rooms.size(); i++)
            {
                MeetingRoom room = rooms.get(i);
                ee = d.createElement(TAG_ROOM);
                root.appendChild(ee);

                //id
                e = d.createElement(TAG_ID);
                e.setNodeValue(room.getRoomID());
                ee.appendChild(e);
                //capacity
                e = d.createElement(TAG_CAPACITY);
                e.setNodeValue(Integer.toString(room.getCap()));
                ee.appendChild(e);
            }

            for (int i = 0; i < rooms.size(); i++)
            {
                Reservation reservation = bookings.get(i);
                ee = d.createElement(TAG_RESERVATION);
                root.appendChild(ee);

                //id
                e = d.createElement(TAG_ID);
                e.setNodeValue(Integer.toString(reservation.getReservationID()));
                ee.appendChild(e);
                //room id
                e = d.createElement(TAG_ROOM_ID);
                e.setNodeValue(reservation.getRoom().getRoomID());
                ee.appendChild(e);
                //start date
                e = d.createElement(TAG_START_DATE);
                e.setNodeValue(Long.toString(reservation.getStart().getTime()));
                ee.appendChild(e);
                //end date
                e = d.createElement(TAG_END_DATE);
                e.setNodeValue(Long.toString(reservation.getEnd().getTime()));
                ee.appendChild(e);
            }
        }
        catch (Exception e) {
            System.out.println("Exception writing room manager to file: \n" + e.getMessage());
        }

        //save the file:
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            tr.transform(new DOMSource(d),
                    new StreamResult(new FileOutputStream(roomFile)));

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return null;
	}

	@Override
	public void parseFromXML(String XMLString) {
        //again, the string input output part is pretty unnecessary, and only complicates things..
        Document d;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            d = db.parse(roomFile);
            NodeList roomsAndRes = d.getFirstChild().getChildNodes();

            //parsing one appointment:
            for (int i = 0; i < roomsAndRes.getLength(); i++)
            {
                Node subNode = roomsAndRes.item(i);
                if (subNode.getNodeName().equals(TAG_ROOM))
                {
                    MeetingRoom room  = new MeetingRoom();
                    NodeList attributes = subNode.getChildNodes();
                    for (int j = 0; j < attributes.getLength(); j++)
                    {
                        if (attributes.item(j).getNodeName().equals(TAG_ID))
                            room.setRoomID(attributes.item(j).getNodeValue());
                        else if (attributes.item(j).getNodeName().equals(TAG_CAPACITY))
                            room.setCap(Integer.getInteger(attributes.item(j).getNodeValue()));
                    }
                    rooms.add(room);
                }
                else if (subNode.getNodeName().equals(TAG_RESERVATION))
                {
                    Reservation res = new Reservation();
                    NodeList attributes = subNode.getChildNodes();
                    for (int j = 0; j < attributes.getLength(); j++)
                    {
                        if (attributes.item(j).getNodeName().equals(TAG_ROOM_ID))
                            res.setRoom(getRoom(attributes.item(j).getNodeValue()));
                        else if (attributes.item(j).getNodeName().equals(TAG_ID))
                            res.setReservationID(Integer.getInteger(attributes.item(j).getNodeValue()));
                        else if (attributes.item(j).getNodeName().equals(TAG_START_DATE))
                            res.setStart(new Date(Long.getLong(attributes.item(j).getNodeValue())));
                        else if (attributes.item(j).getNodeName().equals(TAG_END_DATE))
                            res.setEnd(new Date(Long.getLong(attributes.item(j).getNodeValue())));
                    }
                    bookings.add(res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
