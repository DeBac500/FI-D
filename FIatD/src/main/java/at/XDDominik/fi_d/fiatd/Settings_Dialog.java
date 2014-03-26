package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Ist fuer das Anzeigen des Menues unter dem Settings-Button zustaendig
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class Settings_Dialog extends DialogFragment{
    private final View v;
    private Activity a;
    
    /**
     * Erstellt eine Instanz der Klasse und 
     * @param a 
     */
    public Settings_Dialog(Activity a){
        this.a = a;
        LayoutInflater inflater = a.getLayoutInflater();
        v = inflater.inflate(R.layout.settings,null);
        try {
            FileInputStream fis = a.openFileInput(MainActivity.config);
            Properties pro = new Properties();
            pro.load(fis);
            EditText ip = (EditText)v.findViewById(R.id.sett_ip);
            EditText port = (EditText)v.findViewById(R.id.sett_port);
            ip.setText(pro.getProperty(MainActivity.ip));
            port.setText(pro.getProperty(MainActivity.port));
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
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
                        EditText ip = (EditText) v.findViewById(R.id.sett_ip);
                        EditText port = (EditText) v.findViewById(R.id.sett_port);
                        try {
                            Properties pro = new Properties();
                            FileInputStream fis = a.openFileInput(MainActivity.config);
                            pro.load(fis);
                            pro.setProperty(MainActivity.ip,ip.getText().toString());
                            pro.setProperty(MainActivity.port,port.getText().toString());
                            fis.close();
                            FileOutputStream fos = a.openFileOutput(MainActivity.config, Context.MODE_PRIVATE);
                            pro.store(fos,null);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Settings_Dialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
