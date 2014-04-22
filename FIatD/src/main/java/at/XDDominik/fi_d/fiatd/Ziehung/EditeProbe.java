package at.XDDominik.fi_d.fiatd.Ziehung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.CameraActivity;
import at.XDDominik.fi_d.fiatd.FotoView;
import at.XDDominik.fi_d.fiatd.R;

/**
 * Erstellt die Editieransicht für Probe
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class EditeProbe extends ScrollView {
    private EditeProbeactivity context;
    private ArrayList<EditText> input;
    private ArrayList<TextView> output;
    private EditeProbeactivity a;
    private String bildp = "kein Bild", artnr, dat,kunde, KVName ,KNummer, Name, Ziehungszeit;
    private Button bild;
    private  ArrayList<String> all;

    /**
     * Erstellt die Ansicht
     */
    public EditeProbe(EditeProbeactivity context, Cursor profil) {
        super(context);
        this.a = context;
        this.context = context;
        setFocusable(true);
        TableRow rr = new TableRow(context);
        TableRow rr2 = new TableRow(context);
        input = new ArrayList<EditText>();
        output = new ArrayList<TextView>();

        all = EditeProbe.generateList(context,profil);

        int mid = (all.size())/2;

        TableLayout tl = new TableLayout(context);
        TableLayout tl2 = new TableLayout(context);

        for(int i = 0; i < all.size(); i++){
            if(i <= mid){
                TableRow temp = addRow(i,all.get(i),profil);
                tl.addView(temp);
                TableLayout.LayoutParams par = (TableLayout.LayoutParams)temp.getLayoutParams();
                par.setMargins(0,5,0,0);
                temp.setLayoutParams(par);
            }else{
                TableRow temp = addRow(i,all.get(i),profil);
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
        params3.setMargins(50,10,50,0);
        comit.setLayoutParams(params3);
        TableRow.LayoutParams params4 = (TableRow.LayoutParams)cancle.getLayoutParams();
        params4.setMargins(50,10,50,0);
        cancle.setLayoutParams(params4);
        //Photo
        Button photo = new Button(context);
        //photo.setTextSize(30);
        photo.setText("Foto machen");
        TableRow rr1 = new TableRow(context);

        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditeProbe.this.context,CameraActivity.class);
                intent.putExtra("ARTNR",artnr);
                intent.putExtra("KUNDE",kunde);
                intent.putExtra("DATA",dat);
                EditeProbe.this.context.startActivityForResult(intent, 100);
            }
        });

        bild = new Button(context);
        //photo.setTextSize(30);
        bild.setText(bildp);
        //bild.setBackgroundColor(Color.LTGRAY);
        rr1.addView(photo);
        rr1.addView(bild);
        TableRow.LayoutParams params5 = (TableRow.LayoutParams)photo.getLayoutParams();
        params5.setMargins(50,10,50,0);
        photo.setLayoutParams(params5);
        TableRow.LayoutParams params6 = (TableRow.LayoutParams)bild.getLayoutParams();
        params6.setMargins(50,10,50,0);
        bild.setLayoutParams(params6);
        bild.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditeProbe.this.context,FotoView.class);
                intent.putExtra("Name",EditeProbe.this.bild.getText().toString());
                EditeProbe.this.context.startActivityForResult(intent, 100);
            }
        });
        //end photo
        TableLayout tlll = new TableLayout(context);
        tlll.addView(rr);
        tlll.addView(rr1);
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
                ArrayList<String> numbers = new ArrayList<String>();
                numbers.add("KNummer");
                numbers.add("ArtNr");
                numbers.add("Packungszahl");
                numbers.add("Chargennummer");
                numbers.add("LieferNr");
                SimpleDateFormat dated = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat datet = new SimpleDateFormat("HH:mm:ss");

                String sql = "UPDATE Probendaten SET ";
                for(int  i= 0; i < output.size();i++){
                    if(numbers.contains(output.get(i))){
                        sql += output.get(i).getText() + "=" + input.get(i).getText() + ",";
                    }else{
                        if(all.get(i).contains("DATE")){
                            try {
                                sql += output.get(i).getText() + "=\"" + dated.format(dated.parse(input.get(i).getText().toString())) + "\",";
                            } catch (ParseException e) {}
                        }else if(all.get(i).contains("TIME")){
                            try {
                                sql += output.get(i).getText() + "=\"" + datet.format(datet.parse(input.get(i).getText().toString())) + "\",";
                            } catch (ParseException e) {}
                        }else{
                            sql += output.get(i).getText() + "=\"" + input.get(i).getText() + "\",";
                        }
                    }
                }
                sql += "Bild=\""+ bildp +"\" WHERE ArtNr=" + artnr + " AND KVName=\"" + KVName + "\" AND KNummer=" + KNummer + " AND Name=\"" + Name +
                        "\" AND Ziehungsdatum=\"" + dat + "\" AND Ziehungszeit=\"" + Ziehungszeit + "\"";

                EditeProbe.this.context.getDB().exeSQL(sql);

                Intent returnIntent = new Intent();
                EditeProbe.this.context.setResult(Activity.RESULT_OK, returnIntent);
                EditeProbe.this.context.finish();
            }
        });
        cancle.requestFocus();
        this.setHorizontalScrollBarEnabled(true);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    /**
     * Fügt eine neue Zeile der Ansicht hinzu
     */
    public TableRow addRow(int i , String text,Cursor profil){
        TableRow tr = new TableRow(context);
        output.add(new TextView(context.getApplicationContext()));
        output.get(i).setText(text.split(",")[0]);
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
        setType(input.get(i),text.split(",")[1]);
        tr.addView(output.get(i));
        tr.addView(input.get(i));
        return tr;
    }

    /**
     * Setzt den Typ
     */
    public void setType(EditText text, String para){
        if(para.contains("NUMERIC"))
            text.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(para.contains("TIME"))
            text.setInputType(InputType.TYPE_CLASS_DATETIME);
        if(para.contains("DATE"))
            text.setInputType(InputType.TYPE_CLASS_DATETIME);
    }

    /**
     * Bereitet vor
     */
    public void setStand(Cursor c){
        ArrayList<String> fixwerte = new ArrayList<String>();
        fixwerte.add("ArtNr");
        fixwerte.add("Bezeichnung");
        fixwerte.add("EANCode");
        fixwerte.add("Bio");
        fixwerte.add("KVName");
        fixwerte.add("KNummer");
        fixwerte.add("Name");
        fixwerte.add("Ziehungsdatum");
        fixwerte.add("Ziehungsort");
        fixwerte.add("Preis");
        fixwerte.add("Status");
        fixwerte.add("Ziehungszeit");

        for(int ii = 0; ii < input.size();ii++){
            input.get(ii).setHint(c.getString(c.getColumnIndex((String)output.get(ii).getText())));
            input.get(ii).setText(c.getString(c.getColumnIndex((String)output.get(ii).getText())));
            if(fixwerte.contains((String)output.get(ii).getText())){
                input.get(ii).setInputType(0);
                input.get(ii).setFocusable(false);
            }
        }
        artnr = c.getString(c.getColumnIndex("ArtNr"));
        dat = c.getString(c.getColumnIndex("Ziehungsdatum"));
        KVName = c.getString(c.getColumnIndex("KVName"));
        KNummer = c.getString(c.getColumnIndex("KNummer"));
        Name = c.getString(c.getColumnIndex("Name"));
        Ziehungszeit = c.getString(c.getColumnIndex("Ziehungszeit"));
        bildp = c.getString(c.getColumnIndex("Bild"));
        bild.setText(bildp);
    }

    /**
     * Setzt den Kunden
     */
    public void setKunde(String k){
        this.kunde = k;
    }

    /**
     * Generiert eine Liste
     */
    public static ArrayList<String> generateList(Context context,Cursor profil){
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
            if(!dat[i].contains("Bild"))
            if(!all.contains(dat[i]))
                if(profil != null){
                    //System.out.println("Test  "+ i + " :=  " + profil.getInt(profil.getColumnIndex(dat[i].split(",")[0])));
                    if(0 != profil.getInt(profil.getColumnIndex(dat[i].split(",")[0]))){

                        all.add(dat[i]);
                    }
                }else
                    all.add(dat[i]);
        return all;
    }

    /**
     * Setzt den Pfad zum Bild
     */
    public void setBildp(String bildp){
        this.bildp = bildp;
        bild.setText(bildp);
        bild.postInvalidate();
    }
}
