package at.XDDominik.fi_d.fiatd.Ziehung;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 18.02.14.
 */
public class ProbenAdapter extends CursorAdapter{
    private MainZiehung main;
    private LayoutInflater inflater;
    private ArrayList<CheckBox> box = new ArrayList<CheckBox>();
    private ListView mv;

    public ProbenAdapter(MainZiehung context, Cursor c, boolean autoRequery,ListView mv) {
        super(context, c, autoRequery);
        this.main=context;
        this.inflater = LayoutInflater.from(context);
        this.mv = mv;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_view, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1,tv2;
        tv1 = (TextView)view.findViewById(R.id.beschreibung);
        tv2 = (TextView)view.findViewById(R.id.nummer);
        tv1.setText(cursor.getString(cursor.getColumnIndex("ArtNr")));
        tv2.setText(cursor.getString(cursor.getColumnIndex("Bezeichnung")));
        tv1.setFocusable(false);
        tv2.setFocusable(false);

        CheckBox c = (CheckBox)view.findViewById(R.id.check);
        c.setClickable(false);
        c.setChecked(false);
        this.box.add(c);
    }

    public void setallboxf(){
        for(CheckBox temp:box)
            temp.setChecked(false);
        mv.postInvalidate();
    }
    @Override
    public Cursor swapCursor(Cursor newCursor){
        this.setallboxf();
        this.box.clear();
        return super.swapCursor(newCursor);
    }
}
