package tdt4140.calendarsystem;

import java.util.ArrayList;

public class UserManager extends Manager {

	private ArrayList<User> users;
	private ArrayList<Group> groups;
	
	public UserManager()
	{
		
	}
	
	
	public boolean addUser(String name, String username, String pw, String email)
	{
		User temp = new User(name, username, pw, email);
		
		// check user uniqueness and that password is not empty
		for(int i=0; i<users.size(); i++)
		{
			if(users.get(i).isEqual(temp))
				return false;
		}
		if(pw == null)
			return false;
		
		// add user to the list of users
		users.add(temp);
		
		return true;
	}
	
	@Override
	public void parseFromXML(String XMLString)
	{
		
	}

	@Override
	public String parseToXML() {
		// TODO Auto-generated method stub
		return null;
	}
}
