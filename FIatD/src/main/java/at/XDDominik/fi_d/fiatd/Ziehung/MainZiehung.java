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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import Server.InvalidParameterException;
import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.ProfilAdapter;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Reciever;
import at.XDDominik.fi_d.fiatd.TCPConnection;
import at.XDDominik.fi_d.fiatd.Tabs;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschKunde;
import at.XDDominik.fi_d.fiatd.Unterschrift.UnterschLVA;
import at.XDDominik.fi_d.fiatd.ZiehungBearb.NeueZiehung;
import at.XDDominik.fi_d.fiatd.task;

/**
 * Created by Dominik on 17.02.14.
 */
public class MainZiehung  extends Activity implements Reciever{
    private Database db;

    private task feedTask;

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

        feedTask = new task(this);
        feedTask.execute(new String[] { "hello", "hello" });

        Button com = (Button)findViewById(R.id.zieh_commit);
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastc != null){
                String sql ="UPDATE Probenziehung SET Status=1 WHERE Ziehungsdatum=\""+lastc.getString(lastc.getColumnIndex("Ziehungsdatum"))+"\" AND Ziehungszeit=\""+lastc.getString(lastc.getColumnIndex("Ziehungszeit"))+"\" AND KVName=\""+lastc.getString(lastc.getColumnIndex("KVName"))+"\" AND KNummer="+lastc.getInt(lastc.getColumnIndex("KVName"))+" AND Name=\""+lastc.getString(lastc.getColumnIndex("Name"))+"\"";
                db.exeSQL(sql);
                Intent intent = new Intent(MainZiehung.this,NeueZiehung.class);
                intent.putExtra("Update",true);
                MainZiehung.this.startActivityForResult(intent, 4);
                }else
                    Toast.makeText(MainZiehung.this,"Ziehung nicht angefangen!!",Toast.LENGTH_SHORT).show();

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
                this.unlva=true;
                lva.setBackgroundColor(Color.GREEN);
            }
            if (resultCode == RESULT_CANCELED) {
                //lva.setBackgroundColor(Color.RED+1);
            }
        }
        if(requestCode == 3){
            if(resultCode == RESULT_OK){
                this.unkund = true;
                kund.setBackgroundColor(Color.GREEN);
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
                    LinkedList<ProbenDaten> pdl = new LinkedList<ProbenDaten>();
                    lastc.moveToFirst();
                    System.out.println("Size: " + finpd.size());
                    do{
                        if(finpd.contains(lastc.getPosition())){
                            System.out.println("Save: " + lastc.getPosition());
                            int anz = lastc.getInt(lastc.getColumnIndex("Packungszahl"));
                            int cn = lastc.getInt(lastc.getColumnIndex("Chargennummer"));
                            int ln = lastc.getInt(lastc.getColumnIndex("LieferNr"));
                            int arn = lastc.getInt(lastc.getColumnIndex("ArtNr"));
                            String lieferant = lastc.getString(lastc.getColumnIndex("Lieferant"));
                            String besch = lastc.getString(lastc.getColumnIndex("Probenbeschreibung"));
                            String b2bnr = lastc.getString(lastc.getColumnIndex("B2BNr"));
                            String groesse = lastc.getString(lastc.getColumnIndex("Packungsgroesse"));
                            String bezeichnung = lastc.getString(lastc.getColumnIndex("Bezeichnung"));
                            String eancode = lastc.getString(lastc.getColumnIndex("EANCode"));
                            String bildp  = "";
                            String mhd = lastc.getString(lastc.getColumnIndex("MHD"));
                            boolean bio =false;
                            SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

                            ProbenDaten pd = null;
                            try {
                                pd = new ProbenDaten(anz,simp.parse(mhd),cn,ln,lieferant,besch,b2bnr,groesse,arn,bezeichnung,eancode,bio,bildp);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            pdl.add(pd);
                        }else{
                            System.out.println("NewZ: " + lastc.getPosition());
                            String sql = "UPDATE Probendaten SET Ziehungsdatum=\""+zdatum+"\",Ziehungszeit=\""+ztime+"\",KVName=\""+zkvname+"\",KNummer="+zknummer+",Name=\""+zname+"\" WHERE " +
                                    "ArtNr=" + lastc.getInt(lastc.getColumnIndex("ArtNr"))+" AND Ziehungsdatum=\""+lastc.getString(lastc.getColumnIndex("Ziehungsdatum"))+"\" AND Ziehungszeit=\""+lastc.getString(lastc.getColumnIndex("Ziehungszeit"))+"\" AND KVName=\""+lastc.getString(lastc.getColumnIndex("KVName"))+"\" AND KNummer="+lastc.getInt(lastc.getColumnIndex("KVName"))+" AND Name=\""+lastc.getString(lastc.getColumnIndex("Name"))+"\"";
                            db.exeSQL(sql);
                        }
                    }while(lastc.moveToNext());
                    Probenziehung pz = null;
                    try {
                        Cursor c = db.getZiehungAll(this.ziehc);
                        c.moveToNext();
                        if(false){
                        for(int i = 0; i < c.getColumnCount();i++){
                            System.out.println("ColName: "+ c.getColumnName(i) + " = " + c.getString(i));
                        }
                        }
                        if(true){
                            String pribenzieher = c.getString(c.getColumnIndex("Name"));
                            Kunde k = new Kunde(c.getInt(c.getColumnIndex("KNummer")),c.getString(c.getColumnIndex("KName")));
                            KundenVertreter kunde = new KundenVertreter(c.getString(c.getColumnIndex("KVName")),k);
                            SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date d =  simp.parse(c.getString(c.getColumnIndex("Ziehungsdatum")) + " " + c.getString(c.getColumnIndex("Ziehungszeit")));
                            String ort = c.getString(c.getColumnIndex("Ziehungsort"));
                            double preis = c.getDouble(c.getColumnIndex("Preis"));
                            int status = 0;
                            pz = new Probenziehung(pdl,pribenzieher,kunde,d,ort,preis,status);
                        }
                    } catch (InvalidParameterException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    LinkedList<Probenziehung> pzl = new LinkedList<Probenziehung>();
                    pzl.add(pz);
                    System.out.println(pdl.toString());
                    TCPConnection tcp = null;
                    if(feedTask != null)
                        tcp = feedTask.getTCP();
                    if(tcp != null){
                        tcp.sendMessage("saveAll:");
                        tcp.sendMessage(pzl);
                    }
                    sendData();
                    this.ziehungen.invalidate();
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //kund.setBackgroundColor(Color.RED+1);
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
    public void sendData(){
        if(MainZiehung.this.unlva){
            try {
                //Send LVAun
                String name = "LVA" + "_" + ((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv2)).getText() + "_" + ((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv1)).getText() + ".jpg";
                FileInputStream fis = openFileInput(name);
                byte[] arr = new byte[fis.available()];
                fis.read(arr);
                TCPConnection tcp = null;
                if(feedTask != null)
                    tcp = feedTask.getTCP();
                if(tcp != null){
                    tcp.sendMessage("image:" + name);
                    tcp.sendMessage(arr);
                }
                fis.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(MainZiehung.this.unkund){
            try {
                //Send KundeUN
                String name = "Kunde" + "_" + ((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv2)).getText() + "_" + ((TextView)ziehungen.getSelectedView().findViewById(R.id.spin1_tv1)).getText() + ".jpg";
                FileInputStream fis = openFileInput(name);
                byte[] arr = new byte[fis.available()];
                fis.read(arr);
                TCPConnection tcp = null;
                if(feedTask != null)
                    tcp = feedTask.getTCP();
                if(tcp != null){
                    tcp.sendMessage("image:" + name);
                    tcp.sendMessage(arr);
                }
                fis.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}