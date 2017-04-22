package robot;

import codedungeon.Direction;
import codedungeon.Move;
import codedungeon.RepairBridge;
import codedungeon.Response;

public class EasyBot extends Robot 
{
	Direction d = new Direction(4);
	public EasyBot()
	{
		name = "EasyBot";
	}
	@Override
	public Response getResponse(boolean[][] radar) 
	{
		if(!radar[1][0] && d.equals(Direction.RIGHT))
			return new RepairBridge(Direction.LEFT);
		else if(!radar[0][1] && d.equals(Direction.DOWN))
			return new RepairBridge(Direction.UP);
		else if(!radar[1][2] && d.equals(Direction.LEFT))
			return new RepairBridge(Direction.RIGHT);
		else if(!radar[2][1] && d.equals(Direction.UP))
			return new RepairBridge(Direction.DOWN);
		if(radar[1][0] && !d.equals(Direction.RIGHT))
			d = Direction.LEFT;
		else if(radar[0][1] && !d.equals(Direction.DOWN))
			d = Direction.UP;
		else if(radar[1][2] && !d.equals(Direction.LEFT))
			d = Direction.RIGHT;
		else if(radar[2][1] && !d.equals(Direction.UP))
			d = Direction.DOWN;
		else if(radar[1][0])
			d = Direction.LEFT;
		else if(radar[0][1])
			d = Direction.UP;
		else if(radar[1][2])
			d = Direction.RIGHT;
		else if(radar[2][1])
			d = Direction.DOWN;
		return new Move(d);
	}

}
