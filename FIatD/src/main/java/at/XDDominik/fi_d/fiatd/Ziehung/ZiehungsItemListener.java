package at.XDDominik.fi_d.fiatd.Ziehung;

import android.view.View;
import android.widget.AdapterView;

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
