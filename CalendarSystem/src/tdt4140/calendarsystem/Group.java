package tdt4140.calendarsystem;

import java.util.ArrayList;

public class Group {

	private ArrayList<User> users;
	private ArrayList<Group> subGroups;
	
	public Group()
	{
		
	}
	
	public boolean addUserToGroup(User u)
	{
		for(int i=0; i<users.size(); i++)
		{
			if(users.get(i).isEqual(u))
				return false;
		}
		
		users.add(u);
		return true;
	}
	
	public boolean removeUserFromGroup(User u)
	{
		for(int i=0; i<users.size(); i++)
		{
			if(users.get(i).isEqual(u))
			{
				users.remove(i);
				return true;
			}
		}
		
		return false;
	}
}
