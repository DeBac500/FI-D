package at.XDDominik.fi_d.fiatd;

import java.util.LinkedList;

import Server.Kunde;
import Server.KundenVertreter;
import Server.ProbenDaten;
import Server.Probenziehung;

/**
 * Created by dominik on 31.03.14.
 */
public class Saver {
    private Database dbconn;

    public Saver(Database db){
        dbconn = db;
    }

    public void saveEveryThing(LinkedList<Probenziehung> ll) {
        Kunde k;
        KundenVertreter kv;
        LinkedList<ProbenDaten> pdl;
        for(Probenziehung pz : ll){
            kv=pz.getKunde();
            k=kv.getKunde();
            pdl = pz.getProbenDaten();
            try {
                //Kummulatives Update aller andren Daten
                if(!(dbconn.queryexecute("SELECT * FROM Kunde WHERE KNummer = "+k.getKundennummer()).getColumnCount()>0)){
                    dbconn.uptadeexecute("INSERT INTO Kunde (KNummer, KName) VALUES ("+k.getKundennummer()+",\""+k.getKundenname()+"\");");
                }
                System.out.println();
                if(!(dbconn.queryexecute("SELECT * FROM Kundenvertreter WHERE KVName = \""+kv.getKvname()+"\"").getColumnCount()>0)){
                    dbconn.uptadeexecute("INSERT INTO Kundenvertreter (KVName, KNummer) VALUES  (\""+kv.getKvname()+"\", "+kv.getKunde().getKundennummer()+");");
                }

                if(!(dbconn.queryexecute("SELECT * FROM Probenzieher WHERE Name = \""+pz.getProbenZieher()+"\"").getColumnCount()>0)){
                    dbconn.uptadeexecute("INSERT INTO Probenzieher VALUES (\""+pz.getProbenZieher()+"\");");
                }



                //DELETE alle Proben in den Ziehungen
                dbconn.uptadeexecute("DELETE FROM Probendaten WHERE KNummer = "+k.getKundennummer()+" AND KVName = \""+kv.getKvname()+"\" AND  Name = \""+pz.getProbenZieher()+"\" AND Ziehungsdatum = \""+pz.getDatum().getYear()+"-"+pz.getDatum().getMonth()+"-"+pz.getDatum().getDay()+"\" AND Ziehungszeit = \""+pz.getDatum().getHours()+":"+pz.getDatum().getMinutes()+"-"+pz.getDatum().getSeconds()+"\"");


                //DELETE die Ziehungen
                dbconn.uptadeexecute("DELETE FROM Probenziehung WHERE KNummer = "+k.getKundennummer()+" AND KVName = \""+kv.getKvname()+"\" AND Name = \""+pz.getProbenZieher()+"\"AND Ziehungsdatum = \""+pz.getDatum().getYear()+"-"+pz.getDatum().getMonth()+"-"+pz.getDatum().getDay()+"\" AND Ziehungszeit = \""+pz.getDatum().getHours()+":"+pz.getDatum().getMinutes()+"-"+pz.getDatum().getSeconds()+"\"");

                //INSERT die ZIehungen
                try{
                    dbconn.uptadeexecute("INSERT INTO Probenziehung (KVName, KNummer, Name, Ziehungsdatum, Ziehungszeit, Ziehungsort, Preis, Status) VALUES (\""+kv.getKvname()+"\", "+kv.getKunde().getKundennummer()+", \""+pz.getProbenZieher()+"\", \""+pz.getDatum().getYear()+"-"+pz.getDatum().getMonth()+"-"+pz.getDatum().getDay()+"\", \""+pz.getDatum().getHours()+":"+pz.getDatum().getMinutes()+":"+pz.getDatum().getSeconds()+"\", \""+pz.getOrt()+"\", "+pz.getPreis()+", "+pz.getStatus()+");");
                }catch(Exception mySQLicve){
                    System.out.println("Double entry??? (Should not be reacheable)");
                    //FIXME yeah...
                }




                //INSERT Artikel und Probendaten
                for(ProbenDaten pd : pdl){
                    if(!(dbconn.queryexecute("SELECT * FROM Artikel WHERE ArtNr = "+pd.getArtikelnummer()).getColumnCount()>1)){
                        dbconn.uptadeexecute("INSERT INTO Artikel (ArtNr, Bezeichnung, EANCode, Bio) VALUES ("+pd.getArtikelnummer()+", \""+pd.getBezeichnung()+"\", "+pd.getEanCode()+", "+(pd.isBio()?1:0)+");");
                    }


                    if(!(dbconn.queryexecute("SELECT * FROM Probendaten WHERE KNummer = "+k.getKundennummer()+" AND KVName = \""+kv.getKvname()+"\" AND  Name = \""+pz.getProbenZieher()+"\" AND ArtNr = "+pd.getArtikelnummer()+" AND Ziehungsdatum = \""+pz.getDatum().getYear()+"-"+pz.getDatum().getMonth()+"-"+pz.getDatum().getDay()+"\" AND Ziehungszeit = \""+pz.getDatum().getHours()+":"+pz.getDatum().getMinutes()+"-"+pz.getDatum().getSeconds()+"\"").getColumnCount()>1)){
                        dbconn.uptadeexecute("INSERT INTO Probendaten (KVName, KNummer, Name, Ziehungsdatum, ArtNr, Ziehungszeit, Packungszahl, MHD, Chargennummer, LieferNr, Lieferant, Probenbeschreibung, B2BNr, Packungsgroesse) VALUES (\""+kv.getKvname()+"\", "+k.getKundennummer()+", \""+pz.getProbenZieher()+"\", \""+pz.getDatum().getYear()+"-"+pz.getDatum().getMonth()+"-"+pz.getDatum().getDay()+"\", "+pd.getArtikelnummer()+", \""+pz.getDatum().getHours()+":"+pz.getDatum().getMinutes()+"-"+pz.getDatum().getSeconds()+"\", "+pd.getPackungszahl()+", \""+pd.getmHD().getYear()+"-"+pd.getmHD().getMonth()+"-"+pd.getmHD().getDay()+"\", "+pd.getChargennummer()+", "+pd.getLiefernummer()+", \""+pd.getLieferant()+"\", \""+pd.getProbenbesch()+"\", "+pd.getB2BNr()+", \""+pd.getPackungsgroesse()+"\");");
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
