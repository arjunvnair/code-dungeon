package robot;

import codedungeon.Direction;
import codedungeon.Move;

public class DaredevilBot extends Robot
{
	public DaredevilBot()
	{
		name = "DaredevilBot";
	}
	@Override
	public Move getResponse(boolean[][] radar) 
	{
		if(!radar[1][0])
			return new Move(Direction.LEFT);
		else if(!radar[0][1])
			return new Move(Direction.UP);
		else if(!radar[1][2])
			return new Move(Direction.DOWN);
		else
			return new Move(Direction.RIGHT);
	}
	
}
