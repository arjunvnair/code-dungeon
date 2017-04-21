package robot;

import codedungeon.Direction;
import codedungeon.Move;
import codedungeon.Response;

public class RandomBot extends Robot
{
	public RandomBot()
	{
		name = "RandomBot";
	}
	@Override
	public Response getResponse(boolean[][] radar) 
	{
		return new Move(new Direction((int) (Math.random() * 4)));
	}
	
}