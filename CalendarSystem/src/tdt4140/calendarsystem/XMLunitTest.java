package tdt4140.calendarsystem;

import java.util.Date;

/**
 * Created by Trond on 18.03.14.
 */
public class XMLunitTest {

    private static RoomManager roomManager;
    private static CalendarManager calendarManager;
    private static UserManager userManager;



    public static void main(String [] args)
    {
        roomManager = new RoomManager();
        calendarManager = new CalendarManager();
        userManager = new UserManager();

        userManager.addUser("Katja Kaj", "kajkaj", "jeglikerboller", "katja@kaj.com");
        userManager.addUser("Bente bent", "benben", "jeghaterboller", "bente@bent.co.uk");
/*
        MeetingRoom room = new MeetingRoom();
        room.setCap(10);
        room.setRoomID("awesome room");
        roomManager.getRooms().add(room);
        room = new MeetingRoom();
        room.setCap(4);
        room.setRoomID("shitty room");
        roomManager.getRooms().add(room);


        Appointment a = new Appointment("bollelunch", "taket", new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 1000*60*60));
        Participant p = new Participant();
        p.setaUser(userManager.getUser("kajkaj"));
        a.addParticipant(p);
        a.setRes(roomManager.reserveRoom(roomManager.getRooms().get(0), a.getStart(), a.getEnd()));
        a.setRes(roomManager.reserveRoom(roomManager.getRooms().get(1), a.getStart(), a.getEnd()));
        calendarManager.makeAppointment(a);

        roomManager.parseToXML();
        */

        roomManager.parseFromXML("");
    }
}
