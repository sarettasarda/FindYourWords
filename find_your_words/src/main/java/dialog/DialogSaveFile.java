/**
 * Class DialogSaveFile is a Dialog used in the first Activity to save a txt file
 * In this dialog the user can write the name of the file e save it in the SDcard
 * 
 * @author Sara Craba
 * @version 1.0
 */

package dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import com.sara.find_your_words.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @see DialogFragment() on Android
 */
public class DialogSaveFile extends DialogFragment 
{
	private EditText fileName;
	private String fileNameString;
	private Dialog myDialog;
	private File file;
	private String textFile= new String();

	/**
	 * @see onCreateDialog() on Android Dialogs class
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		//text to save sent by SetWord activity 
		textFile= getArguments().getString("text");
		
		//get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// inflate and set the layout for the dialog
		// pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_insert_filename, null))
			//button Ok save the file
			.setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					//take file name from edit text in the dialog
					myDialog = (Dialog) dialog;
					fileName= (EditText) myDialog.findViewById(R.id.editFileName);
					fileNameString=fileName.getText().toString();
					//if edit text is null save with default name
					if(fileName.getText().toString().length()==0)
					{
						fileNameString="find_your_words";
					}
					
					//create a .txt file
					file = new File(Environment.getExternalStorageDirectory(), fileNameString+ ".txt");
					try
					{
						file.createNewFile();
						FileOutputStream fOut = new FileOutputStream(file);
						OutputStreamWriter myOutWriter =  new OutputStreamWriter(fOut);
						myOutWriter.append(textFile);
						myOutWriter.close();
						fOut.close();
	                } catch (Exception e) 
	                {
	                	Log.e("DialogSaveFile", e.getMessage());
	                }
					
					Toast.makeText(getActivity().getApplicationContext(), 
									"correctly saved "+ fileNameString+ ".txt", Toast.LENGTH_SHORT).show();
				}
			})
			
			//button Cancel: annul action, return to activity
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id) 
				{
					DialogSaveFile.this.getDialog().cancel();
				}
			});
		return builder.create();
	}
}
