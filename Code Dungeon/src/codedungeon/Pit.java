/**
	© 2017 Arjun Nair - All Rights Reserved
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import javax.swing.JOptionPane;

public class Pit extends Tile 
{
	private static final long serialVersionUID = 1L;
	protected Pit()
	{
		traversable = false;
		exitable = false;
	}
	protected boolean getSteppedOn() 
	{
		Main.addMove();
		Main.updateMoves();
		Main.interruptMusic();
		JOptionPane.showMessageDialog(Main.getMainScreen(), Main.getRobot().getName() + " just fell into the pit at coordinates (" + (int) Main.getRobotPosition().getY() + "," + (int) Main.getRobotPosition().getX() + ") while traveling " + Main.getDirection().getName() + ".");
		return super.getSteppedOn();
	}
}
