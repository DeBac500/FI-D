package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import at.XDDominik.fi_d.fiatd.Artikel.ArtikelAdapter;

/**
 * Created by dominik on 02.04.14.
 */
public class FotoView extends Activity {
    private Database db;
    private ArtikelAdapter pa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fotoview);

        db = new Database(this);
        db.open();

        Tabs tabs = new Tabs(this);
        tabs.initTab(0);
        tabs.unselect();

        Button back = (Button)findViewById(R.id.fotoback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FotoView.this.finish();
            }
        });

        String name;
        Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                name="";
            } else {
                name= extras.getString("Name");
            }
        } else {
            name= savedInstanceState.getString("Name");
        }
        if(name != null){
            if(name != "" && name.endsWith(".jpg")){
                TextView tv = (TextView)findViewById(R.id.bildtxt);
                ImageView bild = (ImageView)findViewById(R.id.image);
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File im = new File(storageDir.getPath() + File.separator + "LVA" + File.separator + name);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                if(im.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(im.getAbsolutePath(), options);
                    System.out.println("W:" + bitmap.getWidth());
                    System.out.println("H:" + bitmap.getHeight());
                    int nh = (int) ( bitmap.getHeight() * (500.0 / bitmap.getWidth()) );
                    System.out.println(nh);
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 500, nh, true);
                    tv.setText(im.getName());
                    bild.setImageBitmap(scaled);
                }


            }
        }

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}