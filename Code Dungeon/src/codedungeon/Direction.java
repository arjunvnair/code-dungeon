/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import java.awt.Point;

public class Direction 
{
	public static final Direction RIGHT = new Direction(0);
	public static final Direction UP = new Direction(1);
	public static final Direction LEFT = new Direction(2);
	public static final Direction DOWN = new Direction(3);
	private int pointer;
	public Direction(int p)
	{
		pointer = p;
	}
	protected static Direction random()
	{
		return new Direction((int) (Math.random() * 4));
	}
	protected Point getTileMoveInDirection(int x, int y, int units)
	{
		if(this.pointer == RIGHT.pointer)
			return new Point(x + units, y);
		if(this.pointer == UP.pointer)
			return new Point(x, y - units);
		if(this.pointer == LEFT.pointer)
			return new Point(x - units, y);
		if(this.pointer == DOWN.pointer)
			return new Point(x, y + units);
		return new Point(x, y);
	}
	public boolean equals(Direction d)
	{
		return this.pointer == d.pointer;
	}
	public String getName()
	{
		if(this.equals(Direction.UP))
			return "upwards";
		else if(this.equals(Direction.DOWN))
			return "downwards";
		else if(this.equals(Direction.LEFT))
			return "left";
		else if(this.equals(Direction.RIGHT))
			return "right";
		return null;
	}
}
