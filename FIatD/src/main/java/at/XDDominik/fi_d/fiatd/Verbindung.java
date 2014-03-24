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

import Server.InvalidParameterException;
import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;

/**
 * Created by dominik on 19.02.14.
 */
public class Verbindung extends AsyncTask {
    private Reciever tab;
    private TCPConnection conn;
    public Verbindung(Reciever t){
        tab = t;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try{
            conn = new TCPConnection(tab,new Socket("10.0.105.203",12345));
            conn.open();
        }catch(IOException e){e.printStackTrace();}
        return null;
    }
    public TCPConnection getTCP(){return conn;}

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