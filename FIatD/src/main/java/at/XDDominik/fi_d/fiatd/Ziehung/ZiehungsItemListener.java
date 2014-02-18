package at.XDDominik.fi_d.fiatd.Ziehung;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Dominik on 17.02.14.
 */
public class ZiehungsItemListener implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private MainZiehung main;
    public ZiehungsItemListener(MainZiehung main){
        this.main =main;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = (Cursor)parent.getItemAtPosition(position);
        main.getPA().swapCursor(main.getDB().getprobeninZ(c));
        //Toast.makeText(main.getBaseContext(), "Spinner an:" + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
