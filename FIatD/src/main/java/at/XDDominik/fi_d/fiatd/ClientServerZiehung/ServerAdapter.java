package at.XDDominik.fi_d.fiatd.ClientServerZiehung;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 05.03.14.
 */
public class ServerAdapter extends ArrayAdapter<Probenziehung>{
    private MainClientServerZiehung main;
    private LayoutInflater inflater;
    private ArrayList<CheckBox> box = new ArrayList<CheckBox>();
    private ListView mv;
    private List<Probenziehung> val;

    public ServerAdapter(MainClientServerZiehung context, int textViewResourceId, List<Probenziehung> objects,ListView lv) {
        super(context, textViewResourceId, objects);
        this.main = context;
        this.mv = lv;
        this.val = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_view, parent, false);
        TextView tv1 = (TextView) rowView.findViewById(R.id.beschreibung);
        TextView tv2 = (TextView) rowView.findViewById(R.id.nummer);

        SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
        tv1.setText(df.format(val.get(position).getDatum()));
        tv2.setText(val.get(position).getKunde().getKunde().getKundenname());

        box.add((CheckBox)rowView.findViewById(R.id.check));

        return rowView;
    }
    public void setallboxf(){
        for(CheckBox temp:box)
            temp.setChecked(false);
        mv.postInvalidate();
    }
    @Override
    public int getCount(){
        return val.size();
    }
}
