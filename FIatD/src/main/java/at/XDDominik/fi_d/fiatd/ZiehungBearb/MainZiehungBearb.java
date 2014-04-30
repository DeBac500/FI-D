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
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Erstellt das Fenster zum Ziehung bearbeiten
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class MainZiehungBearb extends Activity {
    private Database db;
    private Spinner ziehungen;
    private ZiehungsAdapter za;
    private ZiehungsAdapter za1;
    private ArrayList<Integer> toz = new ArrayList<Integer>(),ausz = new ArrayList<Integer>();
    private Cursor tozc, auszc;

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
        za = new ZiehungsAdapter(this,db.getArtikelCursor(),false,lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox)view.findViewById(R.id.check);
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                if(!cb.isChecked()){
                    MainZiehungBearb.this.toz.add(c.getPosition());
                    MainZiehungBearb.this.tozc = c;
                    cb.setChecked(!cb.isChecked());
                }else{
                    MainZiehungBearb.this.toz.remove((Integer) c.getPosition());
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
        lv.setAdapter(za);

        ListView lv1 = (ListView)findViewById(R.id.list_inziehung);
        za1 = new ZiehungsAdapter(this,db.getArtikelCursor(),false,lv1);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox)view.findViewById(R.id.check);
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                if(!cb.isChecked()){
                    MainZiehungBearb.this.ausz.add(c.getPosition());
                    MainZiehungBearb.this.auszc = c;
                    cb.setChecked(!cb.isChecked());
                }else{
                    MainZiehungBearb.this.ausz.remove((Integer) c.getPosition());
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
        lv1.setAdapter(za1)  ;
        ImageButton links = (ImageButton)findViewById(R.id.links);
        ImageButton rechts = (ImageButton)findViewById(R.id.rechts);

        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < MainZiehungBearb.this.ausz.size();i++){
                    Integer temp = MainZiehungBearb.this.ausz.get(i);
                    Cursor tempc = MainZiehungBearb.this.auszc;
                    tempc.moveToPosition(temp);
                    if(temp != null)
                        MainZiehungBearb.this.db.removePD(tempc);
                    else
                        Toast.makeText(MainZiehungBearb.this.getBaseContext(), "Temp null" , Toast.LENGTH_SHORT).show();
                }
                MainZiehungBearb.this.za1.swapCursor(MainZiehungBearb.this.db.getprobeninZ((Cursor)MainZiehungBearb.this.ziehungen.getSelectedItem()));
                MainZiehungBearb.this.za.swapCursor(MainZiehungBearb.this.db.getArtikelex((Cursor)MainZiehungBearb.this.ziehungen.getSelectedItem()));
                MainZiehungBearb.this.za1.setallboxf();
                MainZiehungBearb.this.za.setallboxf();
                MainZiehungBearb.this.ausz.clear();
                MainZiehungBearb.this.toz.clear();
            }
        });

        rechts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < MainZiehungBearb.this.toz.size();i++){
                    Integer temp = MainZiehungBearb.this.toz.get(i);
                    Cursor tempc = MainZiehungBearb.this.tozc;
                    tempc.moveToPosition(temp);
                    Cursor sp = null;
                    if(MainZiehungBearb.this.ziehungen!=null)
                        sp = (Cursor)MainZiehungBearb.this.ziehungen.getSelectedItem();
                    else
                        Toast.makeText(MainZiehungBearb.this.getBaseContext(), "s null", Toast.LENGTH_SHORT).show();
                    if(temp != null)
                        if(sp != null)
                            MainZiehungBearb.this.db.addPD(sp,tempc);
                        else
                            Toast.makeText(MainZiehungBearb.this.getBaseContext(), "sp null" , Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainZiehungBearb.this.getBaseContext(), "Temp null" , Toast.LENGTH_SHORT).show();
                }
                MainZiehungBearb.this.za1.swapCursor(MainZiehungBearb.this.db.getprobeninZ((Cursor) MainZiehungBearb.this.ziehungen.getSelectedItem()));
                MainZiehungBearb.this.za.swapCursor(MainZiehungBearb.this.db.getArtikelex((Cursor) MainZiehungBearb.this.ziehungen.getSelectedItem()));
                MainZiehungBearb.this.za1.setallboxf();
                MainZiehungBearb.this.za.setallboxf();
                MainZiehungBearb.this.ausz.clear();
                MainZiehungBearb.this.toz.clear();
            }
        });

        ziehungen = (Spinner)findViewById(R.id.list_ziehung);
        ZiehungsAdapterSpinner zadp = new ZiehungsAdapterSpinner(this,db.getZiehungCursor(),false);
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
    @Override
    public void onResume(){
        ((CursorAdapter)ziehungen.getAdapter()).swapCursor(db.getZiehungCursor());
        super.onResume();
    }

    /**
     * Gibt die Datenbank zurück
     */
    public Database getDB(){return this.db;}

    /**
     * Erstellt einen Adapter für Sachen in der Ziehung
     */
    public ZiehungsAdapter getZiehungsAdapterInZ(){
        return this.za1;
    }

    /**
     * Erstellt einen Adapter für alles
     */
    public ZiehungsAdapter getZiehungsAdapterAll(){
        return this.za;
    }
}