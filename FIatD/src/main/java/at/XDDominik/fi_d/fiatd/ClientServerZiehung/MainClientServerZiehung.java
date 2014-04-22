package at.XDDominik.fi_d.fiatd.ClientServerZiehung;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Reciever;
import at.XDDominik.fi_d.fiatd.Saver;
import at.XDDominik.fi_d.fiatd.TCPConnection;
import at.XDDominik.fi_d.fiatd.Tabs;
import at.XDDominik.fi_d.fiatd.Verbindung;

/**
 * Erstellt das Fenster für Client Server Ziehungen
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class MainClientServerZiehung extends Activity implements Reciever{
    private Database db;
    public LinkedList<Probenziehung> pzz = new LinkedList<Probenziehung>();
    private ServerAdapter a2;
    private Verbindung feedTask;
    private ProbenAdapter pa;
    private Cursor fclientc;
    private ArrayList<Integer> fclient = new ArrayList<Integer>();
    public LinkedList<Probenziehung> fserver = new LinkedList<Probenziehung>();
    private ServerReciever sr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_client_server_z);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(2);

        ListView lv1 = (ListView)findViewById(R.id.list_client);
        pa = new ProbenAdapter(this,db.getZiehungCursor(),false,lv1);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox)view.findViewById(R.id.check);
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                if(!cb.isChecked()){
                    MainClientServerZiehung.this.fclient.add(c.getPosition());
                    MainClientServerZiehung.this.fclientc=c;
                    cb.setChecked(!cb.isChecked());
                }else{
                    MainClientServerZiehung.this.fclient.remove((Integer)c.getPosition());
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
        lv1.setAdapter(pa);

        sr = new ServerReciever(this);
        sr.setUp();

        ImageButton links = (ImageButton)findViewById(R.id.links);
        ImageButton rechts = (ImageButton)findViewById(R.id.rechts);

        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saver s = new Saver(MainClientServerZiehung.this.db);
                System.out.println(MainClientServerZiehung.this.fserver);
                s.saveEveryThing(MainClientServerZiehung.this.fserver);
                pa.notifyDataSetChanged();
            }
        });




    }

    /**
     * Setzt die Liste mit den Probenziehungen
     */
    public void setLinkedList(LinkedList<Probenziehung> temp){
        this.pzz = temp;
        a2.notifyDataSetChanged();
    }

    /**
     * Gibt die Liste mit den Probenziehungen zurück
     */
    public LinkedList<Probenziehung> getLinkedList(){return this.pzz;}
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

    /**
     * Gibt die Datenbank zurück
     */
    public Database getDB(){return this.db;}

    @Override
    public void handleIn(Object o) {
        System.out.println(o.toString());


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
}