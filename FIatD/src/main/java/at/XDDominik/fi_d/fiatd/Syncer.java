package at.XDDominik.fi_d.fiatd;

import android.app.Activity;

/**
 * Created by dominik on 25.03.14.
 */
public class Syncer implements Reciever {
    private Database db;
    private Activity a;
    private Verbindung v;
    private Sender ss;
    private boolean first = true;
    public Syncer(Database db, Activity a){
        this.db = db;
        this.a = a;
    }
    public void setVerbindung(Verbindung v){
        this.v = v;
        ss = new Sender(db,a,this.v);
    }
    @Override
    public void handleIn(Object o) {
        //System.out.println("Empfangen!!");
        if(first){
            if(o instanceof String)
                if(((String) o).equalsIgnoreCase("ready")){
                    ss.syncZiehungen();
                    first = false;
                }
        }else{
            ss.setnext();
        }

    }
}
