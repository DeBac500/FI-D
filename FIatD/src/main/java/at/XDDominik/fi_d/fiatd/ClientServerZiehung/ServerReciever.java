package at.XDDominik.fi_d.fiatd.ClientServerZiehung;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.LinkedList;

import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.R;
import at.XDDominik.fi_d.fiatd.Reciever;
import at.XDDominik.fi_d.fiatd.TCPConnection;
import at.XDDominik.fi_d.fiatd.Verbindung;

/**
 * Empfängt Nachrichten vom Server
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class ServerReciever implements Reciever{
    private MainClientServerZiehung main;
    private Verbindung t;
    private ArrayAdapter<Probenziehung> a2;
    private boolean does= true;
    private ListView lv;
    public ServerReciever(MainClientServerZiehung main){
        this.main = main;
        t = new Verbindung(main,this);
        t.execute();
    }

    /**
     * Bereitet den Server Recever vor
     */
    public void setUp(){

        lv= (ListView)main.findViewById(R.id.list_server);

        LinkedList<Probenziehung> temp =main.getLinkedList();
        //try{
        //    Kunde k = new Kunde(1,"Sparkunde1");
        //    KundenVertreter kv = new KundenVertreter("Spar kv",k);
        //    Date d = new Date();
//
 //           Probenziehung p = new Probenziehung(null,"TestPZ",kv,d,"Wien",100,100);
  //          temp.add(p);
   //         temp.add(p);
    //    }catch(InvalidParameterException e){}


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
        a2 = new ServerAdapter(main,R.layout.list_view,temp,lv);
        lv.setAdapter(a2);
        a2.clear();
        View emptyText = (View)main.findViewById(R.layout.empty);
        lv.setEmptyView(emptyText);
    }

    /**
     * Setzt die Liste mit den Probenziehungen
     */
    public void setLinkedList(LinkedList<Probenziehung> temp){
        System.out.println(temp.toString());
        main.pzz = temp;
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                a2.clear();
                a2.addAll(main.pzz);
                //System.out.println("sakjhdjashjlkdhsajkdhaskljdhkljsadhkljashda");
                a2.notifyDataSetChanged();
            }
        });
        does = false;

    }

    /**
     * Sendet Daten
     */
    public void sendUP(LinkedList<Probenziehung> list){
        for(Probenziehung z : list){
            z.setStatus(2);
        }
        TCPConnection tcp =t.getTCP();
        tcp.sendMessage("save:");
        tcp.sendMessage(list);
    }

    @Override
    public void handleIn(Object o) {
        if(o instanceof String){
            if(does){
                String s = (String)o;
                if(s.equalsIgnoreCase("Ready")){
                    TCPConnection tcp = null;
                    if(t != null)
                        tcp = t.getTCP();
                    if(tcp != null){
                        tcp.sendMessage("zero:");
                    }
                }
            }
        }
        if(o instanceof LinkedList){
            LinkedList<Probenziehung> t = (LinkedList<Probenziehung>)o;

            this.setLinkedList(t);
        }
    }

}
