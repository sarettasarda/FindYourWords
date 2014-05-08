/**
 * Class MainActivity is the main Activity of Find Your Words application
 * In this activity user can play the game.
 * Main Activity call a GridManager class to create the grid and manager the player touchs
 * 
 * @author Sara Craba
 * @version 1.0
 */
package mainClasses;

import grid.GridManager;
import grid.RelativeLayoutView;
import java.util.ArrayList;
import java.util.HashMap;
import word.OneWord;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sara.find_your_words.R;
import dialog.DialogInfoMain;

/**
 * @see Activity() on Android
 */
public class MainActivity extends Activity 
{
    private static int TEXT_SIZE_LIST;
	//layout objects 
	public static LinearLayout wordList;
	public RelativeLayoutView gridView;
	
	private Button buttonStart;
	private Button buttonReload;
	private Button buttonInfo;
	public ImageView led;

	private Typeface type;
	public Chronometer chrono;
	
	//input word 
	private ArrayList<String> inputArray;
	private static HashMap <String, OneWord> wordsMap;
	//suport to create HashMap
	private OneWord oneWord;
	
	//class that manage the grid view
	private GridManager gridManager;
	
	//istance of this class
	private MainActivity instance;

	/**
	 * @see onCreate() on Android Activity class
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

        TEXT_SIZE_LIST= (int) getResources().getDimension(R.dimen.list_words_main_activity);

		wordList =(LinearLayout)findViewById(R.id.wordList);
		gridView= (RelativeLayoutView)findViewById(R.id.relative_layout);

		buttonStart= (Button)findViewById(R.id.button_start);
		buttonReload= (Button)findViewById(R.id.button_reload);
		buttonInfo= (Button)findViewById(R.id.buttonInfo);
		
		led= (ImageView) findViewById(R.id.led);
		
		type =Typeface.createFromAsset(getAssets(),"jennifer.ttf");
		chrono=(Chronometer) findViewById(R.id.chronometer);
		chrono.setTypeface(type,0);
		
		//get the inputArray from the first activity
		inputArray=getIntent().getStringArrayListExtra("WordArray");
		
		//create an HasMap to collect the words
		wordsMap= new HashMap<String, OneWord>();
		for(int index=0; index<inputArray.size(); index++)
		{
			oneWord= new OneWord(this, inputArray.get(index));
			wordsMap.put(oneWord.getText().toString(), oneWord);
		}
        
		instance=this;		

		buttonStart.setOnClickListener(new View.OnClickListener() 
		{
	        public void onClick(View view) 
	        {	
	        	gridManager= new GridManager(instance, wordsMap, TEXT_SIZE_LIST);
	        	buttonStart.setVisibility(View.GONE);
	        	chrono.setBase(SystemClock.elapsedRealtime());
	        	chrono.start();
	        }
	    });
		
		//button reload: 
		//1. reset the previous map of the grid
		//2. unstrike word in the wordList
		buttonReload.setOnClickListener(new View.OnClickListener() 
		{
	        public void onClick(View view) 
	        {	
	        	chrono.stop();
	        	
	        	gridManager.resetGrid(wordsMap);
	        	
	        	for(OneWord item: wordsMap.values())
	        	{
	        		item.unstrikeWordFunction();
	        	}
	        	wordList.invalidate();
	        	
	        	chrono.setBase(SystemClock.elapsedRealtime());
	        	chrono.start();
	        }
	    });
		
		//button info: 
		//create a dialog and show it
		buttonInfo.setOnClickListener(new View.OnClickListener() 
		{
	        public void onClick(View view) 
	        {	
	        	DialogInfoMain callDialog = new DialogInfoMain();
				callDialog.show(getFragmentManager(), "missiles");
	        }
	    });
	}
}
