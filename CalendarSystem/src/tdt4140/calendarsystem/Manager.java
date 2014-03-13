package tdt4140.calendarsystem;

import java.util.ArrayList;

public abstract class Manager {

	public Manager()
	{
		
	}
	
	public void saveXML(String filename, String XMLString)
	{
		
	}
	
	public String readXML(String filename)
	{
		return "";
	}
	
	public abstract String parseToXML();
	
	
	public abstract void parseFromXML(String XMLString);
	
}
