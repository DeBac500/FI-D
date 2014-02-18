package at.XDDominik.fi_d.fiatd.Kunde;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 17.02.14.
 */
public class Kunde_Dialog extends DialogFragment{
    private Database db;
    private Probenzieher_Dialog_Listener mListener;
    private final View v;
    public Kunde_Dialog(Database db, Activity a){
        this.db = db;
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.neukunde,null);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText hh=(EditText)v.findViewById(R.id.nkna);
                        EditText hh1=(EditText)v.findViewById(R.id.nknu);
                        if(hh.getHint() != null)
                            db.exeSQL("UPDATE Kunde SET KName=\""+hh.getText()+"\",KNummer=\""+hh1.getText()+"\" WHERE KNummer=\"" + hh1.getHint()+"\"");
                        else
                            db.exeSQL("INSERT INTO Kunde VALUES (\""+hh1.getText()+"\",\""+hh.getText()+"\")");
                        mListener.onDialogPositiveClick(Kunde_Dialog.this);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Kunde_Dialog.this.getDialog().cancel();
                    }
                })
                .setNeutralButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText h=(EditText)v.findViewById(R.id.nknu);
                        db.exeSQL("DELETE FROM Kunde WHERE KNummer=\"" + h.getHint()+"\"");
                        mListener.onDialogPositiveClick(Kunde_Dialog.this);
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
            mListener = (Probenzieher_Dialog_Listener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement Probenzieher_Dialog_Listener");
        }
    }
    public void setup(Cursor c){
        EditText hh=(EditText)v.findViewById(R.id.nkna);
        EditText hh1=(EditText)v.findViewById(R.id.nknu);
        hh.setText(c.getString(c.getColumnIndex("KName")));
        hh.setHint(c.getString(c.getColumnIndex("KName")));
        hh1.setText(c.getString(c.getColumnIndex("KNummer")));
        hh1.setHint(c.getString(c.getColumnIndex("KNummer")));
    }
    public interface Probenzieher_Dialog_Listener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

}
