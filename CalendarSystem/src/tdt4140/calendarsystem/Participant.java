package tdt4140.calendarsystem;

public class Participant {

	private User aUser;
	private String status;
	
	/**
	 * Default constructor
	 */
	public Participant()
	{
		
	}

	/**
	 * @return the aUser
	 */
	public User getaUser() {
		return aUser;
	}

	/**
	 * @param aUser the aUser to set
	 */
	public void setaUser(User aUser) {
		this.aUser = aUser;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
