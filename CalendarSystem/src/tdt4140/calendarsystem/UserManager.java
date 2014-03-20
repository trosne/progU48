<<<<<<< HEAD
package tdt4140.calendarsystem;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserManager extends Manager {

	private ArrayList<User> users;
	private ArrayList<Group> groups;
	
	private static final String TAG_GROUP = "group", TAG_USER = "user", TAG_NAME = "name",
			TAG_USERNAME = "username", TAG_PW = "password", TAG_EMAIL = "email";

    private static UserManager instance;

	/**
	 * Default constructor
	 */
	public UserManager() {
        instance = this;
	}

    /**
     * An easy way of getting the user manager singleton object
     * @return the working instance of the user manager
     */
    public static UserManager getInstance()
    {
        return instance;
    }


	/**
	 * Adds a user to the user list
	 * 
	 * @param name
	 * @param username
	 * @param pw
	 * @param email
	 * @return true if successful, false if not
	 */
	public boolean addUser(String name, String username, String pw, String email) {
		User temp = new User(name, username, pw, email);

		// check user uniqueness and that password is not empty
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).isEqual(temp))
				return false;
		}
		if (pw == null)
			return false;

		// add user to the list of users
		users.add(temp);

		return true;
	}

	public boolean login(String username, String password) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(username)) {
				if (users.get(i).getPassword().equals(password))
					return true;
				else
					return false;
			}
		}

		return false;
	}

    /**
     * A function to get a user from the list based on username, used to add users to appointments and so on.
     * @param username the username to find
     * @return the user object matching the username, or null if not found.
     */
    public User getUser(String username)
    {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getUsername().equals(username))
                return users.get(i);

        return null;
    }


	@Override
	public void parseFromXML(String XMLString) {
		
	}

	@Override
	public String parseToXML() {
		String result = "";
		try {
			DocumentBuilder f = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document d = f.newDocument();

			Element root = d.createElement("group");
			Element e = null, ee = null;
			
			//add user elements
			for (int i = 0; i < users.size(); i++) {
				User aUser = users.get(i);
				ee = d.createElement("user");
				root.appendChild(ee);
				
				//name
				e = d.createElement("name");
				e.appendChild(d.createTextNode(aUser.getName()));
				ee.appendChild(e);

				//username
				e = d.createElement("username");
				e.appendChild(d.createTextNode(aUser.getUsername()));
				ee.appendChild(e);
				
				//password
				e = d.createElement("password");
				e.appendChild(d.createTextNode(aUser.getPassword()));
				ee.appendChild(e);
				
				//email
				e = d.createElement("email");
				e.appendChild(d.createTextNode(aUser.getEmail()));
				ee.appendChild(e);
			}
			
			//add group elements
			for (int i = 0; i < groups.size(); i++) {
				Group aGroup = groups.get(i);
				ee = d.createElement("group");
				root.appendChild(ee);
				
				//name
				e = d.createElement("name");
				e.appendChild(d.createTextNode(aGroup.getName()));
				ee.appendChild(e);
				
				//users
				for (int j = 0; j < aGroup.getUsers().size(); j++) {
					User aUser = aGroup.getUsers().get(j);
					
					e = d.createElement("user");
					e.appendChild(d.createTextNode(aUser.getUsername()));
					ee.appendChild(e);
				}
				
				//subgroups
				for (int j = 0; j < aGroup.getSubGroups().size(); j++) {
					Group b = aGroup.getSubGroups().get(j);
					
					e = d.createElement("group");
					ee.appendChild(e);
					
					Element sub = null;
					sub = d.createElement("name");
					sub.appendChild(d.createTextNode(b.getName()));
					e.appendChild(sub);
					
					//users in subgroup
					for (int k = 0; k < aGroup.getSubGroups().get(j).getUsers().size(); k++) {
						User c = aGroup.getSubGroups().get(j).getUsers().get(k);
						
						sub = d.createElement("user");
						sub.appendChild(d.createTextNode(c.getUsername()));
						e.appendChild(sub);
					}
				}
			}
		} 
		catch (Exception e) {

		}
		
		return "";
	}
}
=======
package tdt4140.calendarsystem;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserManager extends Manager {

	private final ArrayList<User> users = new ArrayList<User>();
	private final ArrayList<Group> groups = new ArrayList<Group>();

    private static UserManager instance;

	/**
	 * Default constructor
	 */
	public UserManager() {
        instance = this;
	}

    /**
     * An easy way of getting the user manager singleton object
     * @return the working instance of the user manager
     */
    public static UserManager getInstance()
    {
        return instance;
    }


	/**
	 * Adds a user to the user list
	 * 
	 * @param name
	 * @param username
	 * @param pw
	 * @param email
	 * @return true if successful, false if not
	 */
	public boolean addUser(String name, String username, String pw, String email) {
		User temp = new User(name, username, pw, email);

		// check user uniqueness and that password is not empty
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).isEqual(temp))
				return false;
		}
		if (pw == null)
			return false;

		// add user to the list of users
		users.add(temp);

		return true;
	}

	public boolean login(String username, String password) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getUsername().equals(username)) {
				if (users.get(i).getPassword().equals(password))
					return true;
				else
					return false;
			}
		}

		return false;
	}

    /**
     * A function to get a user from the list based on username, used to add users to appointments and so on.
     * @param username the username to find
     * @return the user object matching the username, or null if not found.
     */
    public User getUser(String username)
    {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getUsername().equals(username))
                return users.get(i);

        return null;
    }


	@Override
	public void parseFromXML(String XMLString) {
		
	}

	@Override
	public String parseToXML() {
		String result = "";
		try {
			DocumentBuilder f = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document d = f.newDocument();

			Element root = d.createElement("group");
			Element e = null, ee = null;
			
			//add user elements
			for (int i = 0; i < users.size(); i++) {
				User aUser = users.get(i);
				ee = d.createElement("user");
				root.appendChild(ee);
				
				//name
				e = d.createElement("name");
				e.appendChild(d.createTextNode(aUser.getName()));
				ee.appendChild(e);

				//username
				e = d.createElement("username");
				e.appendChild(d.createTextNode(aUser.getUsername()));
				ee.appendChild(e);
				
				//password
				e = d.createElement("password");
				e.appendChild(d.createTextNode(aUser.getPassword()));
				ee.appendChild(e);
				
				//email
				e = d.createElement("email");
				e.appendChild(d.createTextNode(aUser.getEmail()));
				ee.appendChild(e);
			}
			
			//add group elements
			for (int i = 0; i < groups.size(); i++) {
				Group aGroup = groups.get(i);
				ee = d.createElement("group");
				root.appendChild(ee);
				
				//name
				e = d.createElement("name");
				e.appendChild(d.createTextNode(aGroup.getName()));
				ee.appendChild(e);
				
				//users
				for (int j = 0; j < aGroup.getUsers().size(); j++) {
					User aUser = aGroup.getUsers().get(j);
					
					e = d.createElement("user");
					e.appendChild(d.createTextNode(aUser.getUsername()));
					ee.appendChild(e);
				}
				
				//subgroups
				for (int j = 0; j < aGroup.getSubGroups().size(); j++) {
					Group b = aGroup.getSubGroups().get(j);
					
					e = d.createElement("group");
					ee.appendChild(e);
					
					Element sub = null;
					sub = d.createElement("name");
					sub.appendChild(d.createTextNode(b.getName()));
					e.appendChild(sub);
					
					//users in subgroup
					for (int k = 0; k < aGroup.getSubGroups().get(j).getUsers().size(); k++) {
						User c = aGroup.getSubGroups().get(j).getUsers().get(k);
						
						sub = d.createElement("user");
						sub.appendChild(d.createTextNode(c.getUsername()));
						e.appendChild(sub);
					}
				}
			}
		} 
		catch (Exception e) {

		}
		
		return "";
	}
}
>>>>>>> 8563b3d30f1c5a33b2e8d1666ed6effaff3269f7
