package at.XDDominik.fi_d.fiatd.ZiehungBearb;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import at.XDDominik.fi_d.fiatd.R;

/**
 * Listenadapter der dafür sorgt dass die Elemente in der Liste dargestellt 
 * werden
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class ZiehungsAdapterSpinner extends CursorAdapter{
    private MainZiehungBearb main;
    private LayoutInflater inflater;

    /**
     * Erstellt den Adapter für Ziehung
     */
    public ZiehungsAdapterSpinner(MainZiehungBearb context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.main=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return inflater.inflate(R.layout.spinner_view, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1, tv2;
        tv1 = (TextView)view.findViewById(R.id.spin1_tv1);
        tv2 = (TextView)view.findViewById(R.id.spin1_tv2);
        tv1.setText(cursor.getString(cursor.getColumnIndex("Ziehungsdatum")));
        tv2.setText(cursor.getString(cursor.getColumnIndex("KName")));
        if(cursor.getInt(cursor.getColumnIndex("Status"))==1)
            view.setBackgroundColor(Color.GREEN+100);
        else
            view.setBackgroundColor(Color.parseColor("#e7e7e8"));
        tv1.setFocusable(false);
        tv2.setFocusable(false);
    }
}
