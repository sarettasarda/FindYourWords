package win;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.sara.find_your_words.R;

public class WinView extends View 
{
    private Bitmap star;
    
    private Matrix rotator;

    private int widthView,heightView;
    private int widthStar,heightStar;

   
    public WinView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        
        star=BitmapFactory.decodeResource(getResources(), R.drawable.vortex, options);
        
        widthStar= star.getWidth(); 
        heightStar= star.getHeight();
        
        rotator= new Matrix();
        rotator.preTranslate(widthView/2 - widthStar/2, heightView/2 - heightStar/2);
    }
    
    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);
        
        canvas.drawBitmap(star, rotator, null);
      
        //refresh canvas
        invalidate();
    }
  
    //update position
    public void updatePosition(int degrees)
    {
        Matrix m = new Matrix();
        m.postRotate(degrees, widthStar/2, heightStar/2);
        m.postTranslate(widthView/2 - widthStar/2,  heightView/2 - heightStar/2);

        // Set the current position to the updated rotation
        rotator.set(m);
    }
    
  //record the size of the view
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
  {
      widthView=MeasureSpec.getSize(widthMeasureSpec);
      heightView=MeasureSpec.getSize(heightMeasureSpec);
      setMeasuredDimension(widthView,heightView);
  }
  
  @Override
  public boolean onTouchEvent (MotionEvent event)
  {	
	  switch(event.getAction()) 
		{
			case(MotionEvent.ACTION_DOWN):
				DialogWin.thread.stopIt();
				DialogWin.istance.getDialog().cancel();
				return true;
		 }
		return false;
  }
}
