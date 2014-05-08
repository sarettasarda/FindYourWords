/**
 * The class LetterView creates and manages a view to show the letters
 *  
 * @author Sara Craba
 * @version 1.0
 */
package grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LetterView extends View
{
	private Paint letterPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG |Paint.ANTI_ALIAS_FLAG);

	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LetterView(Context context) 
	{
		super(context);
		
		letterPaint.setTextSize(GridManager.CELL_TEXT_SIZE);
		letterPaint.setColor(GridManager.CELL_TEXT_COLOR);
		letterPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"jennifer.ttf"));
	}
		 
	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LetterView(Context context, AttributeSet attrs) 
	{
		super( context, attrs );
	}
	 
	/**
	 * Constructor.
	 * @see View class on Android
	 */
	public LetterView(Context context, AttributeSet attrs, int defStyle) 
	{
		super( context, attrs, defStyle );
	}

	/**
	 * Drawer: draw all letters into the grid.
	 * @see onDraw() in View class on Android
	 */
	@Override
	protected void onDraw(Canvas canvas) 
	{
		// draw background
		super.onDraw(canvas);
	
		// draw letters
		for(int yPosition=0; yPosition<GridManager.GRID_Y_DIMENSION; yPosition++)
		{
			for(int xPosition=0; xPosition<GridManager.GRID_X_DIMENSION; xPosition++)
			{
				canvas.drawText(Character.toString( GridManager.mCells[yPosition][xPosition].getLetter()), 
					(GridManager.mCells[yPosition][xPosition].getCenterX()- GridManager.CELL_TEXT_SIZE/4), 
					(GridManager.mCells[yPosition][xPosition].getCenterY()+ GridManager.CELL_TEXT_SIZE/4), 
																							letterPaint);
			}
		}
		Log.i("LetterView", "letters drawed");
	}
	
	/**
	 * @see onLayout() in View class on Android
	 */
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) 
	{	
		super.onLayout(changed, left, top, right, bottom);
	}	
}

