package at.XDDominik.fi_d.fiatd;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by dominik on 19.02.14.
 */
public class task extends AsyncTask {
    private Reciever tab;
    private TCPConnection conn;
    public task(Reciever t){
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
}
