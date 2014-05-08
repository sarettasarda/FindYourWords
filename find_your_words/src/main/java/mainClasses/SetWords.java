/**
 * Class SetWords is the first Activity of Find Your Words application
 * In this activity user can write new words, upload and save .txt files,
 * chose the words to put in the grid and start the game
 * 
 * @author Sara Craba
 * @version 1.0
 */

package mainClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.sara.find_your_words.R;

import dialog.DialogCredits;
import dialog.DialogInfo;
import dialog.DialogSaveFile;

/**
 * @see Activity() on Android
 */
public class SetWords extends Activity 
{
	//layout objects 
	private EditText editText;
	private Button buttonInsert;
	private Button buttonReset;
	private Button buttonOpenFile;
	private Button buttonSaveFile;
	
	private LinearLayout layoutListWords;
	private Button buttonSelectAll;
	private Button buttonDeselectAll;
	private Button buttonPlay;
	
	private Button buttonInfo;
	private Button buttonCredits;
	
	//font implementation	
	private Typeface type;
	private static final int TEXT_EDITTEXT_COLOR = Color.WHITE;
	private static final int TEXT_LISTWORDS_COLOR = Color.BLACK;
	private static  int TEXT_LISTWORDS_SIZE;
	
	//onActivityResult code to open file choser
	private static final int REQUEST_CHOOSER = 1234;
	
	//array of input words	
	private ArrayList <CheckBox> checkBoxArray= new ArrayList<CheckBox>();
	//lastCheckBox takes track of the last CheckBox view into the layoutListWords
	private int lastCheckBox=0;
	
	//splitString method variables
	private static StringBuilder supportCreateWord;
	private CheckBox supportWord;
	
	//support for insertButton
	private static String textFromInputBox= new String();
	
	//support for request choser
	private static File file;
	
	//arrayString to pass at the MainActivity
	private ArrayList<String> inputArray;
	
