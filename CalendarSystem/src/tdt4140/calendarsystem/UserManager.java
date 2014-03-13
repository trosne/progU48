package tdt4140.calendarsystem;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserManager extends Manager {

	private ArrayList<User> users;
	private ArrayList<Group> groups;

	/**
	 * Default constructor
	 */
	public UserManager() {

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

	@Override
	public void parseFromXML(String XMLString) {
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
					
				}
			}
		} 
		catch (Exception e) {

		}
	}

	@Override
	public String parseToXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
