package at.XDDominik.fi_d.fiatd.Kundenvertreter;

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
import android.widget.Spinner;
import android.widget.TextView;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Verwaltet den Dialog für Kundenvertreter
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class Kundevertreter_Dialog extends DialogFragment{
    private Database db;
    private Probenzieher_Dialog_Listener mListener;
    private final View v;

    /**
     * Erstellt den Dialog für Kundenvertreter
     */
    public Kundevertreter_Dialog(Database db, Activity a){
        this.db = db;
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.neuekv,null);
        Spinner s = (Spinner)v.findViewById(R.id.nkvk);
        KundenAdapter k = new KundenAdapter(a,db.getKunden(),false);
        s.setAdapter(k);
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
                        EditText kvn=(EditText)v.findViewById(R.id.nkvn);
                        EditText kvkd=(EditText)v.findViewById(R.id.nkvkd);
                        Spinner k = (Spinner)v.findViewById(R.id.nkvk);
                        TextView kt = (TextView)k.getSelectedView().findViewById(R.id.beschreibung);
                        int ktt = Integer.parseInt(kt.getHint().toString());
                        if(kvn.getHint() != null){
                            int kdt = Integer.parseInt(kvkd.getText().toString());
                            db.exeSQL("UPDATE Kundenvertreter SET KVName=\"" + kvn.getText() + "\",KNummer=" + ktt + " WHERE KNummer=" + kdt + " AND KVName=\"" + kvn.getHint() + "\"");
                        }else
                            db.exeSQL("INSERT INTO Kundenvertreter VALUES (\"" + kvn.getText() + "\"," + ktt + ")");
                        mListener.onDialogPositiveClick(Kundevertreter_Dialog.this);
                        }catch(SQLiteConstraintException e){}
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Kundevertreter_Dialog.this.getDialog().cancel();
                    }
                })
                .setNeutralButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText kvn1 = (EditText) v.findViewById(R.id.nkvn);
                        EditText kvkd1 = (EditText) v.findViewById(R.id.nkvkd);
                        if(kvn1.getHint()!= null || kvkd1.getHint() !=null){
                            int kdt = Integer.parseInt(kvkd1.getText().toString());
                            db.exeSQL("DELETE FROM Kundenvertreter WHERE KNummer=" + kdt + " AND KVName=\"" + kvn1.getHint() + "\"");
                            mListener.onDialogPositiveClick(Kundevertreter_Dialog.this);
                        }
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

    /**
     * Bereitet den Dialog vor
     */
    public void setup(Cursor c){
        EditText kvn=(EditText)v.findViewById(R.id.nkvn);
        EditText kvkd=(EditText)v.findViewById(R.id.nkvkd);
        kvn.setText(c.getString(c.getColumnIndex("KVName")));
        kvn.setHint(c.getString(c.getColumnIndex("KVName")));
        kvkd.setText(c.getString(c.getColumnIndex("KNummer")));
        Spinner k = (Spinner)v.findViewById(R.id.nkvk);
        for(int i = 0; i < k.getCount();i++){
            Cursor cc = (Cursor)k.getItemAtPosition(i);
            if(cc.getString(cc.getColumnIndex("KNummer")).equalsIgnoreCase(c.getString(c.getColumnIndex("KNummer")))){
                k.setSelection(i);
                break;
            }
        }
    }

    /**
     * Callback-Interface
     */
    public interface Probenzieher_Dialog_Listener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

}
