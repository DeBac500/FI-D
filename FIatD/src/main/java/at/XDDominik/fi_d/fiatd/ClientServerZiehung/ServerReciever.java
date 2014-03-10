package at.XDDominik.fi_d.fiatd.ClientServerZiehung;

import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.Date;
import java.util.LinkedList;

import Server.InvalidParameterException;
import Server.Kunde;
import Server.KundenVertreter;
import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Reciever;
import at.XDDominik.fi_d.fiatd.TCPConnection;
import at.XDDominik.fi_d.fiatd.task;

/**
 * Created by dominik on 05.03.14.
 */
public class ServerReciever implements Reciever{
    private MainClientServerZiehung main;
    private task t;
    private ArrayAdapter<Probenziehung> a2;

    public ServerReciever(MainClientServerZiehung main){
        this.main = main;
        t = new task(this);
        t.execute();
    }
    public void setUp(){

        ListView lv= (ListView)main.findViewById(R.id.list_server);

        LinkedList<Probenziehung> temp =main.getLinkedList();
        try{
            Kunde k = new Kunde(1,"Sparkunde1");
            KundenVertreter kv = new KundenVertreter("Spar kv",k);
            Date d = new Date();

            Probenziehung p = new Probenziehung(null,"TestPZ",kv,d,"Wien",100,100);
            temp.add(p);
            temp.add(p);
        }catch(InvalidParameterException e){}
        a2 = new ServerAdapter(main,R.layout.list_view,temp,lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = (CheckBox)view.findViewById(R.id.check);
                Probenziehung c = (Probenziehung)adapterView.getItemAtPosition(i);
                if(!cb.isChecked()){
                    main.fserver.add(c);
                    cb.setChecked(!cb.isChecked());
                }else{
                    main.fserver.remove(c);
                    cb.setChecked(!cb.isChecked());
                }
            }
        });
        lv.setAdapter(a2);
    }

    public void setLinkedList(LinkedList<Probenziehung> temp){
        System.out.println(temp.toString());
        main.pzz = temp;
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                a2.clear();
                a2.addAll(main.pzz);
                System.out.println("sakjhdjashjlkdhsajkdhaskljdhkljsadhkljashda");
                a2.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void handleIn(Object o) {
        if(o instanceof String){
            String s = (String)o;
            if(s.equalsIgnoreCase("Ready")){
                TCPConnection tcp = null;
                if(t != null)
                    tcp = t.getTCP();
                if(tcp != null){
                    tcp.sendMessage("all:");
                }
            }
        }
    }

}
