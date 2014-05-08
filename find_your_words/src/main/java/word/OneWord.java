/**
 * Class OneWord is a TextView that rappresent a word in the list of search word
 * into the main activity.
 * 
 * @author Sara Craba
 * @version 1.0
 */

package word;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

public class OneWord extends TextView 
{
	private static final int TEXT_COLOR= Color.BLUE;
	
	private Typeface type;
	
	private LayoutParams lparams;
	
	/**
	 * Constuctor
	 * 
	 * @param context	@see TextView class on Android
	 * @param newWord	string that rappresent the word
	 */
	public OneWord(Context context, String wordString) 
	{
		super(context);
		
		//set layout params
		lparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(lparams);
        
    	//font implementation
		type=Typeface.createFromAsset(context.getAssets(),"note.ttf");
		this.setTextColor(TEXT_COLOR);
		this.setTypeface(type,0);
		
		this.setText(wordString);
	}
	
	/**
	 * Paint word with strike line
	 */
	public void strikeWordFunction()
	{
		this.setPaintFlags(this.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	}
	
	/**
	 * Remove strike line on the word 
	 */
	public void unstrikeWordFunction()
	{
		this.setPaintFlags(this.getPaintFlags()& (~ Paint.STRIKE_THRU_TEXT_FLAG));
	}
}
