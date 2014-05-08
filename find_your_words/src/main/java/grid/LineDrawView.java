/**
 * The class LineDrawView creates and manages a view to show the lines when player touch the screen
 *  
 * @author Sara Craba
 * @version 1.0
 */

package grid;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LineDrawView extends View
{
	private Paint LinePaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);
	
	private Cell inTouch_coord= new Cell();
	private static Cell middleTouch_coord = new Cell();
	private static Cell outTouch_coord = new Cell();
	private static Cell touchSupport = new Cell();

	private Line currentLine= new Line();
	private ArrayList<Line> fixedLines = new ArrayList<Line>();
	
	/**
	 * The class Line help to manage the lines into LineDraw class
	 */
	private class Line
	{
		private Cell startCell;
		private Cell endCell;
		
		/**
		 * Contructor
		 * @param startCell		first cell touched
		 * @param endCell		last cell touched
		 */
		
		Line(Cell startCell, Cell endCell)
		{
			 this.startCell = startCell;
			 this.endCell = endCell;
		}
		
		/**
		 * Constructor: put null values in the variables
		 */
		Line()
		{
			this(null, null);
		}
		
		/**
		 * Get the first cell touched
		 * @return		first cell touched
		 */
		private Cell getStartCell()
		{
			return startCell;
		}
		
		/**
		 * Get the last cell touched
		 * @return		last cell touched
		 */
		private Cell getEndCell()
		{
			return endCell;
		}
		
		/**
		 * Set the startCell and the endCell
		 * @param startCell
		 * @param endCell
		 */
		private void setCells(Cell startCell, Cell endCell) 
		{
			 this.startCell = startCell;
			 this.endCell = endCell;
		}
	}
	

	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LineDrawView(Context context) 
	{
		super(context);
	}
	
	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LineDrawView(Context context, AttributeSet attrs) 
	{
		super( context, attrs );
	}
	 
	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LineDrawView(Context context, AttributeSet attrs, int defStyle) 
	{
		super( context, attrs, defStyle );
	}
	
	/**
	 * Drawer: draw the lines when the screen is touched and the lines on the founded words.
	 * @see onDraw() in View class on Android
	 */
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		if(currentLine.getStartCell()!=null)
		{
			canvas.drawLine(currentLine.getStartCell().getCenterX(),currentLine.getStartCell().getCenterY(), 
				currentLine.getEndCell().getCenterX(), currentLine.getEndCell().getCenterY(), LinePaint);
		}
		
		for (Line line : fixedLines) 
		{
			canvas.drawLine(line.getStartCell().getCenterX(), line.getStartCell().getCenterY(), 
								line.getEndCell().getCenterX(), line.getEndCell().getCenterY(), LinePaint);
		}	 
	}	
 
	/**
	 * Manage the screen thouchs
	 * @see onTouchEvent() in View class on Android
	 */
	@Override
	public boolean onTouchEvent (MotionEvent event)
	{	
		//change float view coordinates into integer cell coordinates
		//xSupport and ySupport is a integer coordination of one cell in  grid
		int xSupport = ((int)event.getX() / GridManager.CELL_WIDTH) < GridManager.GRID_X_DIMENSION ?
							((int)event.getX() / GridManager.CELL_WIDTH): (GridManager.GRID_X_DIMENSION-1);
		int ySupport = ((int)event.getY() / GridManager.CELL_HEIGHT) < GridManager.GRID_Y_DIMENSION ?
							((int)event.getY() / GridManager.CELL_HEIGHT):(GridManager.GRID_Y_DIMENSION-1);

		switch(event.getAction()) 
		{
			case(MotionEvent.ACTION_DOWN):
				actionDown(xSupport, ySupport);
				return true;
			case(MotionEvent.ACTION_MOVE):
				actionMove(xSupport, ySupport);
				return true;
			case(MotionEvent.ACTION_UP):
				actionUp(xSupport, ySupport);
		 }
		return false;
	}
 
	/**
	 * When the finger touch the screen memorize (X,Y) grid coordinates
	 * @param x_Coord		x cordinate of the touch in the grid
	 * @param y_Coord		y cordinate of the touch in the grid
	 */
	private void actionDown(int xCoord, int yCoord)
	{
		inTouch_coord.setCoordinates(xCoord, yCoord);
		middleTouch_coord.setCoordinates(xCoord, yCoord);

		Log.i("LineDrawView - onTouch", "action down: y["+ yCoord +"], x[" +xCoord +']');
	}

	/**
	 * The actionMove method recive a finger touch when the finger move around the paint view.
	 * When a different cell is touched, the method erase the previews line end draw a new one
	 * @param x_Coord		x cordinate of the touch in the grid
	 * @param y_Coord		y cordinate of the touch in the grid
	 */
	private void actionMove(int xCoord, int yCoord)
	{
		touchSupport.setCoordinates(xCoord, yCoord);
		
		//if new coordinate is equals that prevous coordinate, no actions
		if(Cell.comparePoints(touchSupport, middleTouch_coord))
		{
			return;
		}
		
		//change middle touch coordinate to take track of the last movement
		middleTouch_coord.setCoordinates(touchSupport.getCoordX(), touchSupport.getCoordY());			
		Log.i("LineDrawView - onTouch", "action move: y["+ yCoord +"], x[" +xCoord +']');
		
		//paint a new line
		currentLine.setCells(inTouch_coord, middleTouch_coord);
		invalidate();
	}
	
	/**
	 * The actionUp method take the last cell touched from the finger, control if from the first touch
	 * to the last, the player have found a word. If the player have found a word, strike the word into
	 * the word list.
	 * @param x_Coord		x cordinate of the touch in the grid
	 * @param y_Coord		y cordinate of the touch in the grid
	 */
	private void actionUp(int xCoord, int yCoord)
	{		
		outTouch_coord.setCoordinates(xCoord, yCoord);			
		
		String word= CaptureWord.controlCaptureWord(inTouch_coord, outTouch_coord);
		Log.i("LineDrawView - onTouch", "action up: y["+ yCoord +"], x[" +xCoord +']' + ", word "+ word);
		
		//if support is into the word list, strikes word into the list
		if(word!=null)
		{
			CaptureWord.strikeWord(word);
			
			Line newWordStriked= new Line (inTouch_coord, outTouch_coord);
			fixedLines.add(newWordStriked);
			
			inTouch_coord= new Cell();
			outTouch_coord = new Cell();
			
			GridManager.win();
		}

		currentLine.setCells(null, null);
		invalidate();
	}
}
