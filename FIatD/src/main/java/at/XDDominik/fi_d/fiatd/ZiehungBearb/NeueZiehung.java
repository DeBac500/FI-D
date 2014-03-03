package at.XDDominik.fi_d.fiatd.ZiehungBearb;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by dominik on 26.02.14.
 */
public class NeueZiehung extends Activity {
    private Database db;
    private KVAdapter kva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neueziehung);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        Button b = (Button)findViewById(R.id.nziehsave);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    NeueZiehung a = NeueZiehung.this;
                    Spinner k = (Spinner)a.findViewById(R.id.nziehk);
                    Spinner kv = (Spinner)a.findViewById(R.id.nziehkv);
                    Spinner pz = (Spinner)a.findViewById(R.id.nziehpz);
                    DatePicker date = (DatePicker)a.findViewById(R.id.nziehdate);
                    TimePicker time = (TimePicker)a.findViewById(R.id.nziehtime);
                    EditText ort = (EditText)a.findViewById(R.id.nziehort);

                    String datet = ""+date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
                    String timet = ""+time.getCurrentHour()+":"+time.getCurrentMinute()+":00";
                    TextView t = (TextView)k.getSelectedView().findViewById(R.id.pro_tv1);
                    int kt = Integer.parseInt(t.getHint().toString());
                    t = (TextView)kv.getSelectedView().findViewById(R.id.pro_tv1);
                    String kvt = t.getText().toString();
                    t = (TextView)pz.getSelectedView().findViewById(R.id.pro_tv1);
                    String pzt = t.getText().toString();

                    a.getDB().exeSQL("INSERT INTO Probenziehung (KVName, KNummer, Name, Ziehungsdatum, Ziehungszeit, Ziehungsort, Preis, Status) " +
                                "VALUES (\""+kvt+"\", "+kt+", \""+pzt+"\", \""+datet+"\", \""+timet+"\", \""+ort.getText()+"\","+0+" , 1)");
                    NeueZiehung.this.onBackPressed();
                }catch(NumberFormatException e){
                    Toast.makeText(NeueZiehung.this, "Bitte Zahlen eingeben", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button b1 = (Button)findViewById(R.id.nziehabb);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NeueZiehung.this.onBackPressed();
            }
        });

        TimePicker tp =(TimePicker)findViewById(R.id.nziehtime);
        Calendar cal= Calendar.getInstance();
        tp.setIs24HourView(true);
        tp.setCurrentHour(tp.getCurrentHour()+12);

        Spinner k = (Spinner)findViewById(R.id.nziehk);
        KAdapter ka = new KAdapter(this, db.getKunden(), false);
        k.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                NeueZiehung.this.kva.swapCursor(NeueZiehung.this.db.getKVinK(c));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        k.setAdapter(ka);
        Spinner kv = (Spinner)findViewById(R.id.nziehkv);
        kva = new KVAdapter(this, db.getKundenVertreter(), false);
        kv.setAdapter(kva);
        Spinner pz = (Spinner)findViewById(R.id.nziehpz);
        PZAdapter pza = new PZAdapter(this, db.getProbenZieher(), false);
        pz.setAdapter(pza);
        //Spinner sp4 = (Spinner)findViewById(R.id.nziehort);
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
}