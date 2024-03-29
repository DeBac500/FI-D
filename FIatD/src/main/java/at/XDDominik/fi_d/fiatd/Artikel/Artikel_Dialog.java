package at.XDDominik.fi_d.fiatd.Artikel;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Verwaltet den Dialog für Artikel
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class Artikel_Dialog extends DialogFragment{
    private Database db;
    private Probenzieher_Dialog_Listener mListener;
    private final View v;

    /**
     * Erstellt den Dialog für Artikel
     */
    public Artikel_Dialog(Database db, Activity a){
        this.db = db;
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.neuerartikel,null);
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
                            EditText nr=(EditText)v.findViewById(R.id.nartnr);
                            EditText besch=(EditText)v.findViewById(R.id.nartbesch);
                            EditText ean = (EditText)v.findViewById(R.id.narteanc);
                            CheckBox bio = (CheckBox)v.findViewById(R.id.nartbio);
                            int anr = Integer.parseInt(nr.getText().toString());
                            System.out.println("NR: " + anr);


                            int bioc = 0;
                            if(bio.isChecked())
                                bioc = 1;
                            if(nr.getHint() != null){
                                int oanr=Integer.parseInt(nr.getHint().toString());
                                db.exeSQL("UPDATE Artikel SET ArtNr=" + anr + ",Bezeichnung=\"" + besch.getText() + "\",EANCode=" + ean.getText().toString() + ",Bio=" + bioc + " WHERE ArtNr=" + oanr);
                            }else
                                db.exeSQL("INSERT INTO Artikel (ArtNr, Bezeichnung, EANCode, Bio) VALUES ("+anr+", \""+besch.getText()+"\", "+ean.getText().toString()+", "+bioc+")");
                            mListener.onDialogPositiveClick(Artikel_Dialog.this);
                        }catch(NumberFormatException e){
                            Toast.makeText(Artikel_Dialog.this.getActivity(),"Bitte Zahlen eingeben",Toast.LENGTH_SHORT).show();
                        }catch(SQLiteConstraintException e){}
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Artikel_Dialog.this.getDialog().cancel();
                    }
                })
                .setNeutralButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            EditText nr = (EditText) v.findViewById(R.id.nartnr);
                            if(nr.getHint()!=null){
                                int oanr = Integer.parseInt(nr.getHint().toString());
                                db.exeSQL("DELETE FROM Artikel WHERE ArtNr=" + oanr);
                                mListener.onDialogPositiveClick(Artikel_Dialog.this);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(Artikel_Dialog.this.getActivity(), "Konnte nicht gelöscht werden!\nBitte nochmal versuchen!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return builder.create();
    }

    /**
     * Setzt die EAN
     */
    public void setEAN(String ean){
        EditText eantv = (EditText)v.findViewById(R.id.narteanc);
        eantv.setText(ean);
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
     * Bereitet vor und setzt die Standardwerte für den Dialog
     */
    public void setup(Cursor c){
        EditText nr=(EditText)v.findViewById(R.id.nartnr);
        EditText besch=(EditText)v.findViewById(R.id.nartbesch);
        EditText ean = (EditText)v.findViewById(R.id.narteanc);
        CheckBox bio = (CheckBox)v.findViewById(R.id.nartbio);
        
        nr.setText(c.getString(c.getColumnIndex("ArtNr")));
        besch.setText(c.getString(c.getColumnIndex("Bezeichnung")));
        ean.setText(c.getString(c.getColumnIndex("EANCode")));

        nr.setHint(c.getString(c.getColumnIndex("ArtNr")));
        besch.setHint(c.getString(c.getColumnIndex("Bezeichnung")));
        ean.setHint(c.getString(c.getColumnIndex("EANCode")));

        Integer s = c.getInt(c.getColumnIndex("Bio"));
        if(s > 0 )
            bio.setChecked(true);
        else if(s == 0)
            bio.setChecked(false);

    }

    /**
     * Callback-Interface
     */
    public interface Probenzieher_Dialog_Listener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

}
