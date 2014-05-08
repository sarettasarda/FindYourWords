/**
 * CaptureWord is a class that search if the fingher touch have found a word
 * 
 * Commands explain which direction the finger have take.
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

import word.OneWord;
import mainClasses.MainActivity;
import android.util.Log;

public class CaptureWord 
{	
	/**
	 * The controlCaptureWord method make all control to search if a fingher touch have found a word.
	 * @param inTouch_coord		first cell touched 
	 * @param outTouch_coord	last cell touched
	 * @param mCells			grid of the game
	 * @return					a word string if have found a word, null otherwise 
	 */
	protected static String controlCaptureWord(Cell inTouch_coord, Cell outTouch_coord)
	{
		//control if with the coordinates is possible to find a word
		if(Cell.possibleWord(inTouch_coord, outTouch_coord))
		{
	        Log.i("CaptureWord", " not possible coordinate to find word");
			return null;
		}
		
		//check what direction have take a fingher
		int command = Cell.fingherDirection(inTouch_coord, outTouch_coord);
		
		//calculate distance from the two cells
		int wordLength= Cell.distanceFromTwoPoints(inTouch_coord, outTouch_coord, command);
		
		//note: the word can be read in two possible verse
		StringBuilder possibleWord = new StringBuilder();
		
		//built a word from first cell touched  to last cell touched 
		int varA, varB;
		for (int index=0; index< wordLength; index++)
		{
			//initialization of varA and varB
			varA = inTouch_coord.getCoordX() + (((command==0)||(command==4))?0:((command>4)?-index:index));
			varB = inTouch_coord.getCoordY() + (((command==2)||(command==6))?0:
																((command>2)&&(command<6)?index:-index));
			
			possibleWord.append(GridManager.mCells[varB][varA].getLetter());
		}
		
		String possibleStringWord= possibleWord.toString();
		possibleStringWord= possibleStringWord.toLowerCase();
		
		//control if the word is in the list
		if(GridManager.wordsMap.containsKey(possibleStringWord))
		{
			strikeWord(possibleStringWord);
			return possibleStringWord;
		}
		else
		{
			//reverse the string to read it in the other verse
			possibleWord.reverse();
			possibleStringWord= possibleWord.toString();
			possibleStringWord= possibleStringWord.toLowerCase();
			
			if(GridManager.wordsMap.containsKey(possibleStringWord))
			{
				strikeWord(possibleStringWord);
				return possibleStringWord;
			}
			
			Log.i("CaptureWord", "not a correct word");
			
			return null;
		}
	}
	
	/**
	 * The strikeWord method strike the word into the list of words to search 
	 * @param wordToStrike		word to strike
	 */
	protected static void strikeWord(String wordToStrike)
	{
		OneWord oneWordSupport=	GridManager.wordsMap.get(wordToStrike);
		oneWordSupport.strikeWordFunction();
		
		MainActivity.wordList.invalidate();
	}
}
