package tdt4140.calendarsystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserManager extends Manager {

	private final ArrayList<User> users = new ArrayList<User>();
	private final ArrayList<Group> groups = new ArrayList<Group>();
	
	private static final String TAG_GROUP = "group", TAG_USER = "user", TAG_NAME = "name",
			TAG_USERNAME = "username", TAG_PW = "password", TAG_EMAIL = "email";

	private static String userFile = "users.xml";
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
	public void parseFromXML() {
		
	}

	@Override
	public void parseToXML() {
		String result = "";
        Document d = null;
        Element root = null;
        try {
            DocumentBuilder f = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            d = f.newDocument();

            root = d.createElement("groups");

            Element e = null, ee = null;
            //add users
            for (int i = 0; i < users.size(); i++)
            {
                User user = users.get(i);
                ee = d.createElement(TAG_USER);
                root.appendChild(ee);
                
                ee.setAttribute(TAG_NAME, user.getName());
                ee.setAttribute(TAG_USERNAME, user.getUsername());
                ee.setAttribute(TAG_PW, user.getPassword());
                ee.setAttribute(TAG_EMAIL, user.getEmail());
            }

            //add groups
            for (int i = 0; i < groups.size(); i++)
            {
                Group group = groups.get(i);
                ee = d.createElement(TAG_GROUP);
                root.appendChild(ee);

                ee.setAttribute(TAG_NAME, group.getName());
                
                //users
                for (int j = 0; j < groups.get(i).getUsers().size(); j++) {
                	User gUser = groups.get(i).getUsers().get(j);
                	e = d.createElement(TAG_USER);
                	ee.appendChild(e);
                	
                	e.setAttribute(TAG_USERNAME, gUser.getUsername());
                }
                
                //subgroups
                for (int j = 0; j < groups.get(i).getSubGroups().size(); j++) {
                	Group subGroup = groups.get(i).getSubGroups().get(j);
                	e = d.createElement(TAG_GROUP);
                	ee.appendChild(e);
                	
                	e.setAttribute(TAG_NAME, subGroup.getName());
                	
                	//users in subgroup
                	for (int k = 0; k < subGroup.getUsers().size(); k++) {
                		User subUser = subGroup.getUsers().get(k);		
                		Element subU = d.createElement(TAG_USER);
                		e.appendChild(subU);
                		
                		subU.setAttribute(TAG_USERNAME, subUser.getUsername());
                	}
                }
            }

        }
        catch (Exception e) {
            System.out.println("Exception writing user manager to file: \n" + e.getMessage());
        }

        //save the file:
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            tr.transform(new DOMSource(root),
                    new StreamResult(new FileOutputStream(userFile)));


        } catch (TransformerException te) {
            System.out.println("Error transforming user manager XML.");
            System.out.println(te.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO exception in user manager XML.");
            System.out.println(ioe.getMessage());
        }
	}
}
