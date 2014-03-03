package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.R;

/**
 * Created by dominik on 04.12.13.
 */
public class EditeProbe extends ScrollView {
    private EditeProbeactivity context;
    private ArrayList<EditText> input;
    private ArrayList<TextView> output;
    private EditeProbeactivity a;
    public EditeProbe(EditeProbeactivity context, Cursor profil) {
        super(context);
        this.a = context;
        this.context = context;
        setFocusable(true);
        TableRow rr = new TableRow(context);
        TableRow rr2 = new TableRow(context);
        input = new ArrayList<EditText>();
        output = new ArrayList<TextView>();

        String[] art = context.getResources().getStringArray(R.array.Artikel);
        String[] dat = context.getResources().getStringArray(R.array.Probendaten);
        ArrayList<String> all = new ArrayList<String>();
        for(int i = 0; i<art.length;i++)
            if(profil != null){
                //System.out.println("Test  "+ i + " :=  " + profil.getInt(profil.getColumnIndex(dat[i].split(",")[0])));
                if(0 != profil.getInt(profil.getColumnIndex(dat[i].split(",")[0]))){

                    all.add(dat[i]);
                }
            }else
                all.add(dat[i]);
        for(int i = 0; i < dat.length;i++)
            if(!all.contains(dat[i]))
                if(profil != null){
                    //System.out.println("Test  "+ i + " :=  " + profil.getInt(profil.getColumnIndex(dat[i].split(",")[0])));
                    if(0 != profil.getInt(profil.getColumnIndex(dat[i].split(",")[0]))){

                        all.add(dat[i]);
                    }
                }else
                    all.add(dat[i]);
        int mid = (all.size())/2;

        TableLayout tl = new TableLayout(context);
        TableLayout tl2 = new TableLayout(context);

        for(int i = 0; i < all.size(); i++){
            if(i <= mid){
                TableRow temp = addRow(i,all.get(i).split(",")[0]);
                tl.addView(temp);
                TableLayout.LayoutParams par = (TableLayout.LayoutParams)temp.getLayoutParams();
                par.setMargins(0,5,0,0);
                temp.setLayoutParams(par);
            }else{
                TableRow temp = addRow(i,all.get(i).split(",")[0]);
                tl2.addView(temp);
                TableLayout.LayoutParams par = (TableLayout.LayoutParams)temp.getLayoutParams();
                par.setMargins(0,5,0,0);
                temp.setLayoutParams(par);
            }
        }
        rr.addView(tl);
        TableRow.LayoutParams params = (TableRow.LayoutParams)tl.getLayoutParams();
        params.setMargins(50,50,50,50);
        tl.setLayoutParams(params);
        rr.addView(tl2);
        TableRow.LayoutParams params2 = (TableRow.LayoutParams)tl2.getLayoutParams();
        params2.setMargins(50,50,50,50);
        tl2.setLayoutParams(params2);
        Button comit, cancle;
        comit = new Button(context);
        comit.setText("Bestätigen");
        cancle =  new Button(context);
        cancle.setText("Abbrechen");
        rr2.addView(cancle);
        rr2.addView(comit);
        TableRow.LayoutParams params3 = (TableRow.LayoutParams)comit.getLayoutParams();
        params3.setMargins(50,50,50,50);
        comit.setLayoutParams(params3);
        TableRow.LayoutParams params4 = (TableRow.LayoutParams)cancle.getLayoutParams();
        params4.setMargins(50,50,50,50);
        cancle.setLayoutParams(params4);
        TableLayout tlll = new TableLayout(context);
        tlll.addView(rr);
        tlll.addView(rr2);
        addView(tlll);
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                EditeProbe.this.context.setResult(Activity.RESULT_CANCELED, returnIntent);
                EditeProbe.this.context.finish();
            }
        });
        comit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO save

                Intent returnIntent = new Intent();
                EditeProbe.this.context.setResult(Activity.RESULT_OK, returnIntent);
                EditeProbe.this.context.finish();
            }
        });
        cancle.requestFocus();
        this.setHorizontalScrollBarEnabled(true);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    public TableRow addRow(int i , String text){
        TableRow tr = new TableRow(context);
        output.add(new TextView(context.getApplicationContext()));
        output.get(i).setText(text);
        output.get(i).setTextColor(Color.BLACK);
        output.get(i).setTextSize(25);
        input.add(new EditText(context.getApplicationContext()));
        input.get(i).setText("");
        //input.get(i).setHighlightColor(Color.BLUE);
        input.get(i).setTextColor(Color.BLACK);
        input.get(i).setTextSize(25);
        input.get(i).setSingleLine(true);
        input.get(i).setHint("Werte eingeben");
        input.get(i).setWidth(300);
        input.get(i).setBackgroundColor(Color.LTGRAY);
        tr.addView(output.get(i));
        tr.addView(input.get(i));
        return tr;
    }
    public void setStand(Cursor c){
        //c.move((int)i);
        for(int ii = 0; ii < input.size();ii++){
            input.get(ii).setHint(c.getString(c.getColumnIndex((String)output.get(ii).getText())));
            input.get(ii).setText(c.getString(c.getColumnIndex((String)output.get(ii).getText())));
        }
    }
}
