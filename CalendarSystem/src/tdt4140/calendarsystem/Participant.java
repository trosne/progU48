package tdt4140.calendarsystem;

public class Participant {

	private User aUser;
	private String status;

    /**
     * Default values for status. Should be used every time.
     */
    public static final String STATUS_NOT_RESPONDED = "Not responded", STATUS_ATTENDING = "Attending",
            STATUS_DECLINED = "Declined", STATUS_CREATOR = "Creator", STATUS_HIDDEN = "Hide appointment";
	
	/**
	 * Default constructor
	 */
	public Participant()
	{
		status = STATUS_NOT_RESPONDED;
	}

    /**
     * Constructor with immediate user add
     * @param u user
     */
    public Participant(User u)
    {
        status = STATUS_NOT_RESPONDED;
        aUser = u;
    }
	
	/**
	 * Checks equality of two Participant objects. They are equal if user and status match
	 * @param x The participant to compare to
	 * @return true if calling object is equal to parameter object
	 */
	public boolean isEqual(Participant x) {
		if (this.aUser.isEqual(aUser) && this.status.equals(x.getStatus()))
			return true;
		
		return false;
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
