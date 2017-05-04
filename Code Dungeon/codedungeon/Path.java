/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;


public class Path extends Tile 
{
	private static final long serialVersionUID = 1L;
	private int maxSteps;
	private int howManySteps;
	
	protected Path()
	{
		traversable = true;
		exitable = true;
		maxSteps = 0;
		howManySteps = 0;
	}
	
	protected Path(int ms)
	{
		traversable = true;
		exitable = true;
		maxSteps = ms;
		howManySteps = 0;
	}
	
	protected int getMaxSteps()
	{
		return maxSteps;
	}
	
	protected boolean getSteppedOn() 
	{
		howManySteps++;
		if(!(howManySteps > maxSteps))
			Main.getLevel().addMinMove();
		Main.addMove();
		Main.updateMoves();
		return super.getSteppedOn();
	}
	
	protected void setMaxSteps(int n)
	{
		maxSteps = n;
	}
	
	protected void addMaxStep()
	{
		maxSteps++;
	}
}
