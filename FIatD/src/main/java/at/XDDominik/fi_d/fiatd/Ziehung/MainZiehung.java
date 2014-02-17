package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Spinner;

import at.XDDominik.fi_d.fiatd.Database;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by Dominik on 17.02.14.
 */
public class MainZiehung  extends Activity{
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ziehung);

        db = new Database(this);
        db.open();

        Spinner ziehungen = (Spinner)findViewById(R.id.zieh_ziehung);
        ZiehungsAdapter zadp = new ZiehungsAdapter(this,db.getZiehungCursor(),false);
        ziehungen.setAdapter(zadp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
