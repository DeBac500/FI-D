package at.XDDominik.fi_d.fiatd.Unterschrift;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by dominik on 25.02.14.
 */
public class UnterschLVA extends Activity {
    public static String KDatum = "KDatum", KName = "KName",load = "laden";
    public static int Sucsess = 1234567890;

    private Database db;
    private PaintView pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unter);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        String kdatum,kname;
        boolean load;
        Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                kdatum= null;
                kname = null;
                load= false;
            } else {
                kdatum= extras.getString(UnterschLVA.KDatum);
                kname= extras.getString(UnterschLVA.KName);
                load = extras.getBoolean(UnterschLVA.load);
            }
        } else {
            kdatum= (String) savedInstanceState.getSerializable(UnterschLVA.KDatum);
            kname= (String) savedInstanceState.getSerializable(UnterschLVA.KName);
            load = savedInstanceState.getBoolean(UnterschLVA.load);
        }
        if(kname != null && kdatum  != null){
            TextView tv = (TextView)findViewById(R.id.un_text);
            tv.setText("Unterschrift LVA für Ziehung: " +kdatum +" " + kname );

            pp = (PaintView)findViewById(R.id.view);
            pp.setType("LVA");
            pp.setKdatum(kdatum);
            pp.setKname(kname);
            if(load){
                pp.load();
            }
            Button neu = (Button)findViewById(R.id.unneu);
            neu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pp.clear();
                }
            });
        }else{
            //TODO Return abbrechen
        }

        Button b = (Button)findViewById(R.id.unabb);
        Button b1 = (Button)findViewById(R.id.unsave);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                UnterschLVA.this.setResult(Activity.RESULT_CANCELED, returnIntent);
                UnterschLVA.this.finish();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnterschLVA.this.pp.save();
                Intent returnIntent = new Intent();
                UnterschLVA.this.setResult(Activity.RESULT_OK, returnIntent);
                UnterschLVA.this.finish();
            }
        });
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