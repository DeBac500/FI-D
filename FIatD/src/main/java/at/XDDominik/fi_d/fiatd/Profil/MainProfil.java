package at.XDDominik.fi_d.fiatd.Profil;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.ProfilAdapter;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Erstellt das Fenster für Profil
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class MainProfil extends Activity implements Serializable {
    public static String Editprobe = "at.XDDominik.FIatD.EditProbe";
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profile);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        ListView profile = (ListView)findViewById(R.id.list_profil);
        ProfilAdapter pa = new ProfilAdapter(this,db.getProfil(),true);
        profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                EditeProfil p = new EditeProfil(MainProfil.this);
                p.setStand(c,l);
                MainProfil.this.setContentView(p);
            }
        });
        profile.setAdapter(pa);

        Button b1 = (Button)findViewById(R.id.setting_addprof);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditeProfil p = new EditeProfil(MainProfil.this);
                MainProfil.this.setContentView(p);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = MainActivity.OptionsItemSelected(item,this);
        if(!b)
            return super.onOptionsItemSelected(item);
        else
            return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Gibt die Datenbank zurück
     */
    public Database getDB(){return this.db;}
}