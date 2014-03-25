package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

/**
 * Erstellt eine Verbindungsklasse beinhaltet Methoden zur Synchronistion und verwaltet die Kommunikation
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class Verbindung extends AsyncTask {
    private Reciever tab;
    private Activity a;
    private TCPConnection conn;

    /**
     * Erzeugt ein Objekt um mit den Methoden der Verbindung zu arbeiten
     * @param a Hauptprogramm zur Kommnuikation mit Interface
     * @param t Empfaenger fuer die vom Server gesendeten Daten
     */
    public Verbindung(Activity a,Reciever t){
        tab = t;
        this.a = a;
    }

    /**
     * Erzeugt die Verbindung im Hintergrund
     * @param params mögliche Definitionsparameter
     * @return  mögliches Ergebnis
     */
    @Override
    protected Object doInBackground(Object[] params) {
        try{
            Properties pro = new Properties();
            FileInputStream fis = a.openFileInput(MainActivity.config);
            pro.load(fis);
            String ip = pro.getProperty(MainActivity.ip);
            String port = pro.getProperty(MainActivity.port);
            fis.close();
            conn = new TCPConnection(tab,new Socket(ip,Integer.parseInt(port)));
            conn.open();
        }catch(IOException e){e.printStackTrace();}
        catch (NumberFormatException e){Toast.makeText(a,"Port Ist keine Zahl",Toast.LENGTH_SHORT).show();};
        return null;
    }

    /**
     * @return die aktuelle TCPVerbindung
     */
    public TCPConnection getTCP(){return conn;}


}
