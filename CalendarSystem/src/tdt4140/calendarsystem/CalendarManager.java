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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl;
import com.sun.org.apache.xerces.internal.parsers.XMLDocumentParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        Document d = null;
		try {
			DocumentBuilder f = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			d = f.newDocument();
		
			Element root = d.createElement("appointments");
			Element e = null, ee = null;
			for (int i = 0; i < appointments.size(); i++)
			{
				Appointment appointment = appointments.get(i);
				ee = d.createElement(TAG_APPOINTMENT);
				root.appendChild(ee);

                //description
				e = d.createElement(TAG_DESC);
                e.setNodeValue(appointment.getDescription());
				ee.appendChild(e);

				//participants:
				ArrayList<Participant> participants = appointment.getParticipants();
				for (int j = 0; j < participants.size(); j++)
				{
					e = d.createElement(TAG_PARTICIPANT);

                    Element name = d.createElement(TAG_PARTICIPANT_NAME);
                    name.setNodeValue(participants.get(j).getaUser().getUsername());
					e.appendChild(name);
                    Element status = d.createElement(TAG_PARTICIPANT_STATUS);
                    status.setNodeValue(participants.get(j).getStatus());
					e.appendChild(status);

					ee.appendChild(e);
				}

				//ext participants:
				ArrayList<String> extParticipants = appointment.getExtParticipants();
				for (int j = 0; j < extParticipants.size(); j++)
				{
					e = d.createElement(TAG_EXTPARTICIPANT);
					e.setNodeValue(extParticipants.get(i));
					ee.appendChild(e);
				}

				//location:
				if (appointment.getRes() == null)
				{
					e = d.createElement(TAG_LOCATION);
					e.setNodeValue(appointment.getLocation());
					ee.appendChild(e);
				}
				else//reservation
				{
					e = d.createElement(TAG_RESERVATION);
					e.setNodeValue(Integer.toString(appointment.getRes().getReservationID()));
					ee.appendChild(e);
				}
				//start time
				e = d.createElement(TAG_START_DATE);
				e.setNodeValue(Long.toString(appointment.getStart().getTime()));
				ee.appendChild(e);
				//end time
				e = d.createElement(TAG_END_DATE);
				e.setNodeValue(Long.toString(appointment.getEnd().getTime()));
				ee.appendChild(e);
			}




		} 
		catch (Exception e) {
			System.out.println("Exception writing calendar manager to file: \n" + e.getMessage());
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
                    new StreamResult(new FileOutputStream(calendarFile)));

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        //skip the string export, the above bit of code saves the file, and is much simpler.
		return "";
	}
	
	@Override
	public void parseFromXML(String XMLString)
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

                //parsing one attribute:
                for (int j = 0; j < subElements.getLength(); j++)
                {
                    Node node = subElements.item(i);

                    if (node.getNodeName().equals(TAG_DESC))
                        appointmentObj.setDescription(node.getNodeValue());
                    else if (node.getNodeName().equals(TAG_PARTICIPANT))
                    {
                        Participant p = new Participant();
                        NodeList participantAttributes = node.getChildNodes();

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
                    else if (node.getNodeName().equals(TAG_LOCATION))
                        appointmentObj.setLocation(node.getNodeValue());
                    else if (node.getNodeName().equals(TAG_RESERVATION))
                        appointmentObj.setRes(RoomManager.getInstance().getReservation(Integer.getInteger(node.getNodeValue())));
                    else if (node.getNodeName().equals(TAG_START_DATE))
                        appointmentObj.setStart(new Date(Long.getLong(node.getNodeValue())));
                    else if (node.getNodeName().equals(TAG_END_DATE))
                        appointmentObj.setEnd(new Date(Long.getLong(node.getNodeValue())));

                }

                appointmentObj.setExtParticipants(extParticipants);
                this.appointments.add(appointmentObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
