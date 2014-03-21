package tdt4140.calendarsystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.parsers.XMLDocumentParser;

public class CalendarManager extends Manager {

	private ArrayList<Appointment> appointments;

    private static final String calendarFile = "appointments.xml";

    private static final String TAG_APPOINTMENT = "appointment", TAG_DESC = "description",
            TAG_PARTICIPANT = "participant", TAG_PARTICIPANT_NAME = "username", TAG_PARTICIPANT_STATUS = "status",
            TAG_EXTPARTICIPANT = "extparticipant", TAG_LOCATION = "location", TAG_RESERVATION = "reservation",
            TAG_START_DATE = "start_date", TAG_END_DATE = "end_date";

    private static CalendarManager instance;

	public CalendarManager()
	{
		instance = this;
        appointments = new ArrayList<Appointment>();
	}

    public static CalendarManager getInstance()
    {
        return instance;
    }

	public ArrayList<Appointment> generateCalendar(User aUser)
	{
		return new ArrayList<Appointment>();
	}
	
	public void makeAppointment(Appointment anAppointment)
	{
        if (getAppointment(anAppointment.getId()) == null)
    		appointments.add(anAppointment);
	}

    public int generateUniqueID()
    {
        int id = -1;
        while(getAppointment(++id) != null);

        return id;
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
	public void parseToXML()
	{
		String result = "";
        Document d;
        Element root = null;
		try {
			DocumentBuilder f = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			d = f.newDocument();
		
			root = d.createElement("appointments");
			Element e, ee;
			for (int i = 0; i < appointments.size(); i++)
			{
				Appointment appointment = appointments.get(i);
				e = d.createElement(TAG_APPOINTMENT);
				root.appendChild(e);

                e.setAttribute(TAG_DESC, appointment.getDescription());
                e.setAttribute(TAG_START_DATE, Long.toString(appointment.getStart().getTime()));
                e.setAttribute(TAG_END_DATE, Long.toString(appointment.getEnd().getTime()));

				//participants:
				ArrayList<Participant> participants = appointment.getParticipants();
				for (int j = 0; j < participants.size(); j++)
				{
					ee = d.createElement(TAG_PARTICIPANT);

                    ee.setAttribute(TAG_PARTICIPANT_NAME, participants.get(j).getaUser().getUsername());
                    ee.setAttribute(TAG_PARTICIPANT_STATUS, participants.get(j).getStatus());

					e.appendChild(ee);
				}

				//ext participants:
				ArrayList<String> extParticipants = appointment.getExtParticipants();
				for (int j = 0; j < extParticipants.size(); j++)
				{
					ee = d.createElement(TAG_EXTPARTICIPANT);
					ee.setAttribute(TAG_EXTPARTICIPANT, extParticipants.get(i));
					e.appendChild(ee);
				}

				//location:
				if (appointment.getRes() == null)
					e.setAttribute(TAG_LOCATION, appointment.getLocation());
				else//reservation
					e.setAttribute(TAG_RESERVATION, Integer.toString(appointment.getRes().getReservationID()));

			}




		} 
		catch (Exception e) {
			System.out.println("Exception writing calendar manager to file: \n" + e.getMessage());
            e.printStackTrace();
		}

        //save the file:
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            tr.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(calendarFile)));

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
	}

    public Appointment getAppointment(String desc)
    {
        for (int i = 0; i < appointments.size(); i++)
            if (appointments.get(i).getDescription().equals(desc))
                return appointments.get(i);

        return null;
    }

    public Appointment getAppointment(int id)
    {
        for (int i = 0; i < appointments.size(); i++)
            if (appointments.get(i).getId() == id)
                return appointments.get(i);

        return null;
    }

	@Override
	public void parseFromXML()
	{
        //again, the string input output part is pretty unnecessary, and only complicates things..
        Document d;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            d = db.parse(calendarFile);
            NodeList appointments = d.getElementsByTagName("appointment");

            //parsing one appointment:
            for (int i = 0; i < appointments.getLength(); i++)
            {
                Appointment appointmentObj = new Appointment();
                NodeList subElements = appointments.item(i).getChildNodes();

                final ArrayList<String> extParticipants = new ArrayList<String>();

                NamedNodeMap attributes = appointments.item(i).getAttributes();
                // appointmentObj.setDescription(attributes.getNamedItem(TAG_DESC).);

                //parsing one attribute:
                for (int j = 0; j < attributes.getLength(); j++)
                {
                    Node node = attributes.item(j);

                    if (node.getNodeName().equals(TAG_DESC))
                        appointmentObj.setDescription(node.getNodeValue());
                    else if (node.getNodeName().equals(TAG_LOCATION))
                        appointmentObj.setLocation(node.getNodeValue());
                    else if (node.getNodeName().equals(TAG_RESERVATION))
                        appointmentObj.setRes(RoomManager.getInstance().getReservation(Integer.decode(node.getNodeValue())));
                    else if (node.getNodeName().equals(TAG_START_DATE))
                        appointmentObj.setStart(new Date(Long.decode(node.getNodeValue())));
                    else if (node.getNodeName().equals(TAG_END_DATE))
                        appointmentObj.setEnd(new Date(Long.decode(node.getNodeValue())));

                }
                for (int j = 0; j < subElements.getLength(); j++)
                {
                    Node node = subElements.item(j);
                    if (node.getNodeName().equals(TAG_PARTICIPANT))
                    {
                        Participant p = new Participant();
                        NamedNodeMap participantAttributes = node.getAttributes();

                        //get user and status:
                        for (int k = 0; k < participantAttributes.getLength(); k++)
                        {
                            if (participantAttributes.item(k).getNodeName().equals(TAG_PARTICIPANT_NAME))
                                p.setaUser(UserManager.getInstance().getUser(participantAttributes.item(k).getNodeValue()));
                            else if (participantAttributes.item(k).getNodeName().equals(TAG_PARTICIPANT_STATUS))
                                p.setStatus(participantAttributes.item(k).getNodeValue());
                        }
                        appointmentObj.addParticipant(p);
                    }
                    else if (node.getNodeName().equals(TAG_EXTPARTICIPANT))
                        extParticipants.add(node.getNodeValue());
                    }

                appointmentObj.setExtParticipants(extParticipants);
                // add if appointment doesnt exist:
                if (getAppointment(appointmentObj.getDescription()) == null)
                    this.appointments.add(appointmentObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	/**
	 * @return the appointments
	 */
	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * @param appointments the appointments to set
	 */
	public void setAppointments(ArrayList<Appointment> appointments) {
		this.appointments = appointments;
	}
	
	
}
