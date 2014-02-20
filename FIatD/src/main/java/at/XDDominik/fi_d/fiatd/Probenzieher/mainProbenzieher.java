package at.XDDominik.fi_d.fiatd.Probenzieher;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.MainActivity;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Tabs;

/**
 * Created by dominik on 18.02.14.
 */
public class mainProbenzieher extends Activity implements Probenzieher_Dialog.Probenzieher_Dialog_Listener{
    private Database db;
    private PZAdapter pa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_samplepuller);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        ListView profile = (ListView)findViewById(R.id.list_probenzieher);
        pa = new PZAdapter(this,db.getProbenZieher(),false);
        profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor)adapterView.getItemAtPosition(i);
                Probenzieher_Dialog newFragment = new Probenzieher_Dialog(db,mainProbenzieher.this);
                newFragment.setup(c);
                newFragment.show(getFragmentManager(), "Probenzieher");
            }
        });
        profile.setAdapter(pa);

        Button b1 = (Button)findViewById(R.id.setting_addprobenzieher);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Probenzieher_Dialog newFragment = new Probenzieher_Dialog(db,mainProbenzieher.this);
                newFragment.show(getFragmentManager(), "Probenzieher");
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        pa.swapCursor(db.getProbenZieher());
    }
}