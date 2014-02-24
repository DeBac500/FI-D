package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.ProfilAdapter;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by Dominik on 17.02.14.
 */
public class MainZiehung  extends Activity{
    private Database db;
    private ProbenAdapter proa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ziehung);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);

        ListView proben = (ListView)findViewById(R.id.zieh_list);
        proa = new ProbenAdapter(this,db.getArtikelCursor(),false);
        proben.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent inte = new Intent();
            }
        });
        proben.setAdapter(proa);

        Spinner ziehungen = (Spinner)findViewById(R.id.zieh_ziehung);
        ZiehungsAdapter zadp = new ZiehungsAdapter(this,db.getZiehungCursor(),false);
        ZiehungsItemListener zil = new ZiehungsItemListener(this);
        ziehungen.setOnItemSelectedListener(zil);
        ziehungen.setAdapter(zadp);

        Spinner profile = (Spinner)findViewById(R.id.zieh_profil);
        ProfilAdapter profa = new ProfilAdapter(this,db.getProfil(),false);
        profile.setAdapter(profa);


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
    public Database getDB(){return this.db;}
    public ProbenAdapter getPA(){return  this.proa;}
}
