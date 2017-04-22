/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon.tutorial;

import robot.Robot;
import codedungeon.Direction;
import codedungeon.Main;
import codedungeon.Move;
import codedungeon.RepairBridge;
import codedungeon.Response;

public class TutorialBot extends Robot

{
	int count;
	static final Response[] responseList = {new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.LEFT), new RepairBridge(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new RepairBridge(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.LEFT), new Move(Direction.LEFT), new RepairBridge(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.LEFT), new Move(Direction.DOWN), new RepairBridge(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.DOWN), new RepairBridge(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new RepairBridge(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.DOWN), new RepairBridge(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.LEFT), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.DOWN), new Move(Direction.LEFT), new Move(Direction.LEFT), new Move(Direction.UP), new Move(Direction.UP), new Move(Direction.RIGHT), new Move(Direction.UP), new Move(Direction.RIGHT), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.RIGHT), new Move(Direction.DOWN), new Move(Direction.DOWN), new Move(Direction.LEFT)};	
	public TutorialBot()
	{
		name = "TutorialBot";
		count = 0;
	}
	public Response getResponse(boolean[][] radar) 
	{
		Response r = responseList[count];
		count++;
		if(count == 1)
			Main.setTutorialText("<html>Did you notice that the denominator increased? This number, which is used to calculate the efficiency of your robot, will calibrate according to whether or not your robot enters into branches or stays on the main path that goes towards the chest, the level objective.<html>");
		else if(count == 2)
			Main.setTutorialText("<html>So what is \"limited visibility\" anyway? Why didn't the robot go right instead of down at the start? The challenge of the game is that your robot cannot see the entire board, as its radar can only scan a 3x3 boolean array around it, which tells it whether or not a block is traversable. For example, the robot's current radar is: {{FTF}, {TTF}, {FTF}}.<html>");
		else if(count == 3)
			Main.setTutorialText("<html>How do you go about making this robot? First, stick the src folder into the IDE of your choice. Next, make a new class in src/robot that extends the Robot class. Program your robot, add a new instance of it to the static ArrayList<Robot> uploadedRobots in the Robot class, run the game, and test your robot on an auto-generated level of your choice!<html>");
		else if(count == 4)
			Main.setTutorialText("<html>In your robot, you must implement the method public Response getResponse(boolean[] radar). The game inputs the radar and expects an instance of the abstract class Response as an output, which can either be a move or a bridge repair, as we shall soon see.<html>");
		else if(count == 5)
			Main.setTutorialText("<html>Your robot just stepped on a bridge, which will break upon its exit. Bowever, your robot need not fear, as it can repair any broken bridge as a response.<html>");
		else if(count == 6)
			Main.setTutorialText("<html>The bridge has broken, but your robot will explore more territory before using up a move to repair a bridge that it may or may not use.<html>");
		else if(count == 7)
			Main.setTutorialText("<html>Both child classes of Response, Move and RepairBridge, require an instance of the Direction class as input for their constructor, so that the robot knows which direction to move or repair the bridge.<html>");
		else if(count == 8)
			Main.setTutorialText("<html>There are four directions: Direction.UP, Direction.DOWN, Direction.LEFT, and Direction.RIGHT.<html>");
		else if(count == 9)
			Main.setTutorialText("<html>Dead end. Looks like your robot will need to repair the bridge it just broke. Returning new RepairBridge(Direction.RIGHT) will repair the bridge that it just broke.<html>");
		else if(count == 10)
			Main.setTutorialText("<html>Now it's time for the robot to move onto the bridge that it just repaired, so it will return new Move(Direction.RIGHT).<html>");
		else if(count == 11)
			Main.setTutorialText("<html>Let's let the robot keep exploring. We'll resume this commentary at a later point in the robot's journey.<html>");
		else if(count == 60)
			Main.setTutorialText("<html>Pay close attention to this branch of the path. Things are about to get spicy.<html>");
		else if(count == 82)
			Main.setTutorialText("<html>We already explored that branch, what's going on? It seems our robot has a bug! Notice that the denominator didn't increase this time, as our robot just made an inefficient move which it will not calibrate for. This will deduct from the robot's overall efficiency rating.<html>");
		else if(count == 103)
			Main.setTutorialText("<html>From here, we'll let the robot explore its way to the goal without further commentary.<html>");
		else if(count == responseList.length)
			Main.setTutorialText("<html>Congratulations, you finished the tutorial! Does it look daunting to code a robot that can keep track of branching paths and bridges? Not to fear, there are three difficulty settings in the level generator!<html>");
		return r;
	}

}
