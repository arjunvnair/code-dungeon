/**
	© 2017 Arjun Nair
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package robot;

import java.util.ArrayList;
import java.util.List;

import codedungeon.Response;

public abstract class Robot 
{
	private static List<Robot> uploadedRobots;
	protected String name = "Robot"; //Override this with your robot's name in the constructor.
	public static void uploadRobots()
	{
		uploadedRobots = new ArrayList<Robot>();
		//uploadedRobots.add(new YourRobotClassName());
	}
	public static List<Robot> getUploadedRobots()
	{
		return uploadedRobots;
	}
	public static int indexOf(String name)
	{
		for(int i = 0; i < uploadedRobots.size(); i++)
			if(uploadedRobots.get(i).getName().equals(name))
				return i;
		return - 1;
	}
	public String getName()
	{
		return name;
	}
	public abstract Response getResponse(boolean[][] radar);
}
