package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by dominik on 03.03.14.
 */
public class EditeProbeactivity extends Activity {
    public static String profname = "ProfielName", data = "Probendaten", zieh = "Ziehung";

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        int prof, data, zieh;
        Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                prof= -1;
                data= -1;
                zieh = -1;
            } else {
                prof= extras.getInt(EditeProbeactivity.profname);
                data= extras.getInt(EditeProbeactivity.data);
                zieh= extras.getInt(EditeProbeactivity.zieh);
            }
        } else {
            prof= savedInstanceState.getInt(EditeProbeactivity.profname);
            data= savedInstanceState.getInt(EditeProbeactivity.data);
            zieh= savedInstanceState.getInt(EditeProbeactivity.zieh);
        }
        if(prof != -1 && data != -1&& zieh != -1){
            Cursor c = db.getProfil();
            c.moveToPosition(prof);
            EditeProbe e = new EditeProbe(this,c);
            Cursor c1 = db.getZiehungCursor();
            c1.moveToPosition(zieh);
            Cursor c2 = db.getprobeninZ(c1);
            c2.moveToPosition(data);
            e.setStand(c2);
            e.setKunde(c1.getString(c1.getColumnIndex("KName")));
            setContentView(e);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = MainActivity.OptionsItemSelected(item, this);
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
    public Database getDB(){return this.db;}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            if(resultCode == RESULT_OK)
                Toast.makeText(this,"Foto gemacht",Toast.LENGTH_SHORT);
            if(resultCode == RESULT_CANCELED)
                Toast.makeText(this,"Abbgebrochen",Toast.LENGTH_SHORT);
        }
    }
}
