/**
 * Cell is a class that rappresent each cell of the grid with coordinates respect to the view.
 * 
 * Commands are used to determinate the direction in the grid, used to write word in the grid and
 * to capture word when the user play.
 * Commands:
 * 0: vertical up
 * 1: diagonal up rigth
 * 2: horizontal rigth
 * 3: diagonal down rigth
 * 4: vertical down
 * 5: diagonal down left 
 * 6: horizontal left
 * 7: diagonal up left
 * 
 * @author Sara Craba
 * @version 1.0
 */
package grid;

public class Cell 
{
	private int x_coordinate;
	private int y_coordinate;
	private char letter= GridManager.CELL_TEXT_NULL;	//default
	private int x_center;
	private int y_center;
	
	/**
	 * Constructor
	 * @param x_coordinate		X corrdinate in the grid
	 * @param y_coordinate		Y corrdinate in the grid
	 * @param x_center			X cell center respect the paint view
	 * @param y_center			Y cell center respect the paint view
	 */
	protected Cell(int x_coordinate, int y_coordinate)
	{
		this.x_coordinate=x_coordinate;
		this.y_coordinate=y_coordinate;
		this.x_center=(x_coordinate * GridManager.CELL_WIDTH) + (GridManager.CELL_WIDTH/2);
		this.y_center=(y_coordinate * GridManager.CELL_HEIGHT) + (GridManager.CELL_HEIGHT/2);
	}
	
	/**
	 * Constructor: put the default value "-1" at all parameters
	 * 
	 */
	protected Cell()
	{
		this(0, 0);
	}
	
	/**
	 * Change cell grid coordinates and center in the view from the (X,Y) grid coordinates
	 * @param x_coordinate		x grid coordinate
	 * @param y_coordinate		y grid coordinate
	 */
	protected void setCoordinates(int x_coordinate, int y_coordinate)
	{
		this.x_coordinate=x_coordinate;
		this.y_coordinate=y_coordinate;
		this.x_center=(x_coordinate * GridManager.CELL_WIDTH) + (GridManager.CELL_WIDTH/2);
		this.y_center=(y_coordinate * GridManager.CELL_HEIGHT) + (GridManager.CELL_HEIGHT/2);
	}
	
	/**
	 * Get X coordinate in the grid
	 * @return x_coordinate
	 */
	protected int getCoordX()
	{
		return x_coordinate;
	}
	
	/**
	 * Get Y coordinate in the grid
	 * @return y_coordinate
	 */
	protected int getCoordY()
	{
		return y_coordinate;
	}
	
	/**
	 * Get the letter of the cell
	 * @return letter
	 */
	protected char getLetter()
	{
		return letter;
	}
	
	/**
	 * Get X cell center respect the paint view
	 * @return x_center
	 */
	protected int getCenterX()
	{
		return x_center;
	}
	
	/**
	 * Get Y cell center respect the paint view
	 * @return y_center
	 */
	protected int getCenterY()
	{
		return y_center;
	}
	
	/**
	 * Set the letter in the cell
	 * @param letter	letter to set
	 */
	protected void setLetter(char letter)
	{
		this.letter=letter;
	}
	
	/**
	 * Set the letter in the cell
	 * @param letter	letter to set
	 */
	protected void setLetter(char[] letter)
	{
		this.letter=letter[0];
	}
	
	/**
	 * The possibleWord method is used to determinate if a touch can be a word, 
	 * only touch that start and finish in two  different cell that are in 
	 * horizontal, vertical or diagonal position can be a word 
	 * @param inTouch_coord		first cell touched 
	 * @param outTouch_coord	last cell touched
	 * @return					true if can be a word, false otherwise
	 */
	protected static boolean possibleWord(Cell inTouch_coord, Cell outTouch_coord)
	{
		//is a possible word if input touch is different from the end touch and:
		//if have same x but different y (command 0 and 4)
		//if have same y but different x (command 2 and 6)
		//if inTouch and outTouch is in diagonal positions (command 1 and 5, 3 and 7)
		if((inTouch_coord.getCoordX() != outTouch_coord.getCoordX()) && 
		   (inTouch_coord.getCoordY() != outTouch_coord.getCoordY()) &&
		   ((inTouch_coord.getCoordX() + inTouch_coord.getCoordY()) != 
		   										(outTouch_coord.getCoordX()+outTouch_coord.getCoordY())) &&
		   ((inTouch_coord.getCoordX() + outTouch_coord.getCoordY())!= 
		   										(inTouch_coord.getCoordY()+outTouch_coord.getCoordX())))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * The fingherDirection method is used to determinate which direction have take the finger
	 * there are 8 possible directions and a cammand number associate at each direction.
	 * Normally is call after possibleWord method.
	 * @param inTouch_coord		first cell touched 
	 * @param outTouch_coord	last cell touched
	 * @return					command, a integer [0, 7] that rappresent the direction
	 */
	protected static int fingherDirection(Cell inTouch_coord, Cell outTouch_coord)
	{
		//checks what direction have take a fingher
		if(inTouch_coord.getCoordX()== outTouch_coord.getCoordX())
		{
			if(inTouch_coord.getCoordY() > outTouch_coord.getCoordY())
			{
				return 0;
			}
			else
			{
				return 4;
			}
		}
		else if(inTouch_coord.getCoordY() == outTouch_coord.getCoordY())
		{
			if(inTouch_coord.getCoordX() > outTouch_coord.getCoordX())
			{
				return 6;
			}
			else 
			{
				return 2;
			}
		}
		else if((inTouch_coord.getCoordX() + inTouch_coord.getCoordY())==
												(outTouch_coord.getCoordX()+outTouch_coord.getCoordY()))
		{
			if(inTouch_coord.getCoordX() > outTouch_coord.getCoordX())
			{
				return 5;
			}
			else 
			{
				return 1;
			}
		}
		else
		{
			if(inTouch_coord.getCoordX() > outTouch_coord.getCoordX()) 
			{
				return 7;
			}
			else 
			{	
				return 3;
			}
		}
	}
	
	/**
	 * The distanceFromTwoPoints method is used to calculate how many cell there are 
	 * from the first cell to the last cell.
	 * Normally is call after fingherDirection cell;
	 * @param inTouch_coord		first cell touched
	 * @param outTouch_coord	last cell touched
	 * @param command			direction of the touch
	 * @return					a integer that rappresent the number of the cell
	 */
	protected static int  distanceFromTwoPoints(Cell inTouch_coord, Cell outTouch_coord, int command)
	{
		int distance;
		if(command==0)
		{
			distance = inTouch_coord.getCoordY() - outTouch_coord.getCoordY() + 1;
		}
		else if(command==4)
		{ 
			distance = outTouch_coord.getCoordY() - inTouch_coord.getCoordY() + 1;
		}
		else if(command>4)
		{ 
			distance = inTouch_coord.getCoordX() - outTouch_coord.getCoordX() + 1;
		}
		else
		{ 
			distance= outTouch_coord.getCoordX() - inTouch_coord.getCoordX() +1;
		}
		return distance;
	}
	
	/**
	 * The comparePoint method is used to deterinate if two cell are the same cell.
	 * @param cellA		cell to compare with cellB		
	 * @param cellB		cell to compare with cellA
	 * @return			true if the cells are the same, false otherwise
	 */
	protected static boolean comparePoints(Cell cellA, Cell cellB)
	{
		if ((cellA.getCoordX()!= cellB.getCoordX()) || (cellA.getCoordY()!= cellB.getCoordY()))
		{
			return false;
		}
		
		return true;
	}
}
