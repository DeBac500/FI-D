package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Greift auf die Kamera zu
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class CameraActivity extends Activity {

    private static final String LOG_TAG = CameraActivity.class.getSimpleName();

    private boolean done = true;
    File sdDir;
    String mCurrentPhotoPath, name;
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
                File image = createImageFile(artnr,kunde,dat);
                dispatchTakePictureIntent(image);

           }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }

    }

    /**
     * Erstellt eine Bild-Datei
     */
    private File createImageFile(String artnr , String kunde ,String dat) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = artnr + "_" + kunde + "_" + dat+ "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File ff = new File(storageDir.getPath() + File.separator  + "LVA"+ File.separator);
        ff.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                ff      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        name = image.getName();
        return image;
    }

    /**
     * 
     */
    private void dispatchTakePictureIntent(File photoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 0);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            galleryAddPic();
            Intent intent = new Intent();
            intent.putExtra("bild", name);
            setResult(RESULT_OK,intent);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Fügt ein Bild zur Gallerie hinzu
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}