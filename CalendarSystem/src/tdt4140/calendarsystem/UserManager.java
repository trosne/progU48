package tdt4140.calendarsystem;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	/**
	 * Add user by User object
	 * @param user
	 * @return true if successful, false if not
	 */
	public boolean addUser(User user) {

		// check user uniqueness and that password is not empty
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).isEqual(user))
				return false;
		}
		if (user.getPassword() == null)
			return false;

		// add user to the list of users
		users.add(user);

		return true;
	}
	
	/**
	 * Add group by name
	 * @param name
	 * @return true if successful, false if not
	 */
	public boolean addGroup(String name) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getName().equals(name)) {
				return false;
			}
		}
		
		groups.add(new Group(name));
		return true;
	}
	
	/**
	 * Add group with Group object
	 * @param group
	 * @return true if successful, false if not
	 */
	public boolean addGroup(Group group) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getName().equals(group.getName())) {
				return false;
			}
		}
		
		groups.add(group);
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
		
		Document d;
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            d = db.parse(userFile);
            NodeList groupsAndUsersList = d.getFirstChild().getChildNodes();

            //parsing group or user
            for (int i = 0; i < groupsAndUsersList.getLength(); i++)
            {
                Node subNode = groupsAndUsersList.item(i);
                if (subNode.getNodeName().equals(TAG_USER))
                {
                    User user = new User();
                    NamedNodeMap attributes = subNode.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++)
                    {
                        if (attributes.item(j).getNodeName().equals(TAG_NAME))
                            user.setName(attributes.item(j).getTextContent());
                        else if (attributes.item(j).getNodeName().equals(TAG_USERNAME))
                            user.setUsername(attributes.item(j).getNodeValue());
                        else if (attributes.item(j).getNodeName().equals(TAG_PW))
                            user.setPassword(attributes.item(j).getNodeValue());
                        else if (attributes.item(j).getNodeName().equals(TAG_EMAIL))
                            user.setEmail(attributes.item(j).getNodeValue());
                    }
                    users.add(user);
                }
                else if (subNode.getNodeName().equals(TAG_GROUP))
                {
                    Group group = new Group();
                    NamedNodeMap attributes = subNode.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++)
                    {
                        if (attributes.item(j).getNodeName().equals(TAG_NAME))
                            group.setName(attributes.item(j).getNodeValue());
                        
                        NodeList subGroupList = subNode.getChildNodes();
                        
                        //parse users and subgroups
                        for (int k = 0; k < subGroupList.getLength(); k++) {
                        	Node sub2Node = subGroupList.item(k);
                        	
                        	if (sub2Node.getNodeName().equals(TAG_USER)) {
                        		NamedNodeMap userAttr = sub2Node.getAttributes();
                        		
                        		for (int l = 0; l < userAttr.getLength(); l++) {
                        			if (userAttr.item(l).getNodeName().equals(TAG_USERNAME))
                        				group.addUserToGroup(getUser(userAttr.item(l).getNodeValue()));
                        		}
                        	}
                        	else if (sub2Node.getNodeName().equals(TAG_GROUP)) {
                        		Group subGroup = new Group();
                        		NamedNodeMap subGroupAttr = sub2Node.getAttributes();
                        		
                        		for (int l = 0; l < subGroupAttr.getLength(); l++) {
                        			if (subGroupAttr.item(l).getNodeName().equals(TAG_NAME))
                        				subGroup.setName(subGroupAttr.item(l).getNodeValue());
                        			
                        			NodeList subGroupUsers = sub2Node.getChildNodes();
                        			
                        			//parse users in subgroup
                        			for (int m = 0; m < subGroupUsers.getLength(); m++) {
                        				Node sub3Node = subGroupUsers.item(m);
                        				
                        				if (sub3Node.getNodeName().equals(TAG_USER)) {
                        					NamedNodeMap subUserAttr = sub3Node.getAttributes();
                                    		
                                    		for (int n = 0; n < subUserAttr.getLength(); n++) {
                                    			if (subUserAttr.item(n).getNodeName().equals(TAG_USERNAME))
                                    				subGroup.addUserToGroup(getUser(subUserAttr.item(n).getNodeValue()));
                                    		}
                        				}
                        			}
                        		}
                        		group.addSubGroup(subGroup);
                        	}
                        }
                        
                    }
                    groups.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

	/**
	 * Gets user list
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * Gets group list
	 * @return the groups
	 */
	public ArrayList<Group> getGroups() {
		return groups;
	}
}