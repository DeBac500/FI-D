package at.XDDominik.fi_d.fiatd.ZiehungBearb;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by dominik on 24.02.14.
 */
public class MainZiehungBearb extends Activity {
    private Database db;
    private Spinner ziehungen;
    private ZiehungsAdapter za;
    private ZiehungsAdapter za1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ziehungbearb);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(1);
        Button b = (Button)findViewById(R.id.addziehung);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainZiehungBearb.this,NeueZiehung.class);
                MainZiehungBearb.this.startActivity(intent);
            }
        });
        ListView lv = (ListView)findViewById(R.id.list_probe);
        za = new ZiehungsAdapter(this,db.getArtikelCursor(),false);
        lv.setAdapter(za);

        ListView lv1 = (ListView)findViewById(R.id.list_inziehung);
        za1 = new ZiehungsAdapter(this,db.getArtikelCursor(),false);
        lv1.setAdapter(za1);

        ziehungen = (Spinner)findViewById(R.id.list_ziehung);
        ZiehungsAdapterSpinner zadp = new ZiehungsAdapterSpinner(this,db.getZiehungCursor(),false);
        //ZiehungsItemListener zil = new ZiehungsItemListener(this);
        ziehungen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                MainZiehungBearb.this.getZiehungsAdapterInZ().swapCursor(MainZiehungBearb.this.getDB().getprobeninZ(c));
                MainZiehungBearb.this.getZiehungsAdapterAll().swapCursor(MainZiehungBearb.this.getDB().getArtikelex(c));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ziehungen.setAdapter(zadp);
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
    public ZiehungsAdapter getZiehungsAdapterInZ(){
        return this.za1;
    }
    public ZiehungsAdapter getZiehungsAdapterAll(){
        return this.za;
    }
}