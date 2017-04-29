/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon.responses;

import codedungeon.Direction;
import codedungeon.Response;

public class RepairBridge extends Response 
{
	/**
	 * Constructs an order to the robot to repair a bridge in a direction.
	 * @param d the direction in which you want the robot to move
	 */
	public RepairBridge(Direction d)
	{
		this.d = d;
	}
}
