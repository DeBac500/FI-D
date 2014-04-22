package at.XDDominik.fi_d.fiatd;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;

/**
 * Speichert Daten in der Datenbank
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class Saver {
    private Database dbconn;

    /**
     * Erstellt ein Objekt zum Speichern von Daten in der Datenbank
     */
    public Saver(Database db){
        dbconn = db;
    }

    /**
     * Speichert alles aus der übergebenen Liste
     */
    public void saveEveryThing(LinkedList<Probenziehung> ll) {
        Kunde k;
        KundenVertreter kv;
        LinkedList<ProbenDaten> pdl;

        for(Probenziehung pz : ll){
            kv=pz.getKunde();
            k=kv.getKunde();
            pdl = pz.getProbenDaten();
            System.out.println(pdl);
            try {
                //Kummulatives Update aller andren Daten
                if(!(dbconn.queryexecute("SELECT * FROM Kunde WHERE KNummer = "+k.getKundennummer()).getColumnCount()>0)){
                    dbconn.exeSQL("INSERT INTO Kunde (KNummer, KName) VALUES (" + k.getKundennummer() + ",\"" + k.getKundenname() + "\");");
                }
                System.out.println();
                if(!(dbconn.queryexecute("SELECT * FROM Kundenvertreter WHERE KVName = \""+kv.getKvname()+"\"").getColumnCount()>0)){
                    dbconn.exeSQL("INSERT INTO Kundenvertreter (KVName, KNummer) VALUES  (\"" + kv.getKvname() + "\", " + kv.getKunde().getKundennummer() + ");");
                }

                if(!(dbconn.queryexecute("SELECT * FROM Probenzieher WHERE Name = \""+pz.getProbenZieher()+"\"").getColumnCount()>0)){
                    dbconn.exeSQL("INSERT INTO Probenzieher VALUES (\"" + pz.getProbenZieher() + "\");");
                }

                SimpleDateFormat dated = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat datet = new SimpleDateFormat("HH:mm:ss");

                //DELETE alle Proben in den Ziehungen
                dbconn.exeSQL("DELETE FROM Probendaten WHERE KNummer = " + k.getKundennummer() + " AND KVName = \"" + kv.getKvname() + "\" AND  Name = \"" + pz.getProbenZieher() + "\" AND Ziehungsdatum = \"" + dated.format(pz.getDatum()) + "\" AND Ziehungszeit = \"" + datet.format(pz.getDatum()) + "\"");


                //DELETE die Ziehungen
                dbconn.exeSQL("DELETE FROM Probenziehung WHERE KNummer = " + k.getKundennummer() + " AND KVName = \"" + kv.getKvname() + "\" AND Name = \"" + pz.getProbenZieher() + "\"AND Ziehungsdatum = \"" + dated.format(pz.getDatum()) + "\" AND Ziehungszeit = \"" + datet.format(pz.getDatum()) + "\"");

                //INSERT die ZIehungen
                try{
                    dbconn.exeSQL("INSERT INTO Probenziehung (KVName, KNummer, Name, Ziehungsdatum, Ziehungszeit, Ziehungsort, Preis, Status) VALUES (\"" + kv.getKvname() + "\", " + kv.getKunde().getKundennummer() + ", \"" + pz.getProbenZieher() + "\", \"" + dated.format(pz.getDatum()) + "\", \"" + datet.format(pz.getDatum()) + "\", \"" + pz.getOrt() + "\", " + pz.getPreis() + ", " + pz.getStatus() + ");");
                }catch(Exception mySQLicve){
                    System.out.println("Double entry??? (Should not be reacheable)");
                    //FIXME yeah...
                }




                //INSERT Artikel und Probendaten
                for(ProbenDaten pd : pdl){
                    if(!(dbconn.queryexecute("SELECT * FROM Artikel WHERE ArtNr = "+pd.getArtikelnummer()).getCount()>=1)){
                        dbconn.exeSQL("INSERT INTO Artikel (ArtNr, Bezeichnung, EANCode, Bio) VALUES (" + pd.getArtikelnummer() + ", \"" + pd.getBezeichnung() + "\", " + pd.getEanCode() + ", " + (pd.isBio() ? 1 : 0) + ")");
                        System.out.println("JA1");
                    }


                    if(!(dbconn.queryexecute("SELECT * FROM Probendaten WHERE KNummer = "+k.getKundennummer()+" AND KVName = \""+kv.getKvname()+"\" AND  Name = \""+pz.getProbenZieher()+"\" AND ArtNr = "+pd.getArtikelnummer()+" AND Ziehungsdatum = \""+dated.format(pz.getDatum())+"\" AND Ziehungszeit = \""+datet.format(pz.getDatum())+"\"").getCount()>=1)){
                        dbconn.exeSQL("INSERT INTO Probendaten (KVName, KNummer, Name, Ziehungsdatum, ArtNr, Ziehungszeit, Packungszahl, MHD, Chargennummer, LieferNr, Lieferant, Probenbeschreibung, B2BNr, Packungsgroesse) VALUES (\"" + kv.getKvname() + "\", " + k.getKundennummer() + ", \"" + pz.getProbenZieher() + "\", \"" + dated.format(pz.getDatum()) + "\", " + pd.getArtikelnummer() + ", \"" + datet.format(pz.getDatum()) + "\", " + pd.getPackungszahl() + ", \"" + dated.format(pd.getmHD()) + "\", " + pd.getChargennummer() + ", " + pd.getLiefernummer() + ", \"" + pd.getLieferant() + "\", \"" + pd.getProbenbesch() + "\", " + pd.getB2BNr() + ", \"" + pd.getPackungsgroesse() + "\")");
                        System.out.println("JA1");
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // TODO Save everything
    }

}
