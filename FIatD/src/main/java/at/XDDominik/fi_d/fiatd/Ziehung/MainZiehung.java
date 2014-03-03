package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.ProfilAdapter;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschKunde;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschLVA;

/**
 * Created by Dominik on 17.02.14.
 */
public class MainZiehung  extends Activity{
    private Database db;
    private ProbenAdapter proa;
    private Spinner ziehungen, profile;
    private CheckBox lastcb;
    private Cursor lastc;
    private Button kund,lva;
    private ArrayList<Integer> finpd = new ArrayList<Integer>();

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
        proa = new ProbenAdapter(this,db.getArtikelCursor(),false,proben);
        proben.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastcb = (CheckBox)view.findViewById(R.id.check);
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                lastc = c;
                Intent inte = new Intent(MainZiehung.this,EditeProbeactivity.class);
                inte.putExtra(EditeProbeactivity.profname,((Cursor)MainZiehung.this.profile.getSelectedItem()).getPosition());
                inte.putExtra(EditeProbeactivity.data,c.getPosition());
                inte.putExtra(EditeProbeactivity.zieh,((Cursor)MainZiehung.this.ziehungen.getSelectedItem()).getPosition());
                MainZiehung.this.startActivityForResult(inte, 1);
            }
        });
        proben.setAdapter(proa);

        ziehungen = (Spinner)findViewById(R.id.zieh_ziehung);
        ZiehungsAdapter zadp = new ZiehungsAdapter(this,db.getZiehungCursor(),false);
        ZiehungsItemListener zil = new ZiehungsItemListener(this);
        ziehungen.setOnItemSelectedListener(zil);
        ziehungen.setAdapter(zadp);

        profile = (Spinner)findViewById(R.id.zieh_profil);
        ProfilAdapter profa = new ProfilAdapter(this,db.getProfil(),false);
        profile.setAdapter(profa);

        kund = (Button)findViewById(R.id.zieh_unkund);
        kund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainZiehung.this, UnterschKunde.class);
                intent.putExtra(UnterschKunde.KDatum, ((TextView) ziehungen.getSelectedView().findViewById(R.id.spin1_tv1)).getText());
                intent.putExtra(UnterschKunde.KName, ((TextView) ziehungen.getSelectedView().findViewById(R.id.spin1_tv2)).getText());
                MainZiehung.this.startActivityForResult(intent, 3);
            }
        });
        kund.setBackgroundColor(Color.RED+1);
        lva = (Button)findViewById(R.id.zieh_uncomp);
        lva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainZiehung.this,UnterschLVA.class);
                intent.putExtra(UnterschKunde.KDatum,((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv1)).getText());
                intent.putExtra(UnterschKunde.KName,((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv2)).getText());
                MainZiehung.this.startActivityForResult(intent, 2);
            }
        });
        lva.setBackgroundColor(Color.RED+1);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                lastcb.setChecked(true);
                this.finpd.add(lastc.getPosition());
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Nein", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                lva.setBackgroundColor(Color.GREEN);
            }
            if (resultCode == RESULT_CANCELED) {
                lva.setBackgroundColor(Color.RED+1);
            }
        }
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                kund.setBackgroundColor(Color.GREEN);
            }
            if (resultCode == RESULT_CANCELED) {
                kund.setBackgroundColor(Color.RED+1);
            }
        }
    }
    public void removeall(){
        this.finpd.clear();
    }
}