	/**
	 * @see onCreate() on Android Activity class
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_words);
	
		//hide keybord when start
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		editText=(EditText)findViewById(R.id.insertWords);
        TEXT_LISTWORDS_SIZE = (int) getResources().getDimension(R.dimen.list_word_check_box);
		type =Typeface.createFromAsset(getAssets(),"jennifer.ttf");
		//set editText font 
		editText.setTextColor(TEXT_EDITTEXT_COLOR);
		editText.setTypeface(type,0);
		
		buttonInsert= (Button)findViewById(R.id.buttonInsert);
		buttonReset= (Button)findViewById(R.id.buttonReset);
		buttonOpenFile= (Button)findViewById(R.id.btnReadSDFile);
		buttonSaveFile= (Button)findViewById(R.id.buttonWriteSDFile);
		
		layoutListWords= (LinearLayout)findViewById(R.id.scrollView);
		buttonSelectAll= (Button)findViewById(R.id.selectAll);
		buttonDeselectAll= (Button)findViewById(R.id.deselectAll);
		buttonPlay= (Button)findViewById(R.id.button_play);
		
		buttonInfo= (Button)findViewById(R.id.buttonInfo);
		buttonCredits =(Button)findViewById(R.id.buttonCredits);

		//button insert: 
		//1. gets the text from editText and create one string of this
		//2. upload value of lastCheckBox
		//3. parse the string to detect the words and fill checkBoxArray
		//4. put the checkBox words into the layoutListWord
		buttonInsert.setOnClickListener(new View.OnClickListener() 
		{
	        public void onClick(View view) 
	        {	
	        	textFromInputBox = editText.getText().toString();

	    		lastCheckBox= checkBoxArray.size();
	    		
	        	splitString(textFromInputBox);
	        	
	        	for(int index=lastCheckBox; index<checkBoxArray.size(); index++)
	        	{
	        		layoutListWords.addView(checkBoxArray.get(index));
	        	}
	        	layoutListWords.invalidate();
	        }
	    });    
		
		//button reset:
		//erase the text into editText
		buttonReset.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				editText.setText("");				
			}
		});
		
		//button openFile
		//1. open a fileChooser to select the text file to open
		//2. call startActivityForResult to manage the file
		buttonOpenFile.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent getContentIntent = FileUtils.createGetContentIntent();
				Intent intent = Intent.createChooser(getContentIntent, "Select a file");
				
				startActivityForResult(intent, REQUEST_CHOOSER);
	        }
        });
		
		//button saveFile
		//1. put editText text into a bundle
		//2. create a dialog, pass bundle to dialog and show dialog
		buttonSaveFile.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				Bundle dialogArguments= new Bundle();
				dialogArguments.putString("text", editText.getText().toString());
				
				DialogSaveFile callDialog = new DialogSaveFile();
				callDialog.setArguments(dialogArguments);
				callDialog.show(getFragmentManager(), "missiles");
			}
		});
		
		//button info: 
		//create a info dialog and show it
		buttonInfo.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				DialogInfo callDialog = new DialogInfo();
				callDialog.show(getFragmentManager(), "missiles");
			}
		});
		
		//button credits: 
		//create a credits dialog and show it
		buttonCredits.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				DialogCredits callDialog = new DialogCredits();
				callDialog.show(getFragmentManager(), "missiles");
			}
		});
		
		//button selectAll
		//mark each CheckBox string as selected
		buttonSelectAll.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				for(CheckBox object: checkBoxArray)
				{
					object.setChecked(true);
				}
				
				layoutListWords.invalidate();
			}
		});
		
		//button deselectAll
		//mark each CheckBox string as not selected
		buttonDeselectAll.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				for(CheckBox object: checkBoxArray)
				{
					object.setChecked(false);
				}
				
				layoutListWords.invalidate();			
			}
		});
		
		//button play: start the game
		//1. create a new inputArray
		//2. copy the word from checkBoxArray to inputArray
		//3. start MainActivity with inputArray
		buttonPlay.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View view) 
			{
				inputArray= new ArrayList<String>();
			
				for(CheckBox object: checkBoxArray)
				{
					if(object.isChecked())
					{
						inputArray.add(object.getText().toString());
					}
				}
				
				if(inputArray.size()==0)
				{
					Toast.makeText(getApplicationContext(),"Nothing to search!",Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putStringArrayListExtra("WordArray", inputArray);
				    startActivity(intent);
				}
			}
		});
	}

	/**
	 * Split a String in words with 3 or more characters
	 * 
	 * 1. changes text in lower case
	 * 2. for all char in the textString
	 * 		2.1 if is a letter concat to StringBuilder
	 * 		2.2 else create a new CheckBox object and add this at checkBoxArray
	 * 
	 * @param textString 	String to split
	 */
	private void splitString(String textString)
	{	
		textString=textString.toLowerCase() + '.';
		
		//Note: creates a string builder whose value is initialized by the specified string, 
		//	plus an extra 16 empty elements trailing the string
		StringBuilder textFromInput = new StringBuilder(textString);
		
		boolean jumpIfCopy;
		
		//for from the begin of textFromInput to the end+1
		for(int index=0; index< textFromInput.length(); index++)
		{
			supportCreateWord=new StringBuilder();
			jumpIfCopy= false;
		
			//while founds letters, appends it to the supportCreateWord
			while(Character.isLetter((textFromInput.charAt(index))))
			{
				supportCreateWord.append(textFromInput.charAt(index++));
			}
			
			//if supportCreateWord is a word with less of 3 char, is not new word
			if (supportCreateWord.length()<3)
			{
				continue;
			}
			
			//if founds a copy not create another CheckBox object
			for(int control=0; control<checkBoxArray.size(); control++)
			{
				if(checkBoxArray.get(control).getText().toString().equals(supportCreateWord.toString()))
				{
					jumpIfCopy=true;
					break;
				}
			}
			
			if(!jumpIfCopy)
			{
				//create new checkBox with the word in supportCreateWord and put this into the checkBoxArray
				supportWord= new CheckBox(this);
				supportWord.setText(supportCreateWord);
				supportWord.setTypeface(type,0);
				supportWord.setTextColor(TEXT_LISTWORDS_COLOR);
				supportWord.setTextSize(TEXT_LISTWORDS_SIZE);
				supportWord.setChecked(true);
				checkBoxArray.add(supportWord);
			}
		}		
	}
	
	/**
	 * @see onActivityResult() on Android Activity class
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		
			/**
			 * Open a chose file
			 * 1. takes the file
			 * 2. controls is a .txt file
			 * 3. read the file
			 * 4. show file in editText
			 */
			case REQUEST_CHOOSER:
				if(resultCode == RESULT_OK)
				{
					final Uri uri = data.getData();
					file = FileUtils.getFile(uri);
					
					String supportCorrectName=file.getName();
					
					if(!(supportCorrectName.substring(supportCorrectName.length()-4).equals(".txt")) )
					{
						Log.e("string", supportCorrectName.substring(supportCorrectName.length()-4) );
						Toast.makeText(getApplicationContext(), 
									"Error: not text file selected, \nselect .txt file",
									Toast.LENGTH_LONG).show();
						return;
					}
					
					StringBuilder text = new StringBuilder();
					BufferedReader buffer=null;
					try
					{
						buffer = new BufferedReader(new FileReader(file));
						String line;
						
						while ((line = buffer.readLine()) != null)
						{
							text.append(line);
							text.append('\n');
						}
					} 
					catch(IOException e) 
					{
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					} 
					finally 
					{
						try 
						{
							if (buffer != null) 
								buffer.close();
						} 
						catch (IOException e) 
						{
							Toast.makeText(getApplicationContext(), e.getMessage(), 
																				Toast.LENGTH_SHORT).show();
						}
					}
					
					//if input file is successful
					editText.setText(text);
				}
		}
	}
}
