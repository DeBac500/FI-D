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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Erstellt das Fenster für eine Neue Ziehung
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class NeueZiehung extends Activity {
    private Database db;
    private KVAdapter kva;
    private boolean update;
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

        Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                update= false;
            } else {
                update= extras.getBoolean("Update");

            }
        } else {
            update = savedInstanceState.getBoolean("Update");
        }
        if(update)
            Toast.makeText(getBaseContext(), "Ziehung für nicht abgeschlossene Proben" , Toast.LENGTH_SHORT).show();

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
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String datet = ""+date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
                    Date d = df.parse(datet);
                    String timet = ""+time.getCurrentHour()+":"+time.getCurrentMinute()+":00";
                    TextView t = (TextView)k.getSelectedView().findViewById(R.id.pro_tv1);
                    int kt = Integer.parseInt(t.getHint().toString());
                    t = (TextView)kv.getSelectedView().findViewById(R.id.pro_tv1);
                    String kvt = t.getText().toString();
                    t = (TextView)pz.getSelectedView().findViewById(R.id.pro_tv1);
                    String pzt = t.getText().toString();

                    a.getDB().exeSQL("INSERT INTO Probenziehung (KVName, KNummer, Name, Ziehungsdatum, Ziehungszeit, Ziehungsort, Preis, Status) " +
                                "VALUES (\""+kvt+"\", "+kt+", \""+pzt+"\", \""+df.format(d)+"\", \""+timet+"\", \""+ort.getText()+"\","+0+" , 0)");
                    if(update){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ZDatum",datet);
                        returnIntent.putExtra("ZTime",timet);
                        returnIntent.putExtra("ZKVName",kvt);
                        returnIntent.putExtra("ZKNummer",kt);
                        returnIntent.putExtra("ZName",pzt);
                        NeueZiehung.this.setResult(Activity.RESULT_OK, returnIntent);
                        NeueZiehung.this.finish();
                    }else{
                        NeueZiehung.this.finish();
                    }
                }catch(NumberFormatException e){
                    Toast.makeText(NeueZiehung.this, "Bitte Zahlen eingeben", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button b1 = (Button)findViewById(R.id.nziehabb);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update){
                    Intent returnIntent = new Intent();
                    NeueZiehung.this.setResult(Activity.RESULT_CANCELED, returnIntent);
                    NeueZiehung.this.finish();
                }else{
                    NeueZiehung.this.finish();
                }
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

    /**
     * Gibt die Datenbank zurück
     */
    public Database getDB(){return this.db;}
}