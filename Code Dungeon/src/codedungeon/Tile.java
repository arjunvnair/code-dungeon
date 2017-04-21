/**
	© 2017 Arjun Nair
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import java.io.Serializable;

public abstract class Tile implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected boolean traversable;
	protected boolean exitable;
	protected boolean isTraversable()
	{
		return traversable;
	}
	protected boolean getSteppedOn()
	{
		return traversable;
	}
	protected void getExited(Direction d) {}
	protected boolean allowsExit()
	{
		return exitable;
	}
}
