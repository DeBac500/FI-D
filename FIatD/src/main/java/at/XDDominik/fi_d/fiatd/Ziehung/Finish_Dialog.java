package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import at.XDDominik.fi_d.fiatd.R;

/**
 * Verwaltet den Dialog für eine fertiggestellte Ziehung
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class Finish_Dialog extends DialogFragment{
    private Finish_Listener mListener;
    private final View v;

    /**
     * Erstellt den Dialog für eine fertiggestellte Ziehung
     */
    public Finish_Dialog( Activity a){
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.finish,null);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView tv = (TextView)v.findViewById(R.id.preis);
                        CheckBox c = (CheckBox)v.findViewById(R.id.fin_neueziehung);
                        mListener.onDialogPositiveClick(Finish_Dialog.this,tv.getText().toString(),c.isChecked());
                    }
                }).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Finish_Dialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (Finish_Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement Finish_Listener");
        }
    }

    /**
     * Callback-Interface
     */
    public interface Finish_Listener {
        public void onDialogPositiveClick(DialogFragment dialog,String preis ,boolean neu);
    }

}
