package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class CameraActivity extends Activity {

    private static final String LOG_TAG = CameraActivity.class.getSimpleName();

    private boolean done = true;
    File sdDir;

    protected static final String PHOTO_TAKEN = "photo_taken";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            String artnr;
            String kunde;
            String dat;
            Bundle extras;
            if (savedInstanceState == null) {
                extras = getIntent().getExtras();
                if(extras == null) {
                    artnr= "";
                    dat= "";
                    kunde = "";
                } else {
                    artnr= extras.getString("ARTNR");
                    dat= extras.getString("DATA");
                    kunde= extras.getString("KUNDE");
                }
            } else {
                artnr= savedInstanceState.getString("ARTNR");
                dat= savedInstanceState.getString("DATA");
                kunde= savedInstanceState.getString("KUNDE");
            }
            if((artnr != null && dat != null && kunde != null) && (artnr != "" && dat != "" && kunde != "") ){
                File root = new File("sdcard" + File.separator + "LVA" + File.separator);
                root.mkdirs();
                sdDir = new File(root, artnr + "_" + kunde + "_" + dat + ".jpg");
                Log.d(LOG_TAG, "Creating image storage file: " + sdDir.getPath());
                startCameraActivity();
           }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (Exception e) {
            finish();
        }

    }
    protected void startCameraActivity() {
        Uri outputFileUri = Uri.fromFile(sdDir);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        intent.putExtra("uri", sdDir.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(CameraActivity.PHOTO_TAKEN)) {
            done = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(CameraActivity.PHOTO_TAKEN,  done);
    }


}