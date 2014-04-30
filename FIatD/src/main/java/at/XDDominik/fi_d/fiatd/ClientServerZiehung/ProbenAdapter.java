package at.XDDominik.fi_d.fiatd.ClientServerZiehung;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
 * Listenadapter der dafür sorgt dass die Elemente in der Liste dargestellt 
 * werden
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class ProbenAdapter extends CursorAdapter{
    private MainClientServerZiehung main;
    private LayoutInflater inflater;
    private ArrayList<CheckBox> box = new ArrayList<CheckBox>();
    private ListView mv;

    /**
     * Erstellt den Adapter für Proben
     */
    public ProbenAdapter(MainClientServerZiehung context, Cursor c, boolean autoRequery, ListView mv) {
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
        tv1.setText(cursor.getString(cursor.getColumnIndex("Ziehungsdatum")));
        tv2.setText(cursor.getString(cursor.getColumnIndex("KName")));
        tv1.setFocusable(false);
        tv2.setFocusable(false);
        int stat = cursor.getInt(cursor.getColumnIndex("Status"));
        if(stat == 1){
            view.setBackgroundColor(Color.GREEN+100);
        }else
            view.setBackground(view.getResources().getDrawable(R.drawable.list_selector));
        //System.out.println("Status:" + stat + "\nKname: "+ tv2.getText().toString());

        CheckBox c = (CheckBox)view.findViewById(R.id.check);
        c.setClickable(false);
        c.setChecked(false);
        this.box.add(c);
    }

    /**
     * Setzt alle Checkboxes inaktiv
     */
    public void setallboxf(){
        for(CheckBox temp:box)
            temp.setChecked(false);
        mv.postInvalidate();
    }
}
