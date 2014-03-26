package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * 
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class ProfilAdapter extends CursorAdapter{
    private Activity main;
    private LayoutInflater inflater;
    private boolean bb;
    
    /**
     * 
     */
    public ProfilAdapter(Activity context, Cursor c, boolean autoRequery) {
        super(context, c, false);
        this.main=context;
        bb = autoRequery;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * 
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if(bb)
            return inflater.inflate(R.layout.profile_list_2, parent,false);
        else
            return inflater.inflate(R.layout.profile_list, parent,false);
    }

    /**
     * 
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1;
        tv1 = (TextView)view.findViewById(R.id.pro_tv1);
        tv1.setText(cursor.getString(cursor.getColumnIndex("PName")));
        tv1.setFocusable(false);
    }
}
