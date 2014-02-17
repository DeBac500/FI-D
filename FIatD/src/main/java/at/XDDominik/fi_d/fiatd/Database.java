package at.XDDominik.fi_d.fiatd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by dominik on 02.12.13.
 */
public class Database extends SQLiteOpenHelper {
    private Context con;
    private SQLiteDatabase db;
    //private MySQLLiteHelper mysql;

    public Database(Context context) {
        super(context,
                context.getResources().getString(R.string.dbname),
                null,
                Integer.parseInt(context.getResources().getString(R.string.version)));
        con = context;
        //mysql = new MySQLLiteHelper(con);

    }
    public void open(){
        db = this.getWritableDatabase();
    }
    public void close(){
        super.close();
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        for(String sql : con.getResources().getStringArray(R.array.drop))
            db.execSQL(sql);
        onCreate(db);
    }
    public void exeSQL(String sql){
        db.execSQL(sql);
    }
    public Cursor getArtikelCursor(){
        return db.rawQuery("SELECT *,ArtNr as _id FROM Artikel",null);
    }
    public Cursor getZiehungCursor(){
        return db.rawQuery("SELECT *,KNummer as _id FROM Kunde NATURAL JOIN Probenziehung",null);
    }
    public Cursor getprobeninZ(Cursor c){
        return db.rawQuery("SELECT ArtNr as _id,* FROM Artikel NATURAL JOIN Probendaten WHERE KVName = " +
                "\""+c.getString(c.getColumnIndex("KVName"))+"\" AND " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\" ;",null);
    }
    public Cursor getProfil(){
        return db.rawQuery("SELECT *,PName as _id FROM Profil",null);
    }
    public Cursor getProbenZieher(){
        return db.rawQuery("SELECT *,Name as _id FROM Probenzieher",null);
    }
    public Cursor getKunden(){
        return db.rawQuery("SELECT KName,KNummer,KNummer as _id FROM Kunde",null);
    }
    public Cursor getKundenVertreter(){
        return db.rawQuery("SELECT KVName,KNummer,KNummer as _id FROM Kundenvertreter",null);
    }
    public void addPD(Cursor spinner,Cursor von){
        db.execSQL("INSERT INTO Probendaten (KVName, KNummer, Name, Ziehungsdatum, ArtNr, Ziehungszeit) VALUES " +
                "(\""+spinner.getString(spinner.getColumnIndex("KVName"))+"\", "+spinner.getInt(spinner.getColumnIndex("KNummer"))+"," +
                " \""+spinner.getString(spinner.getColumnIndex("Name"))+"\", \""+spinner.getString(spinner.getColumnIndex("Ziehungsdatum"))+"\"," +
                von.getInt(von.getColumnIndex("ArtNr"))+", \""+spinner.getString(spinner.getColumnIndex("Ziehungszeit"))+"\")");
    }
    public void removePD(Cursor c){
        db.execSQL("DELETE FROM Probendaten WHERE KVName = \""+c.getString(c.getColumnIndex("KVName"))+"\" AND " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\" AND " +
                "ArtNr = \""+c.getString(c.getColumnIndex("ArtNr"))+"\" ;");
    }
    public Cursor getArtikelex(Cursor c){
        return db.rawQuery("SELECT ArtNr as _id,* FROM Artikel WHERE " +
                "Artikel.ArtNr NOT IN ( SELECT ArtNr FROM Probendaten WHERE " +
                "KNummer = "+c.getInt(c.getColumnIndex("KNummer"))+" AND " +
                "Name = \""+c.getString(c.getColumnIndex("Name"))+"\" AND " +
                "Ziehungsdatum = \""+c.getString(c.getColumnIndex("Ziehungsdatum"))+"\" AND " +
                "Ziehungszeit = \""+c.getString(c.getColumnIndex("Ziehungszeit"))+"\");",null);
    }
}
