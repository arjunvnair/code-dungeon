/**
	© 2017 Arjun Nair
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import javax.swing.JOptionPane;

public class Goal extends Tile 
{
	private static final long serialVersionUID = 1L;
	protected Goal()
	{
		traversable = true;
		exitable = false;
	}
	protected boolean getSteppedOn() 
	{
		Main.addMove();
		Main.updateMoves();
		Main.interruptMusic();
		Main.playVictoryJingle();
		JOptionPane.showMessageDialog(Main.getMainScreen(), Main.getRobot().getName() + " just found the checkpoint with a " + (int)((((double) Main.getLevel().getMinMoves())/((double) Main.getMoves())) * 100) + "% efficiency rating.");
		return super.getSteppedOn();
	}
}
