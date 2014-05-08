package grid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class RelativeLayoutView extends RelativeLayout
{
	public static int parentWidth;
	public static int parentHeight;
	
	public RelativeLayoutView(Context context) {
		super(context);
		Log.e("const", "1");
	}
	
	public RelativeLayoutView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		Log.e("rL", "2");
	}
	
	public RelativeLayoutView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);

		Log.e("const", "3");
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		parentHeight = MeasureSpec.getSize(heightMeasureSpec);	
	
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
