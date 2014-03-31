package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.ProfilAdapter;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Reciever;
import at.XDDominik.fi_d.fiatd.TCPConnection;
import at.XDDominik.fi_d.fiatd.Tabs;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschKunde;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschLVA;
import at.XDDominik.fi_d.fiatd.Verbindung;
import at.XDDominik.fi_d.fiatd.ZiehungBearb.NeueZiehung;

/**
 * Created by Dominik on 17.02.14.
 */
public class MainZiehung  extends Activity implements Reciever, Finish_Dialog.Finish_Listener{
    private Database db;

    private Verbindung feedTask;

    private ProbenAdapter proa;
    private Spinner ziehungen, profile;
    private CheckBox lastcb;
    private Cursor lastc;
    private Button kund,lva;
    private ArrayList<Integer> finpd = new ArrayList<Integer>();
    private boolean unlva = false, unkund = false;
    public Cursor ziehc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ziehung);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);

        feedTask = new Verbindung(this,this);
        feedTask.execute(new String[] { "hello", "hello" });

        Button com = (Button)findViewById(R.id.zieh_commit);
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastc != null){
                    if(MainZiehung.this.unkund && MainZiehung.this.unlva){
                        Finish_Dialog newFragment = new Finish_Dialog(MainZiehung.this);
                        newFragment.show(getFragmentManager(), "Ziehung Beenden");
                    }else
                        Toast.makeText(MainZiehung.this,"Bitte Ziehung unterschreiben!",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainZiehung.this,"Ziehung nicht angefangen!",Toast.LENGTH_SHORT).show();

                //Send data
            }
        });

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
                intent.putExtra(UnterschLVA.KDatum,((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv1)).getText());
                intent.putExtra(UnterschLVA.KName,((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv2)).getText());
                intent.putExtra(UnterschLVA.load,MainZiehung.this.unlva);
                MainZiehung.this.startActivityForResult(intent, 2);
            }
        });
        lva.setBackgroundColor(Color.RED+1);

        ImageButton img = (ImageButton)findViewById(R.id.zieh_scan);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainZiehung.this);
                scanIntegrator.initiateScan();
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
    public Database getDB(){return this.db;}
    public ProbenAdapter getPA(){return  this.proa;}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                lastcb.setChecked(true);
                this.finpd.add(lastc.getPosition());
            }
            if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(this, "Nein", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                this.unlva=true;
                lva.setBackgroundColor(((Color.GREEN)+100));
            }
            if (resultCode == RESULT_CANCELED) {
                //lva.setBackgroundColor(Color.RED+1);
            }
        }
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                this.unkund = true;
                kund.setBackgroundColor(((Color.GREEN)+100));
            }
            if (resultCode == RESULT_CANCELED) {
                //kund.setBackgroundColor(Color.RED+1);
            }
        }
        if(requestCode == 4){
            if(resultCode == RESULT_OK){
                Bundle extras;
                extras = data.getExtras();
                String zdatum, ztime, zkvname,zname;
                int zknummer;
                zdatum = extras.getString("ZDatum");
                ztime =extras.getString("ZTime");
                zkvname =extras.getString("ZKVName");
                zknummer=extras.getInt("ZKNummer");
                zname = extras.getString("ZName");
                if(lastc != null){
                    lastc.moveToFirst();
                    do{
                        if(!finpd.contains(lastc.getPosition())){
                            System.out.println("NewZ: " + lastc.getPosition());
                            String sql = "UPDATE Probendaten SET Ziehungsdatum=\""+zdatum+"\",Ziehungszeit=\""+ztime+"\",KVName=\""+zkvname+"\",KNummer="+zknummer+",Name=\""+zname+"\" WHERE " +
                                    "ArtNr=" + lastc.getInt(lastc.getColumnIndex("ArtNr"))+" AND Ziehungsdatum=\""+lastc.getString(lastc.getColumnIndex("Ziehungsdatum"))+"\" AND Ziehungszeit=\""+lastc.getString(lastc.getColumnIndex("Ziehungszeit"))+"\" AND KVName=\""+lastc.getString(lastc.getColumnIndex("KVName"))+"\" AND KNummer="+lastc.getInt(lastc.getColumnIndex("KVName"))+" AND Name=\""+lastc.getString(lastc.getColumnIndex("Name"))+"\"";
                            db.exeSQL(sql);
                        }
                    }while(lastc.moveToNext());
                    this.ziehungen.invalidate();

                    //Verbindung.syncZiehungen(this.db,this,this.feedTask);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //kund.setBackgroundColor(Color.RED+1);
            }
        }else{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                //String scanFormat = scanningResult.getFormatName();
                Toast.makeText(getApplicationContext(),"Content: " + scanContent, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void removeall(){
        this.finpd.clear();
    }

    @Override
    public void handleIn(Object o) {

    }
    @Override
    public void finish(){
        TCPConnection tcp = null;
        if(feedTask != null)
            tcp = feedTask.getTCP();
        if(tcp != null)
            tcp.close();
        super.finish();
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String preis, boolean neu) {
        String sql ="UPDATE Probenziehung SET Status=1,Preis="+preis+" WHERE Ziehungsdatum=\""+lastc.getString(lastc.getColumnIndex("Ziehungsdatum"))+"\" AND Ziehungszeit=\""+lastc.getString(lastc.getColumnIndex("Ziehungszeit"))+"\" AND KVName=\""+lastc.getString(lastc.getColumnIndex("KVName"))+"\" AND KNummer="+lastc.getInt(lastc.getColumnIndex("KVName"))+" AND Name=\""+lastc.getString(lastc.getColumnIndex("Name"))+"\"";
        db.exeSQL(sql);
        if(neu){
            Intent intent = new Intent(MainZiehung.this,NeueZiehung.class);
            intent.putExtra("Update",true);
            MainZiehung.this.startActivityForResult(intent, 4);
        }else
            Toast.makeText(getApplicationContext(),"Ziehung erfolgreich beendet!", Toast.LENGTH_SHORT).show();
    }
}