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
	protected String name = "Robot"; //Override this in child class constructor
	
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
	
	/**
	 * Inputs the robot's current radar and outputs a response that the robot will carry out.
	 * @param radar 3x3 array of booleans that indicates the traversability of the tiles around and below the robot, who sits at [1][1]
	 * @return the robot's response (instance of Move or RepairBridge)
	 */
	public abstract Response getResponse(boolean[][] radar);
}
