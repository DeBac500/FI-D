package at.XDDominik.fi_d.fiatd.Artikel;

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
import android.widget.Toast;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 17.02.14.
 */
public class Artikel_Dialog extends DialogFragment{
    private Database db;
    private Probenzieher_Dialog_Listener mListener;
    private final View v;
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
                            EditText bio = (EditText)v.findViewById(R.id.nartbio);
                            int anr = Integer.parseInt(nr.getText().toString());
                            System.out.println("NR: " + anr);

                            int eanc = Integer.parseInt(ean.getText().toString());
                            int bioc = 0;
                            if(bio.getText().toString().equalsIgnoreCase("Ja"))
                                bioc = 1;
                            else if(bio.getText().toString().equalsIgnoreCase("Nein"))
                                bioc = 0;
                            else{
                                Toast.makeText(Artikel_Dialog.this.getActivity(), "Bitte ja oder nein eungaben", Toast.LENGTH_SHORT).show();
                            }
                            if(nr.getHint() != null){
                                int oanr=Integer.parseInt(nr.getHint().toString());
                                db.exeSQL("UPDATE Artikel SET ArtNr=" + anr + ",Bezeichnung=\"" + besch.getText() + "\",EANCode=" + eanc + ",Bio=" + bioc + " WHERE ArtNr=" + oanr);
                            }else
                                db.exeSQL("INSERT INTO Artikel (ArtNr, Bezeichnung, EANCode, Bio) VALUES ("+anr+", \""+besch.getText()+"\", "+eanc+", "+bioc+")");
                            mListener.onDialogPositiveClick(Artikel_Dialog.this);
                        }catch(NumberFormatException e){
                            Toast.makeText(Artikel_Dialog.this.getActivity(),"Bitte Zahlen eingeben",Toast.LENGTH_SHORT).show();
                        }
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
                            int oanr = Integer.parseInt(nr.getHint().toString());
                            db.exeSQL("DELETE FROM Artikel WHERE ArtNr=" + oanr);
                            mListener.onDialogPositiveClick(Artikel_Dialog.this);
                        } catch (NumberFormatException e) {
                            Toast.makeText(Artikel_Dialog.this.getActivity(), "Konnte nicht gelöscht werden!\nBitte nochmal versuchen!", Toast.LENGTH_SHORT).show();
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
    public void setup(Cursor c){
        EditText nr=(EditText)v.findViewById(R.id.nartnr);
        EditText besch=(EditText)v.findViewById(R.id.nartbesch);
        EditText ean = (EditText)v.findViewById(R.id.narteanc);
        EditText bio = (EditText)v.findViewById(R.id.nartbio); 
        
        nr.setText(c.getString(c.getColumnIndex("ArtNr")));
        besch.setText(c.getString(c.getColumnIndex("Bezeichnung")));
        ean.setText(c.getString(c.getColumnIndex("EANCode")));

        nr.setHint(c.getString(c.getColumnIndex("ArtNr")));
        besch.setHint(c.getString(c.getColumnIndex("Bezeichnung")));
        ean.setHint(c.getString(c.getColumnIndex("EANCode")));

        Integer s = c.getInt(c.getColumnIndex("Bio"));
        if(s > 0 )
            bio.setText("Ja");
        else if(s == 0)
            bio.setText("Nein");

    }
    public interface Probenzieher_Dialog_Listener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

}
