package at.XDDominik.fi_d.fiatd.Probenzieher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 17.02.14.
 */
public class Probenzieher_Dialog extends DialogFragment{
    private Database db;
    private Probenzieher_Dialog_Listener mListener;
    private final View v;
    public Probenzieher_Dialog(Database db,Activity a){
        this.db = db;
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.neueprobenzieher,null);
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
                        try{

                        EditText gg=(EditText)v.findViewById(R.id.npzn);
                        if(gg.getHint()!=null){
                            db.exeSQL("UPDATE Probenzieher SET Name=\"" + gg.getText() + "\" WHERE Name=\"" + gg.getHint() + "\"");
                        }else{
                            //Toast.makeText(a, "comit", Toast.LENGTH_SHORT).show();
                            db.exeSQL("INSERT INTO Probenzieher VALUES (\"" + gg.getText() + "\")");
                        }
                        mListener.onDialogPositiveClick(Probenzieher_Dialog.this);
                        }catch(SQLiteConstraintException e){}
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Probenzieher_Dialog.this.getDialog().cancel();
                    }
                })
                .setNeutralButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText g = (EditText) v.findViewById(R.id.npzn);
                        if (!g.getHint().toString().equalsIgnoreCase(""))
                            db.exeSQL("DELETE FROM Probenzieher WHERE Name=\"" + g.getHint() + "\"");
                        mListener.onDialogPositiveClick(Probenzieher_Dialog.this);
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
        EditText g = (EditText) v.findViewById(R.id.npzn);
        g.setText(c.getString(c.getColumnIndex("Name")));
        g.setHint(c.getString(c.getColumnIndex("Name")));
    }
    public interface Probenzieher_Dialog_Listener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

}
