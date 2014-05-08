/**
 * Class GridManager is the core of Find Your Words application
 * In this class the is implemented the grid and is call the two views the
 * paint the letters and the lines.
 * 
 * @author Sara Craba
 * @version 1.0
 */
package grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import mainClasses.MainActivity;
import win.DialogWin;
import word.CompareStringList;
import word.OneWord;
import android.graphics.Color;
import com.sara.find_your_words.R;

public class GridManager 
{
	protected static int CELL_WIDTH;
	protected static int CELL_HEIGHT;
	public static float CELL_TEXT_SIZE;
	protected static final int CELL_TEXT_COLOR = Color.BLACK;
	protected static final int CELL_LINE_COLOR = Color.RED;
	protected static final char CELL_TEXT_NULL = '-';

    protected static int TEXT_SIZE_LIST ;
    
    //grid dimension
	protected static final int GRID_Y_DIMENSION = 10;		//height/row
	protected static final int GRID_X_DIMENSION = 14;		//width/column
	
	protected static final int MAX_ITERATIONS=10;	
	
	//effective grid
	protected static Cell [][] mCells=new Cell [GRID_Y_DIMENSION][GRID_X_DIMENSION];
	
	//word to put in the grid
	protected static HashMap<String, OneWord> wordsMap;

	//helper to detect right words found
	protected CaptureWord captureWord = new CaptureWord();
	
	//MainActivity instance
	protected static MainActivity istanceMainActivity;

	//View to paint letters and lines
	private LineDrawView lineDrawView;
	private LetterView letterView;
	
	//word that realy is put in the grid
	private static ArrayList<String> wordsInList;
	
	//count how many words are found to show win dialog 
	private static int win;

	/**
	 * Contructor
	 * @param istance
	 * @param wordsMap	is the HashMap with the list of the words
	 */
	public GridManager( MainActivity istance, HashMap<String, OneWord> wordsMap, int textsize)
	{
		istanceMainActivity = istance;
		GridManager.wordsMap= wordsMap;

		CELL_WIDTH= istanceMainActivity.gridView.parentWidth/GRID_X_DIMENSION;
		CELL_HEIGHT= istanceMainActivity.gridView.parentHeight/GRID_Y_DIMENSION;
		CELL_TEXT_SIZE= (CELL_WIDTH*18)/35;

        TEXT_SIZE_LIST= textsize;

		buildCells();
			
		fillGrid(wordsMap);
	}

	/**
	 * The buildCells method build the cells of the grid
	 */
	private void buildCells() 
	{
		for(int yPosition=0; yPosition<GRID_Y_DIMENSION; yPosition++)
		{ 
			for(int xPosition=0; xPosition<GRID_X_DIMENSION; xPosition++)
			{ 
				mCells[yPosition][xPosition] = new Cell(xPosition, yPosition);
			}
		}		
	}

	/**
	 * The fillGrid method take the words from the wordsStringList and put the words into the grid.
	 * At the end call drawLetter method to draw letter in the view.
	 * @param wordsStringList	list of the words
	 */
	private void fillGrid(HashMap<String, OneWord> wordsMap)
	{
		boolean isIntoTheGrid;
		int randomFromZeroToSeven;
		int  maxIterations;
		
		//support string to put in the grid
		String wordSupport;
		
		//random variable to select random  coordinates
		Random r = new Random();
		
		//coordinates x and y
		int coordX=0; 
		int coordY=0;
		
		//create an ArrayList<String> of the words and copy all the String of the wordsMap
		ArrayList<String> wordsStringList= new ArrayList<String>();	
		for (String item : wordsMap.keySet()) 
		{
			wordsStringList.add(item);
		}
		
		//possible position for support string from start coordinate (X,Y)
		Positions wordPositions= new Positions (GRID_X_DIMENSION, GRID_Y_DIMENSION);
		boolean [] wordPositionsSupport= new boolean[8];
		
		wordsInList= (ArrayList<String>) wordsStringList.clone();
		
		win=0;
	
		//sort the string list to the lenght
		Collections.sort(wordsStringList, new CompareStringList());
		        
		Iterator<String> wordsStringListIterator= wordsStringList.iterator();
		
		//while all word are iterated	
		while(wordsStringListIterator.hasNext())
		{				
			//get first word from input array
			wordSupport=wordsStringListIterator.next();
			maxIterations=MAX_ITERATIONS;
			
			//take random coordinates and try to put into the grid, if not possible take other coordinates
			//isIn is true if supportString is into the grid							
			isIntoTheGrid=false;
			while (!isIntoTheGrid && (maxIterations>0))
			{
				//get random coordinates
				coordX=r.nextInt(GRID_X_DIMENSION);
				coordY=r.nextInt(GRID_Y_DIMENSION);

				//say what position are good for this coordinates
				wordPositionsSupport= wordPositions.getArray(mCells, wordSupport, coordX, coordY);
				for(int i=0; i<8; i++)
				{
					randomFromZeroToSeven=r.nextInt(8);
					if(wordPositionsSupport[randomFromZeroToSeven])
					{
						putWord(wordSupport, coordX, coordY, randomFromZeroToSeven);
						isIntoTheGrid=true;
						break;
					}
				}
				maxIterations--;
				
				if(maxIterations==0)
				{
					istanceMainActivity.led.setImageResource(R.drawable.led_off);
					wordsInList.remove(wordSupport);
				}
			}
		}
		
		//sort worsMap from the alfabetical values and put the views into wordList layout
		Collections.sort(wordsInList);
		for (String key : wordsInList) 
		{ 
			wordsMap.get(key).setTextSize(TEXT_SIZE_LIST);
			MainActivity.wordList.addView(wordsMap.get(key));
		}
		
		start();
	}

