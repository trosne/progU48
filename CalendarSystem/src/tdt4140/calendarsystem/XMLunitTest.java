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

        /*userManager.addUser("Katja Kaj", "kajkaj", "jeglikerboller", "katja@kaj.com");
        userManager.addUser("Bente Bent", "benben", "jeghaterboller", "bente@bent.co.uk");
        userManager.addUser("Patrik Fridberg", "patrikfb", "1234", "bakken.patrik@gmail.com");
        User one = new User("Vegard Brattsberg", "vegbrat", "2345", "vegard@brattsberg.no");
        User two = new User("Lisa Steen", "lisasteen", "3456", "lisa@steen.com");
        userManager.addUser(one);
        userManager.addUser(two);
        
        Group _super = new Group("super");
        userManager.addGroup(_super);
        _super.addUserToGroup(one);
        Group _sub = new Group("sub");
        _super.addSubGroup(_sub);
        _sub.addUserToGroup(two);
        
        userManager.parseToXML(); */
        
        userManager.parseFromXML();
        
        for (User user : UserManager.getInstance().getUsers()) {
        	System.out.println("User: " + user.getName() + " " + user.getUsername() + " " + user.getPassword()
        			+ " " + user.getEmail());
        }
        
        for (Group group : UserManager.getInstance().getGroups()) {
        	System.out.println("Group: " + group.getName() + " " + group.getUsers().size());
        	
        	for (Group subGroup : group.getSubGroups()) {
        		System.out.println("Sub groups: " + subGroup.getName());
        	}
        }
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

        //roomManager.parseFromXML("");
        
        
    }
}
