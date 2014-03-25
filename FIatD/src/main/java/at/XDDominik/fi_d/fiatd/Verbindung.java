package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import Server.InvalidParameterException;
import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;

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

    /**
     * Ist für die Synchronisation der Daten über eine angegebene Verbindung, aus der angegebenen Datenbank auf dem Mobilgerät zuständig.
     * @param db Datenbank
     * @param a Hauptprogramm zur Kommnuikation mit Interface
     * @param v aktuelle Verbindung
     */
    public static void syncZiehungen(Database db,Activity a,Verbindung v){
        try {
            TCPConnection tcp = null;
            if(v != null)
                tcp = v.getTCP();
            if(tcp != null){
                LinkedList<Probenziehung> liste = new LinkedList<Probenziehung>();
                Cursor c = db.getFinishZ();
                while(c.moveToNext()){
                    Cursor temp = db.getprobeninZ(c);
                    LinkedList<ProbenDaten> pdl = new LinkedList<ProbenDaten>();
                    while(temp.moveToNext()){
                        int anz = temp.getInt(temp.getColumnIndex("Packungszahl"));
                        int cn = temp.getInt(temp.getColumnIndex("Chargennummer"));
                        int ln = temp.getInt(temp.getColumnIndex("LieferNr"));
                        int arn = temp.getInt(temp.getColumnIndex("ArtNr"));
                        String lieferant = temp.getString(temp.getColumnIndex("Lieferant"));
                        String besch = temp.getString(temp.getColumnIndex("Probenbeschreibung"));
                        String b2bnr = temp.getString(temp.getColumnIndex("B2BNr"));
                        String groesse = temp.getString(temp.getColumnIndex("Packungsgroesse"));
                        String bezeichnung = temp.getString(temp.getColumnIndex("Bezeichnung"));
                        String eancode = temp.getString(temp.getColumnIndex("EANCode"));
                        String bildp  = "";
                        String mhd = temp.getString(temp.getColumnIndex("MHD"));
                        boolean bio =false;
                        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
                        pdl.add(new ProbenDaten(anz,simp.parse(mhd),cn,ln,lieferant,besch,b2bnr,groesse,arn,bezeichnung,eancode,bio,bildp));
                    }

                    String pribenzieher = c.getString(c.getColumnIndex("Name"));
                    Kunde k = new Kunde(c.getInt(c.getColumnIndex("KNummer")),c.getString(c.getColumnIndex("KName")));
                    KundenVertreter kunde = new KundenVertreter(c.getString(c.getColumnIndex("KVName")),k);
                    SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d =  simp.parse(c.getString(c.getColumnIndex("Ziehungsdatum")) + " " + c.getString(c.getColumnIndex("Ziehungszeit")));
                    String ort = c.getString(c.getColumnIndex("Ziehungsort"));
                    double preis = c.getDouble(c.getColumnIndex("Preis"));
                    int status = c.getInt(c.getColumnIndex("Status"));
                    liste.add(new Probenziehung(pdl,pribenzieher,kunde,d,ort,preis,status));

                    String name = "LVA" + "_" +k.getKundenname() + "_"+ c.getString(c.getColumnIndex("Ziehungsdatum")) + ".jpg";
                    FileInputStream fis = a.openFileInput(name);
                    byte[] arr = new byte[fis.available()];
                    fis.read(arr);
                    tcp.sendMessage("image:" + name);
                    tcp.sendMessage(arr);
                    fis.close();

                    name = "Kunde" + "_" +k.getKundenname() + "_"+ c.getString(c.getColumnIndex("Ziehungsdatum")) + ".jpg";
                    fis = a.openFileInput(name);
                    arr = new byte[fis.available()];
                    fis.read(arr);
                    tcp.sendMessage("image:" + name);
                    tcp.sendMessage(arr);
                    fis.close();
                }


                tcp.sendMessage("save:");
                tcp.sendMessage(liste);
                //TODO Daten aus mobiler Datenbank löschen
                Toast.makeText(a.getApplicationContext(),"Syncronisieren Erfolgreich!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(a.getApplicationContext(),"Syncronisieren Fehlgeschlagen!", Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
