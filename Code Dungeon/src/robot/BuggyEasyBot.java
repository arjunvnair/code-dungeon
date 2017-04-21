package robot;

import codedungeon.Direction;
import codedungeon.Move;
import codedungeon.Response;

public class BuggyEasyBot extends Robot 
{
	Direction d = new Direction(4);
	public BuggyEasyBot()
	{
		name = "BuggyEasyBot";
	}
	@Override
	public Response getResponse(boolean[][] radar) 
	{
		if(radar[1][0] && !d.equals(Direction.RIGHT))
			d = Direction.LEFT;
		else if(radar[0][1] && !d.equals(Direction.DOWN))
			d = Direction.UP;
		else if(radar[1][2] && !d.equals(Direction.UP))
			d = Direction.RIGHT;
		else
			d = Direction.DOWN;
		return new Move(d);
	}

}
