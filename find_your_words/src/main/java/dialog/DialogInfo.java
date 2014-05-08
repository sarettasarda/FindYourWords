/**
 * Class DialogSaveFile is a Dialog used in the first Activity to save a txt file
 * In this dialog the user can write the name of the file e save it in the SDcard
 * 
 * @author Sara Craba
 * @version 1.0
 */

package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.sara.find_your_words.R;

/**
 * @see DialogFragment() on Android
 */
public class DialogInfo extends DialogFragment 
{
	private TextView allTextView;
	private View view;
	
	//font implementation	
	private Typeface type;
	private static final int TEXT_SIZE=20;
		
	/**
	 * @see onCreateDialog() on Android Dialogs class
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		//get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		//get view
		view=inflater.inflate(R.layout.dialog_info, null);
		
		type =Typeface.createFromAsset(getActivity().getAssets(),"jennifer.ttf");
		setText(R.id.infoAdd);
		setText(R.id.infoOpen);
		setText(R.id.infoDelete);
		setText(R.id.infoSave);
		setText(R.id.infoDeselect);
		setText(R.id.infoSelect);
		setText(R.id.infoPlay);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// inflate and set the layout for the dialog
		// pass null as the parent view because its going in the dialog layout
		builder.setView(view)
		//button Ok save the file
		.setPositiveButton("Done", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				DialogInfo.this.getDialog().cancel();
			}
		});
		return builder.create();
	}
	
	private void setText(int id){
		allTextView= (TextView) view.findViewById(id);
		allTextView.setTypeface(type,0);
		allTextView.setTextSize(TEXT_SIZE);
	}
}
