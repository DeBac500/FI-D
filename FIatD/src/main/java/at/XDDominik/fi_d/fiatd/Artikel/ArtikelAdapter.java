package at.XDDominik.fi_d.fiatd.Artikel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import at.XDDominik.fi_d.fiatd.R;

/**
 * Verwaltet den Adapter für Artikel
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class ArtikelAdapter extends CursorAdapter {
    private mainArtikel main;
    private LayoutInflater inflater;

    /**
     * Erstellt den Adapter für Artikel
     */
    public ArtikelAdapter(mainArtikel context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.main=context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_view_4, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1;
        tv1 = (TextView)view.findViewById(R.id.beschreibung);
        //tv2 = (TextView)view.findViewById(R.id.nummer);
        tv1.setText(cursor.getString(cursor.getColumnIndex("Bezeichnung")));
        //tv2.setText(cursor.getString(cursor.getColumnIndex("Bezeichnung")));
        tv1.setFocusable(false);
        //tv2.setFocusable(false);
    }
}
