package at.XDDominik.fi_d.fiatd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Erstellt die Datenbank und verwaltet die Datenbankverbindung
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public class Database extends SQLiteOpenHelper {
    private Context con;
    private SQLiteDatabase db;
    //private MySQLLiteHelper mysql;

    /**
     * Konstruktor zur Erstellung einer Referenz zur Datenbank und dessen Verbindung
     * @param context das Programm
     */
    public Database(Context context) {
        super(context,
                context.getResources().getString(R.string.dbname),
                null,
                Integer.parseInt(context.getResources().getString(R.string.version)));
        con = context;
        //mysql = new MySQLLiteHelper(con);

    }
    
    /**
     * ÷ffnet eine Datenbankverbindung
     */
    public void open(){
        db = this.getWritableDatabase();
    }
    
    /**
     * Schlieﬂt eine Datenbankverbindung
     */
    public void close(){
        super.close();
    }

    /**
     * Erstellt die Datenbank mit Hilfe von SQL Queries
     * @param db die Datenbank
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");

        String[] aa = con.getResources().getStringArray(R.array.drop);
        for(int i = 0; i <aa.length;i++){
            db.execSQL(aa[i]);
        }

        String sql = "CREATE TABLE Artikel (";
        String[] dat = con.getResources().getStringArray(R.array.Artikel);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Kunde (";
        dat = con.getResources().getStringArray(R.array.Kunde);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Kundenvertreter (";
        dat = con.getResources().getStringArray(R.array.Kundenvertreter);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Probenzieher (";
        dat = con.getResources().getStringArray(R.array.Probenzieher);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Probenziehung (";
        dat = con.getResources().getStringArray(R.array.Probenziehung);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Probendaten (";
        dat = con.getResources().getStringArray(R.array.Probendaten);
        db.execSQL(create(sql,dat));

        sql = "CREATE TABLE Profil (";
        dat = con.getResources().getStringArray(R.array.Profil);
        db.execSQL(create(sql,dat));

        aa = con.getResources().getStringArray(R.array.insert);
        for(int i = 0; i <aa.length;i++){
            db.execSQL(aa[i]);
        }

    }
    
    /**
     * Erzeugt eine SQL Query mit mitgegebenen Daten
     * @param sql SQL Anweisung
     * @param dat mitgegebene Daten
     * @return SQL Anweisung
     */
    public String create(String sql , String[] dat){
        sql += " ";
        for(int i = 0; i < dat.length;i++){
            String name, att;
            //if(dat[i].split(",").length ==2){
            name = dat[i].split(",")[0];
            att = dat[i].split(",")[1];
            //}else{
            //  name = dat[i].split(",")[1];
            // att = dat[i].split(",")[2];
            //}
            if(i == dat.length-1)
                sql += name + " " + att;
            else
                sql += name + " " + att +",";
        }
        sql += ")";
        return sql;
    }

    /**
     * Upgraded die Version der Datenbank
     * @param db Datenbank
     * @param oldVersion Alte Version
     * @param newVersion Neue Version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for(String sql : con.getResources().getStringArray(R.array.drop))
            db.execSQL(sql);
        onCreate(db);
    }
    
    /**
     * F¸hrt eine Query in der Datenbank aus
     * @param sql SQL Anweisung
     */
    public void exeSQL(String sql){
        db.execSQL(sql);
    }
    
    /**
     * Gibt einen Cursor des Artikels mit Hilfe eines SQL SELECT zur¸ck
     * @return SQL Anweisung
     */
    public Cursor getArtikelCursor(){
        return db.rawQuery("SELECT *,ArtNr as _id FROM Artikel",null);
    }
    
    /**
     * Gibt einen Cursor der Ziehung mit Hilfe eines SQL SELECT zur¸ck
     * @return SQL Anweisung
     */
    public Cursor getZiehungCursor(){
        return db.rawQuery("SELECT *,KNummer as _id FROM Kunde NATURAL JOIN Probenziehung",null);
    }
    
    /**
     * Gibt alle Proben anhand eines bestimmten Cursors mit Hilfe eines SQL SELECT zur¸ck
     * @param c der Cursor
     * @return SQL Anweisung
     */
    public Cursor getprobeninZ(Cursor c){
        return db.rawQuery("SELECT ArtNr as _id,* FROM Artikel NATURAL JOIN Probendaten WHERE KVName = " +
                "\""+c.getString(c.getColumnIndex("KVName"))+"\" AND " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\" ;",null);
    }
    
    /**
     * Gibt einen Cursor des Profils mit Hilfe eines SQL SELECT zur¸ck
     * @return	SQL Anweisung
     */
    public Cursor getProfil(){
        return db.rawQuery("SELECT *,PName as _id FROM Profil",null);
    }
    
    /**
     * Gibt einen Cursor des Probenziehers mit Hilfe eines SQL SELECT zur¸ck
     * @return	SQL Anweisung
     */
    public Cursor getProbenZieher(){
        return db.rawQuery("SELECT *,Name as _id FROM Probenzieher",null);
    }
    
    /**
     * Gibt einen Cursor des Kunden mit Hilfe eines SQL SELECT zur¸ck
     * @return	SQL Anweisung
     */
    public Cursor getKunden(){
        return db.rawQuery("SELECT KName,KNummer,KNummer as _id FROM Kunde",null);
    }
    
    /**
     * Gibt einen Cursor des Kundenvertreters mit Hilfe eines SQL SELECT zur¸ck
     * @return	SQL Anweisung
     */
    public Cursor getKundenVertreter(){
        return db.rawQuery("SELECT KVName,KNummer,KNummer as _id FROM Kundenvertreter",null);
    }
    
    /**
     * F¸gt einen neuen Datensatz mit Daten zu einer Probe in die Datenbank ein
     * @param spinner 
     * @param von 
     */
    public void addPD(Cursor spinner,Cursor von){
        db.execSQL("INSERT INTO Probendaten (KVName, KNummer, Name, Ziehungsdatum, ArtNr, Ziehungszeit) VALUES " +
                "(\""+spinner.getString(spinner.getColumnIndex("KVName"))+"\", "+spinner.getInt(spinner.getColumnIndex("KNummer"))+"," +
                " \""+spinner.getString(spinner.getColumnIndex("Name"))+"\", \""+spinner.getString(spinner.getColumnIndex("Ziehungsdatum"))+"\"," +
                von.getInt(von.getColumnIndex("ArtNr"))+", \""+spinner.getString(spinner.getColumnIndex("Ziehungszeit"))+"\")");
    }
    
    /**
     * Lˆscht einen Datensatz mit Daten zu einer Probe aus der Datenbank
     * @param c der Cursor
     */
    public void removePD(Cursor c){
        db.execSQL("DELETE FROM Probendaten WHERE KVName = \""+c.getString(c.getColumnIndex("KVName"))+"\" AND " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\" AND " +
                "ArtNr = \""+c.getString(c.getColumnIndex("ArtNr"))+"\" ;");
    }
    
    /**
     * Gibt alle Artikel zur¸ck die nicht in den Probendaten vorhanden sind
     * @param c der Cursor
     * @return SQL Anweisung
     */
    public Cursor getArtikelex(Cursor c){
        return db.rawQuery("SELECT ArtNr as _id,* FROM Artikel WHERE " +
                "Artikel.ArtNr NOT IN ( SELECT ArtNr FROM Probendaten WHERE " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\");",null);
    }
    
    /**
     * Gibt alle Kundenvertreter mit zugehˆriger ID zur¸ck
     * @param c der Cursor
     * @return SQL Anweisung
     */
    public Cursor getKVinK(Cursor c){
        return db.rawQuery("SELECT KVName as _id,* FROM Kundenvertreter WHERE " +
                "KNummer == " + c.getInt(c.getColumnIndex("KNummer")),null);
    }
    
    /**
     * Gibt alle Probenziehungen mit zugehˆrigem Kundenvertreter und Kunde zur¸ck
     * @param kz der Cursor
     * @return SQL Anweisung
     */
    public Cursor getZiehungAll(Cursor kz){
        return db.rawQuery("SELECT * FROM Probenziehung NATURAL JOIN Kundenvertreter NATURAL JOIN Kunde Where KVName = \""+kz.getString(kz.getColumnIndex("KVName"))+"\" AND " +
                "KNummer = "+kz.getInt(kz.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+kz.getString(kz.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+kz.getString(kz.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+kz.getString(kz.getColumnIndex("Ziehungszeit"))+"\"",null);
    }
    
    /**
     * Gibt alle finalisierten Probenziehungen zur¸ck
     * @return SQL Anweisung
     */
    public Cursor getFinishZ(){
        return db.rawQuery("SELECT * FROM Probenziehung WHERE Status=1",null);
    }

    public Cursor queryexecute(String sql){
        return db.rawQuery(sql,null);
    }
    public int uptadeexecute(String sql){
        return db.rawQuery(sql,null).getCount();
    }
}
