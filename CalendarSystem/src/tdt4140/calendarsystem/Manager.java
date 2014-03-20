package tdt4140.calendarsystem;

import java.util.ArrayList;

public abstract class Manager {

	/**
	 * Default constructor
	 */
	public Manager()
	{
		
	}
	
	/**
	 * Parses java objects to XML
	 * @return XML string
	 */
	public abstract void parseToXML();
	
	/**
	 * Parses an XML string to java objects
	 * @param XMLString The string to parse
	 */
	public abstract void parseFromXML();
	
}
