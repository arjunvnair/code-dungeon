package codedungeon;

import javax.swing.JOptionPane;

public class Bridge extends Tile 
{
	private static final long serialVersionUID = 1L;
	private int maxSteps;
	private int howManySteps;
	private boolean hasLeftRightOrientation;
	protected Bridge()
	{
		maxSteps = 0;
		howManySteps = 0;
		traversable = true;
		exitable = true;
	}
	protected Bridge(boolean branch, boolean leftRightOrientation)
	{
		if(branch)
			maxSteps = 3;
		else
			maxSteps = 0;
		howManySteps = 0;
		traversable = true;
		exitable = true;
		hasLeftRightOrientation = leftRightOrientation;
	}
	protected Bridge(int ms)
	{
		maxSteps = ms;
		howManySteps = 0;
		traversable = true;
		exitable = true;
	}
	protected boolean getSteppedOn()
	{
		if(traversable)
		{
			howManySteps++;
			if(!(howManySteps > maxSteps))
				Main.getLevel().addMinMove();
			Main.addMove();
			Main.updateMoves();
			
		}
		else
		{
			Main.addMove();
			Main.updateMoves();
			Main.interruptMusic();
			JOptionPane.showMessageDialog(Main.getMainScreen(), Main.getRobot().getName() + " just fell into the pit at coordinates (" + (int) Main.getRobotPosition().getY() + "," + (int) Main.getRobotPosition().getX() + ") while traveling " + Main.getDirection().getName() + ".");
		}
		return super.getSteppedOn();
	}
	protected void getExited(Direction d) 
	{
		traversable = false;
		exitable = false;
		Main.refreshTileAt(Main.getRobotPosition());
		if(!((hasLeftRightOrientation && (d.equals(Direction.LEFT) || d.equals(Direction.RIGHT))) || (!hasLeftRightOrientation && (d.equals(Direction.UP) || d.equals(Direction.DOWN)))))
		{
			Main.addMove();
			Main.updateMoves();
			Main.interruptMusic();
			JOptionPane.showMessageDialog(Main.getMainScreen(), Main.getRobot().getName() + " just fell off a bridge at coordinates (" + (int) Main.getRobotPosition().getY() + "," + (int) Main.getRobotPosition().getX() + ") while traveling " + Main.getDirection().getName() + ".");
		}
	}
	protected int getMaxSteps()
	{
		return maxSteps;
	}
	protected void repairBridge()
	{
		howManySteps++;
		if(howManySteps <= maxSteps)
			Main.getLevel().addMinMove();
		Main.addMove();
		Main.updateMoves();
		traversable = true;
		exitable = true;
	}
}
