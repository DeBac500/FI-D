package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import at.XDDominik.fi_d.fiatd.Artikel.mainArtikel;
import at.XDDominik.fi_d.fiatd.Kunde.mainKunde;
import at.XDDominik.fi_d.fiatd.Kundenvertreter.mainKundevertreter;
import at.XDDominik.fi_d.fiatd.Probenzieher.mainProbenzieher;
import at.XDDominik.fi_d.fiatd.Profil.MainProfil;
import at.XDDominik.fi_d.fiatd.Ziehung.MainZiehung;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button next = (Button)findViewById(R.id.welc_next);
        next.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainZiehung.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = MainActivity.OptionsItemSelected(item,this);
        if(!b)
            return super.onOptionsItemSelected(item);
        else
            return b;
    }

    public static boolean OptionsItemSelected(MenuItem item,Activity a) {
        // Handle presses on the action bar items
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
            case android.R.id.home:

                return true;
            case R.id.action_profile:
                intent = new Intent(a, MainProfil.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.startActivity(intent);
                a.finish();
                a.overridePendingTransition(0,0);
                return true;
            case R.id.action_samplepuller:
                intent = new Intent(a, mainProbenzieher.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.startActivity(intent);
                a.finish();
                a.overridePendingTransition(0,0);
                return true;
            case R.id.action_customer:
                intent = new Intent(a, mainKunde.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.startActivity(intent);
                a.finish();
                a.overridePendingTransition(0,0);
                return true;
            case R.id.action_decoy:
                intent = new Intent(a, mainKundevertreter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.startActivity(intent);
                a.finish();
                a.overridePendingTransition(0,0);
                return true;
            case R.id.action_artikel:
                intent = new Intent(a, mainArtikel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                a.startActivity(intent);
                a.finish();
                a.overridePendingTransition(0,0);
                return true;
            default:
                return false;
        }
    }
}
