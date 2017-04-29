/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import java.awt.Point;

public class Direction 
{
	private int pointer;
	/**
	 * Pointer is 0
	 */
	public static final Direction RIGHT = new Direction(0);
	/**
	 * Pointer is 1
	 */
	public static final Direction UP = new Direction(1);
	/**
	 * Pointer is 2
	 */
	public static final Direction LEFT = new Direction(2);
	/**
	 * Pointer is 3
	 */
	public static final Direction DOWN = new Direction(3);
	
	/**
	 * Constructs a direction according to the pointer value that corresponds to the compass below.<br>
	 * -  1  -<br>
	 *   
	 * 2  X  0<br>
	 * 
	 * -  3  -
	 * @param p a pointer from 0 to 3 indicating direction
	 */
	public Direction(int p)
	{
		if(0 <= pointer && pointer <= 3)
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
	
	/**
	 * Compares each direction's pointers to determine directional equality.
	 * @param d a direction which <b>this</b> will compare to.
	 */
	public boolean equals(Direction d)
	{
		return this.pointer == d.pointer;
	}
	
	protected String getName()
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
