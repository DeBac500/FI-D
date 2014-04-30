package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import at.XDDominik.fi_d.fiatd.Artikel.mainArtikel;
import at.XDDominik.fi_d.fiatd.ClientServerZiehung.MainClientServerZiehung;
import at.XDDominik.fi_d.fiatd.Kunde.mainKunde;
import at.XDDominik.fi_d.fiatd.Kundenvertreter.mainKundevertreter;
import at.XDDominik.fi_d.fiatd.Probenzieher.mainProbenzieher;
import at.XDDominik.fi_d.fiatd.Profil.MainProfil;
import at.XDDominik.fi_d.fiatd.Ziehung.MainZiehung;
import at.XDDominik.fi_d.fiatd.ZiehungBearb.MainZiehungBearb;

/**
 * 
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class MainActivity extends Activity{
    public static String config = "config.properties",ip = "IP-Address", port = "Server-Port";
    private Menu menu;
    /**
     * 
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextpz = (Button)findViewById(R.id.welc_next_pz);
        nextpz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainZiehung.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        Button nextcsz = (Button)findViewById(R.id.welc_next_csz);
        nextcsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainClientServerZiehung.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        Button nextzeb = (Button)findViewById(R.id.welc_next_zeb);
        nextzeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainZiehungBearb.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //System.out.println("Test!!!!");

        try {
            FileInputStream fis = openFileInput(MainActivity.config);
            fis.close();
            System.out.println("Prop not created!");
        } catch (FileNotFoundException e) {
            try {
                Properties pro = new Properties();
                pro.setProperty(MainActivity.ip,"127.0.0.1");
                pro.setProperty(MainActivity.port,"4444");
                FileOutputStream fos = openFileOutput(MainActivity.config,MODE_PRIVATE);
                pro.store(fos,null);
                fos.close();
                System.out.println("Prop created!");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    /**
     * 
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * 
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = MainActivity.OptionsItemSelected(item,this);
        if(!b)
            return super.onOptionsItemSelected(item);
        else
            return b;
    }
    
    /**
     * 
     * @param item
     * @param a
     * @return
     */
    public static boolean OptionsItemSelected(MenuItem item,Activity a) {
        // Handle presses on the action bar items
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Settings_Dialog setting = new Settings_Dialog(a);
                setting.show(a.getFragmentManager(),"Settings");
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
            case R.id.action_sync:
                Toast.makeText(a,"Starting sync",Toast.LENGTH_SHORT).show();
                Database db = new Database(a);
                db.open();
                Syncer s = new Syncer(db,a);
                Verbindung v = new Verbindung(a,s);
                s.setVerbindung(v);
                v.execute();
                Toast.makeText(a,"Sync started",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
