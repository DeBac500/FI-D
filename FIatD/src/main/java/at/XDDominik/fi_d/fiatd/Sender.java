package at.XDDominik.fi_d.fiatd;

import android.app.Activity;
import android.database.Cursor;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import Server.InvalidParameterException;
import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;

/**
 * Verwaltet den Sendevorgang aus der mobilen Datenbank zur Server Datenbank
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class Sender implements Runnable{
    private Database db;
    private Activity a;
    private Verbindung v;
    private TCPConnection tcp;
    private boolean next,interrupt=false;
    private ArrayList<String> befhel = new ArrayList<String>();
    private ArrayList<Object> tosend = new ArrayList<Object>();
    public Sender(Database db, Activity a,Verbindung v){
        this.db = db;
        this.a = a;
        this.v = v;
    }
    /**
     * Ist fuer die Synchronisation der Daten ueber eine angegebene Verbindung, aus der angegebenen Datenbank auf dem Mobilgeraet zustaendig.
     */
    public void syncZiehungen(){
        try {
            if(v != null)
                tcp = v.getTCP();
            else
                Toast.makeText(a.getApplicationContext(), "Keine Verbindung", Toast.LENGTH_SHORT).show();
            if(tcp != null){
                LinkedList<Probenziehung> liste = new LinkedList<Probenziehung>();
                Cursor c1 = db.getFinishZ();
                while(c1.moveToNext()){
                    Cursor c = db.getZiehungAll(c1);
                    c.moveToNext();
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
                    befhel.add("image:" + name);
                    tosend.add(arr);
                    fis.close();

                    name = "Kunde" + "_" +k.getKundenname() + "_"+ c.getString(c.getColumnIndex("Ziehungsdatum")) + ".jpg";
                    fis = a.openFileInput(name);
                    arr = new byte[fis.available()];
                    fis.read(arr);
                    befhel.add("image:" + name);
                    tosend.add(arr);
                    fis.close();

                }


                befhel.add("save:");
                tosend.add(liste);
                for( Probenziehung temp : liste){
                    befhel.add("generiereFormular:");
                    tosend.add(temp);
                }

                next = true;
                Thread t = new Thread(this);
                t.start();
                //TODO Daten aus mobiler Datenbank löschen


            }else{
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(a.getApplicationContext(),"Syncronisieren Fehlgeschlagen!", Toast.LENGTH_SHORT).show();
                    }
                });
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
    
    /**
     * 
     */
    public void setnext(){this.next = true;}
    
    /**
     * 
     */
    @Override
    public void run() {
        while(!interrupt){
            if(this.next){
                if(tcp != null){
                    tcp.sendMessage(befhel.get(0));
                    tcp.sendMessage(tosend.get(0));
                    befhel.remove(0);
                    tosend.remove(0);
                    this.next=false;
                }
            }
            if(befhel.size() <= 0)
                interrupt = true;
        }
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a.getApplicationContext(),"Syncronisieren Erfolgreich!", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println("Ich beende!!!");
        tcp.close();
    }
}
