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
	 * Saves information to a file in XML format
	 * @param filename Name of file to print to
	 * @param XMLString The string containing the XML
	 */
	public void saveXML(String filename, String XMLString)
	{
		
	}
	
	/**
	 * Reads a file to a string
	 * @param filename Name of file to read from
	 * @return XML string
	 */
	public String readXML(String filename)
	{
		return "";
	}
	
	/**
	 * Parses java objects to XML
	 * @return XML string
	 */
	public abstract String parseToXML();
	
	/**
	 * Parses an XML string to java objects
	 * @param XMLString The string to parse
	 */
	public abstract void parseFromXML(String XMLString);
	
}