	/**
	 * The putWord method put word into the grid starting from (X,Y) cordinates in a direction.
	 * @param word		word to put into the grid
	 * @param coordX	x coordinate of start cell
	 * @param coordY	y coordinate of start cell
	 * @param command	direction
	 */
	private void putWord (String word, int coordX, int coordY, int command)
	{
		//change the letter to upper case
		word=word.toUpperCase();
		
        int varA, varB;
		for (int j=0; j< word.length(); j++)
		{
			//initialization of varA and varB different for each command (direction)
			varA=coordX+ (((command==0)||(command==4))?0:((command>4)?-j:j));
			varB=coordY+ (((command==2)||(command==6))?0:((command>2)&&(command<6)?j:-j));
		
			mCells[varB][varA].setLetter(word.charAt(j));
		}	
	}
	
	/**
	 * The resetGrid method reload the grid
	 */
	public void resetGrid(HashMap<String, OneWord> wordsMap)
	{
//debug remove when grid is total fill 
		for(int yPosition=0; yPosition<GRID_Y_DIMENSION; yPosition++)
		{ 
			for(int xPosition=0; xPosition<GRID_X_DIMENSION; xPosition++)
			{ 
				mCells[yPosition][xPosition] = null;
			}
		}	
		buildCells();
//end debug
		istanceMainActivity.gridView.removeView(letterView);
		istanceMainActivity.gridView.removeView(lineDrawView);
		MainActivity.wordList.removeAllViews();
		fillGrid(wordsMap);
	}
	
	/**
	 * The start method start the game.
	 * 1. draw the letters
	 * 2. capture touchs and draw lines
	 */
	void start()
	{
		Random random = new Random();
		
		char randomCar='A';
		
		//fill all void cell on the grid with a random char
		for(int yPosition=0; yPosition<GRID_Y_DIMENSION; yPosition++)
		{ 
			for(int xPosition=0; xPosition<GRID_X_DIMENSION; xPosition++)
			{ 
				if(mCells[yPosition][xPosition].getLetter()== CELL_TEXT_NULL)
				mCells[yPosition][xPosition].setLetter(Character.toChars(randomCar+ random.nextInt(26)));;
			}
		}	
		
		letterView= new LetterView(istanceMainActivity.getApplicationContext());
		istanceMainActivity.gridView.addView(letterView);

		lineDrawView= new LineDrawView(istanceMainActivity.getApplicationContext());
		istanceMainActivity.gridView.addView(lineDrawView);
	}

	/**
	 * The win method count the words founded, if the player founds all the words show the win dialog.
	 */
	public static void win() 
	{
		win++;
		
		if(win==wordsInList.size())
		{
			DialogWin callDialog = new DialogWin();
			callDialog.show(istanceMainActivity.getFragmentManager(), "missiles");

			istanceMainActivity.chrono.stop();
		}
	}
}

