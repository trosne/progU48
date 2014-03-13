package tdt4140.calendarsystem;

public class User {

	private String name;
	private String username;
	private String password;
	private String email;
	
	/**
	 * Default constructor
	 */
	public User()
	{
		
	}
	
	public User(String name, String username, String password, String email) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * Checks equality of two User objects. They are equal if usernames match
	 * @param aUser The user to compare to
	 * @return true if calling object is equal to parameter object
	 */
	public boolean isEqual(User aUser) {
		if (this.username.equals(aUser.getUsername()))
			return true;
		
		return false;
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

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
}
