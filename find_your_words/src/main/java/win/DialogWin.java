/**
 * Class DialogSaveFile is a Dialog used in the first Activity to save a txt file
 * In this dialog the user can write the name of the file e save it in the SDcard
 * 
 * @author Sara Craba
 * @version 1.0
 */

package win;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.sara.find_your_words.R;

/**
 * @see DialogFragment() on Android
 */
public class DialogWin extends DialogFragment 
{
	static WinView image;
	private View view;
	
	static DialogWin istance;
	static DialogWinThread thread= new DialogWinThread();;
		
	/**
	 * @see onCreateDialog() on Android Dialogs class
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);        
		
		
		//get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		//get view
		view=inflater.inflate(R.layout.dialog_win, null);
		
		istance=this;

		image = (WinView)view.findViewById(R.id.winView1);
		
		thread.start();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		
		return builder.create();
	}
}
