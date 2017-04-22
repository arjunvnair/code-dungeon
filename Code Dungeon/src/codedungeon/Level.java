/**
	© 2017 Arjun Nair
	Contact arjunvnair@hotmail.com with any queries or suggestions.
*/

package codedungeon;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private Tile board[][];
	private int minMoves; //May not necessarily be the minimum number of moves for ALL cases; some levels on higher difficulties may coincidentally generate shorter paths to victory.
	private String difficulty;
	public Level(String d, int height, int length)
	{
		difficulty = d;
		try
		{
			board = new Tile[height][length];
			if(difficulty.equals("Easy"))
			{
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[0].length; j++)
						placeTile(new Point(j, i), new Pit());
				minMoves = board.length + board[0].length - 2;
				generatePath(new Point(board[0].length/2, board.length/2));
			}
			else if(difficulty.equals("Normal"))
			{
				while(!containsBranching())
				{
					for(int i = 0; i < board.length; i++)
						for(int j = 0; j < board[0].length; j++)
							placeTile(new Point(j, i), new Pit());
					minMoves = board.length + board[0].length - 2;
					generateBranchingPath(new Point(board.length/2, board[0].length/2), 0);
				}
				
			}
			else if(difficulty.equals("Hard"))
			{
				while(!(containsBranching() && containsBridges()))
				{
					for(int i = 0; i < board.length; i++)
						for(int j = 0; j < board[0].length; j++)
							placeTile(new Point(j, i), new Pit());
					minMoves = board.length + board[0].length - 2;
					generateBranchingPath(new Point(board.length/2, board[0].length/2), 0);
					placeBridges();
				}
			}
		}
		catch(StackOverflowError e)
		{
			e.printStackTrace();
			board = (new Level(difficulty, length, height)).board;
		}
		initializeMinMoves();
	}
	protected Level(String filename)
	{
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		try 
		{

			fin = new FileInputStream(filename);
			ois = new ObjectInputStream(fin);
			this.board = (Tile[][]) ois.readObject();
			this.minMoves = ois.readInt();
			this.difficulty = (String) ois.readObject();

		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
		finally 
		{

			if (fin != null) 
			{
				try 
				{
					fin.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}

			if (ois != null) 
			{
				try 
				{
					ois.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}

		}
	}
	public void serialize(String name) 
	{
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try 
		{

			fout = new FileOutputStream("c:\\CodeDungeon\\" + name + ".ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(getBoard());
			oos.writeInt(getMinMoves());
			oos.writeObject(getDifficulty());
			oos.flush();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();

		} 
		finally 
		{
			if (fout != null) 
			{
				try 
				{
					fout.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) 
			{
				try 
				{
					oos.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	protected Tile[][] getBoard()
	{
		return board;
	}
	protected int getMinMoves()
	{
		return minMoves;
	}
	protected String getDifficulty()
	{
		return difficulty;
	}
	protected Tile getTileAt(Point p)
	{
		try
		{
			return board[(int) p.getY()][(int) p.getX()];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return new Pit();
		}
	}
	private int howManyTraversableAdjacent(Point p)
	{
		int count = 0;
		if(getTileAt(Direction.RIGHT.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)).isTraversable())
			count++;
		if(getTileAt(Direction.LEFT.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)).isTraversable())
			count++;
		if(getTileAt(Direction.UP.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)).isTraversable())
			count++;
		if(getTileAt(Direction.DOWN.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)).isTraversable())
			count++;
		return count;
	}
	private boolean canBePlaced(Point p)
	{
		try
		{
			if(board[(int) p.getY()][(int) p.getX()].isTraversable())
				return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		return howManyTraversableAdjacent(p) <= 1;
	}
	protected int howManyCanBePlacedAround(Point p)
	{
		int count = 0;
		if(canBePlaced(Direction.RIGHT.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)))
			count++;
		if(canBePlaced(Direction.LEFT.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)))
			count++;
		if(canBePlaced(Direction.UP.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)))
			count++;
		if(canBePlaced(Direction.DOWN.getTileMoveInDirection((int) p.getX(), (int) p.getY(), 1)))
			count++;
		return count;
	}
	@Deprecated
	protected boolean areTraversableTilesAdjacent(int x, int y, int x1, int y1)
	{
		if(x < 0 || y < 0 || x > board[0].length - 1 || y >  board.length - 1 || x1 < 0 || y1 < 0 || x1 > board[0].length - 1 || y1 >  board.length - 1)
			return false;
		if(Math.sqrt((double) Math.pow(x - x1, 2) + (double) Math.pow(y - y1, 2)) == 1 && board[y1][x1].isTraversable())
			return true;
		return false;
	}
	private void placeTile(Point p, Tile t)
	{
		int x = (int) p.getX();
		int y = (int) p.getY();
		board[y][x] = t;
	}
	private Point findAdjacentLegalTile(Point p)
	{
		int x = (int) p.getX();
		int y = (int) p.getY();
		p = Direction.random().getTileMoveInDirection(x, y, 1);
		while(!canBePlaced(p))
		{
			p = Direction.random().getTileMoveInDirection(x, y, 1);
		}
		return p;
	}
	private boolean containsBranching()
	{
		int count = 0;
		for(Tile [] e : board)
			for(Tile t : e)
				if(t instanceof Path && ((Path) t).getMaxSteps() > 0)
					count++;
		if(count >= board.length)
			return true;
		return false;
	}
	private boolean containsBridges()
	{
		int count = 0;
		for(Tile [] e : board)
			for(Tile t : e)
				if(t instanceof Bridge)
					count++;
		if(count >= board.length)
			return true;
		return false;
	}
	private void generatePath(Point start)
	{
		Point p = start;
		placeTile(p, new Path());
		for(int i = 0; i < minMoves; i++)
		{
			if(howManyCanBePlacedAround(p) > 0)
			{
				p = findAdjacentLegalTile(p);
				placeTile(p, new Path());
			}
			else
			{
				minMoves = i + 1;
				placeTile(p, new Goal());
				return;
			}	
		}
		placeTile(p, new Goal());
	}
	private void generateBranchingPath(Point start, int generation)
	{
		Point p = new Point((int) start.getX(), (int) start.getY());
		ArrayList<Point> branchPts = new ArrayList<Point>();
		if(generation == 0)
			placeTile(p, new Path());
		else if(howManyCanBePlacedAround(p) > 0)
			((Path) getTileAt(p)).addMaxStep();
		for(int i = 0; i < minMoves; i++)
		{
			if(howManyCanBePlacedAround(p) > 0)
			{
				if(howManyCanBePlacedAround(p) > 1 && (int) (Math.random() * 6) >= 3 - generation)
					branchPts.add(p);
				p = findAdjacentLegalTile(p);
				placeTile(p, new Path(generation));
			}
			else if(generation == 0)
			{
				minMoves = i + 1;
				placeTile(p, new Goal());
				return;
			}	
			else
				return;
		}
		if(generation == 0)
			placeTile(p, new Goal());
		for(Point pt : branchPts)
			if(generation == 0)
				generateBranchingPath(pt, generation + 2);
			else
				generateBranchingPath(pt, generation + 1);
		if(generation == 0)
		{
			for(int i = 0; i < board.length; i++)
			{
				for(int j = 0; j < board[0].length; j++)
				{
					p = new Point(j, i);
					if(getTileAt(p) instanceof Path && howManyTraversableAdjacent(p) == 1 && ((Path) getTileAt(p)).getMaxSteps() != 0)
						((Path) getTileAt(p)).setMaxSteps(1);
					else if(getTileAt(p) instanceof Path && howManyTraversableAdjacent(p) - 1 > ((Path) getTileAt(p)).getMaxSteps() && ((Path) getTileAt(p)).getMaxSteps() != 0)
						((Path) getTileAt(p)).setMaxSteps(howManyTraversableAdjacent(p) - 1);
				}
			}
			p = new Point(board[0].length/2, board.length/2);
			((Path) getTileAt(p)).setMaxSteps(howManyTraversableAdjacent(p) - 1);
		}		
	}
	private void placeBridges()
	{
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				Point p = new Point(j, i);
				boolean leftRight = getTileAt(Direction.LEFT.getTileMoveInDirection(j, i, 1)) instanceof Path && getTileAt(Direction.RIGHT.getTileMoveInDirection(j, i, 1)) instanceof Path;	
				boolean upDown = getTileAt(Direction.UP.getTileMoveInDirection(j, i, 1)) instanceof Path  && getTileAt(Direction.DOWN.getTileMoveInDirection(j, i, 1)) instanceof Path;
				if(getTileAt(p) instanceof Path && howManyTraversableAdjacent(p) == 2 && (leftRight || upDown) && (int)(Math.random() * 3) == 0
				)
				{
					if(((Path) getTileAt(p)).getMaxSteps() == 0)
						board[(int) p.getY()][(int) p.getX()] = new Bridge(false, leftRight);
					else
						board[(int) p.getY()][(int) p.getX()] = new Bridge(true, leftRight);
						
				}
			}
		}
	}
	protected void initializeMinMoves()
	{
		minMoves = 0;
		for(Tile[] e : board)
			for(Tile t : e)
				if((t instanceof Path && ((Path) t).getMaxSteps() == 0) || (t instanceof Bridge && ((Bridge) t).getMaxSteps() == 0))
					minMoves++;
	}
	protected void addMinMove()
	{
		minMoves++;
	}
	@Deprecated
	protected void printLevel()
	{
		for(Tile[] e : board)
		{
			for(Tile t : e)
				if(t instanceof Path)
					System.out.print(((Path) t).getMaxSteps());
				else if(!t.isTraversable())
					System.out.print("X");
				else if(t instanceof Goal)
					System.out.print("G");
				else if(t instanceof Bridge)
					if(((Bridge) t).getMaxSteps() > 0)
						System.out.print("B");
					else
						System.out.print("b");
			System.out.println();
		}
	}
}
