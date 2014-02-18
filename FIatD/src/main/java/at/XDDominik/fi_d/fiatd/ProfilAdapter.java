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
 * Created by dominik on 18.02.14.
 */
public class ProfilAdapter extends CursorAdapter{
    private Activity main;
    private LayoutInflater inflater;

    public ProfilAdapter(Activity context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.main=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.profile_list, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1;
        tv1 = (TextView)view.findViewById(R.id.pro_tv1);
        tv1.setText(cursor.getString(cursor.getColumnIndex("PName")));
        tv1.setFocusable(false);
    }
}
