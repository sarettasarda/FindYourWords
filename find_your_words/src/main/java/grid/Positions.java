/**
 * Position is a class that take track of all possible position of the words from the coordinates (X,Y)
 * 
 * Commands explain which direction can be used to put a word into the grid.
 * 
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

public class Positions{
	private boolean[] positions;
	private int grid_x_dimension;
	private int grid_y_dimension;

	/**
	 * Constuctor
	 * @param grid_x_dimension		whidt of the grid
	 * @param grid_y_dimension		hight of the grid
	 */
	public Positions(int grid_x_dimension, int grid_y_dimension)
	{
		//for definition all element are false
		positions= new boolean[8];
		this.grid_x_dimension=grid_x_dimension;
		this.grid_y_dimension=grid_y_dimension;
	}
	
	/**
	 * The getArray method return a position array that explain in which position can stay the
	 * word in the grid from (X,Y) start point.
	 * @param gridGame		grid of the game
	 * @param word			word to check
	 * @param coordX		x coordinate of the start cell
	 * @param coordY		y coordinate of the start cell
	 * @return				boolean position array
	 */
	public boolean[] getArray(Cell gridGame[][], String word, int coordX, int coordY)
	{
		control(gridGame, word,coordX,coordY);
		return positions;
	}
	
	/**
	 * The control method initialize the positions array that take track per each direction if a word can
	 * stay in the direction. The word start from the cell in coordinates (X,Y).
	 * Per each direction put true if can stay, false otherwise
	 * @param gridGame		grid of the game
	 * @param word			word to check
	 * @param coordX		x coordinate of the start cell
	 * @param coordY		y coordinate of the start cell
	 */
	private void control(Cell gridGame[][], String word, int coordX, int coordY)
	{
		final int costA= word.length() - 1;
		final int costB= grid_x_dimension - 1 - costA;
		final int costC= grid_y_dimension - 1 - costA;
		int varA, varB, varC, varD;

		for (int command=0; command<8; command++)
		{
			//initialization of varA and varB
			varA= ((command==0)||(command==4))?0:((command>4)?(coordX-costA):(costB-coordX));
			varB= ((command==2)||(command==6))?0:((command>2)&&(command<6)?(costC-coordY):(coordY-costA));

			//varA and varB say if the word starting from (X,Y) position can stay into the grid
			if( (varA<0) || (varB<0) )
			{
				positions[command]=false;
				continue;
			}

			//word can stay in the position
			positions[command]=true;

			//check if the char of the word can stay in the cells
			for (int j=0; j< word.length(); j++)
			{
				//initialization of varC and varD
				varC=coordX + (((command==0)||(command==4))?0:((command>4)?-j:j));
				varD=coordY + (((command==2)||(command==6))?0:((command>2)&&(command<6)?j:-j));

				//if the char into the cell is different from the char of the word, the position is busy
				if((gridGame[varD][varC].getLetter()!=GridManager.CELL_TEXT_NULL) && 
				   (gridGame[varD][varC].getLetter()!=word.charAt(j)))
				{
					positions[command]=false;
					break;
				}
			}
		}
	}
}