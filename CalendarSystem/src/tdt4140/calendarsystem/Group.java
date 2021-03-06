package tdt4140.calendarsystem;

import java.util.ArrayList;

public class Group {

	private ArrayList<User> users;
	private ArrayList<Group> subGroups;
	private String name;
	
	/**
	 * Default constructor
	 */
	public Group()
	{
		users = new ArrayList<>();
		subGroups = new ArrayList<>();
	}
	
	public Group(String name) {
		users = new ArrayList<>();
		subGroups = new ArrayList<>();
		this.name = name;
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
	
	public boolean addSubGroup(Group g) {
		for(int i=0; i<subGroups.size(); i++)
		{
			if(subGroups.get(i).getName().equals(g.getName()))
			{
				return false;
			}
		}
		
		subGroups.add(g);
		return true;
	}

	/**
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

    /**
     * Get users from all sub groups as well as own users recursively
     * @return all users
     */
    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> result = new ArrayList<>();
        result.addAll(users);
        for (int i = 0; i < subGroups.size(); i++)
            result.addAll(subGroups.get(i).getAllUsers());

        return result;
    }

	/**
	 * @param users the users to set
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * @return the subGroups
	 */
	public ArrayList<Group> getSubGroups() {
		return subGroups;
	}

	/**
	 * @param subGroups the subGroups to set
	 */
	public void setSubGroups(ArrayList<Group> subGroups) {
		this.subGroups = subGroups;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
