package at.XDDominik.fi_d.fiatd.Profil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import at.XDDominik.fi_d.fiatd.Ziehung.EditeProbe;

/**
 * Created by dominik on 04.12.13.
 */
public class EditeProfil extends ScrollView implements Serializable {
    private ArrayList<CheckBox> put;
    private MainProfil context;
    private EditText name;
    private String oldname;
    private Button deleate;
    private boolean neu;
    public EditeProfil(MainProfil context) {
        super(context);
        this.context = context;
        neu = true;
        setFocusable(true);
        TableRow rr = new TableRow(context);
        TableRow rr2 = new TableRow(context);
        put = new ArrayList<CheckBox>();

        ArrayList<String> all =EditeProbe.generateList(context, null);

        int mid = (all.size())/2;

        TableLayout tl = new TableLayout(context);
        TableLayout tl2 = new TableLayout(context);

        for(int i = 0; i < all.size(); i++){
            if(i <= mid)
                tl.addView(addRow(i,all.get(i).split(",")[0]));
            else
                tl2.addView(addRow(i,all.get(i).split(",")[0]));
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
        comit.setTextSize(30);
        cancle =  new Button(context);
        cancle.setText("Abbrechen");
        cancle.setTextSize(30);
        deleate = new Button(context);
        deleate.setText("Löschen");
        deleate.setTextSize(30);
        TableRow tr = new TableRow(context);
        tr.addView(deleate);
        TableRow.LayoutParams delp = (TableRow.LayoutParams)deleate.getLayoutParams();
        delp.span=2;
        tr.setLayoutParams(delp);
        rr2.addView(cancle);
        rr2.addView(comit);
        TableRow rrr = new TableRow(context);
        TextView tv = new TextView(context);
        tv.setText("Name: ");
        tv.setTextSize(30);
        rrr.addView(tv);
        name = new EditText(context);
        name.setTextSize(30);
        name.setSingleLine(true);
        rrr.addView(name);
        TableLayout tlll = new TableLayout(context);
        tlll.addView(rrr);
        tlll.addView(rr);
        tlll.addView(rr2);
        tlll.addView(tr);
        tlll.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        addView(tlll);
        LayoutParams pa = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pa.gravity=Gravity.CENTER_HORIZONTAL;
        tlll.setLayoutParams(pa);
        deleate.setEnabled(false);
        deleate.setVisibility(INVISIBLE);
        deleate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditeProfil p1 = EditeProfil.this;
                EditeProfil.this.context.getDB().exeSQL("DELETE FROM Profil WHERE PNAME=\""+p1.getOldname()+"\"");
                Intent intent = new Intent(EditeProfil.this.context, MainProfil.class);
                EditeProfil.this.context.startActivity(intent);
            }
        });
        cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditeProfil.this.context, MainProfil.class);
                EditeProfil.this.context.startActivity(intent);
            }
        });
        comit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                EditeProfil p = EditeProfil.this;
                if(p.getNeu()){
                    String sql = "INSERT INTO Profil";
                    String tabn =" (";
                    String val = " VALUES(";
                    for(int i = 0; i<p.getCheck().size();i++){
                        tabn += p.getCheck().get(i).getText() + ",";
                        if(p.getCheck().get(i).isChecked())
                            val += "1, ";
                        else
                            val += "0, ";
                    }
                    tabn += "PName)";
                    val += "\""+p.getName().getText()+"\")";
                    sql += tabn + val;
                    EditeProfil.this.context.getDB().exeSQL(sql);
                }else{
                    String sql = "UPDATE Profil SET ";
                    for(int i = 0; i < p.getCheck().size();i++){
                        sql += p.getCheck().get(i).getText() + "=";
                        if(p.getCheck().get(i).isChecked())
                            sql += "1, ";
                        else
                            sql += "0, ";
                    }
                    sql+= "PName=\""+p.getName().getText()+"\" ";
                    sql+=" WHERE PName=\"" + p.getOldname()+"\"";
                    EditeProfil.this.context.getDB().exeSQL(sql);
                }
                Intent intent = new Intent(EditeProfil.this.context, MainProfil.class);
                EditeProfil.this.context.startActivity(intent);
                }catch(SQLiteConstraintException e){
                    Toast.makeText(EditeProfil.this.context,"Schon Vorhanden",Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.setHorizontalScrollBarEnabled(true);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    public TableRow addRow(int i , String text){
        TableRow tr = new TableRow(context);
        put.add(new CheckBox(context.getApplicationContext()));
        put.get(i).setText(text);
        put.get(i).setTextColor(Color.BLACK);
        put.get(i).setTextSize(25);
        put.get(i).setChecked(true);
        put.get(i).setTextSize(30);
        tr.addView(put.get(i));
        return tr;
    }
    public void setStand(Cursor c, long id){
        //c.move((int)i);
        neu = false;
        deleate.setVisibility(VISIBLE);
        deleate.setEnabled(true);
        name.setText(c.getString(c.getColumnIndex("PName")));
        oldname = c.getString(c.getColumnIndex("PName"));
        for(int i = 0; i < put.size();i++){
            String t = (String)put.get(i).getText();
            int bb = c.getInt(c.getColumnIndex(t));
            //System.out.println("HHHHHHHH: " + bb);
            if(bb == 0)
                put.get(i).setChecked(false);
            else
                put.get(i).setChecked(true);
        }
        this.invalidate();
    }
    public boolean getNeu(){return neu;}
    public ArrayList<CheckBox> getCheck(){return put;}
    public EditText getName(){return name;}
    public String getOldname(){return oldname;}
}
