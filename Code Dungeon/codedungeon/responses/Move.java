/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon.responses;

import codedungeon.Direction;
import codedungeon.Response;

public class Move extends Response 
{
	/**
	 * Constructs an order to the robot to move in a direction.
	 * @param d the direction in which you want the robot to move
	 */
	public Move(Direction d)
	{
		this.d = d;
	}
}
