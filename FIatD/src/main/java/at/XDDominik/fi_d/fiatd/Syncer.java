package at.XDDominik.fi_d.fiatd;

import android.app.Activity;

/**
 * Verwaltet die Snyhronisation zwischen Mobiler Datenbank und Server Datenbank
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class Syncer implements Reciever {
    private Database db;
    private Activity a;
    private Verbindung v;
    private Sender ss;
    private boolean first = true;
    
    /**
     * Erstellt eine Instanz dieser Klasse
     * @param db Datenbank
     * @param a Aktuelle Aktivit�t
     */
    public Syncer(Database db, Activity a){
        this.db = db;
        this.a = a;
    }
    
    /**
     * Setzt eine neue Verbindung
     * @param v	Verbindung die gesetzt wird
     */
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
            //System.out.println("next!!!");
            ss.setnext();
        }

    }
}
