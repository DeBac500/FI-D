package at.XDDominik.fi_d.fiatd;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by dominik on 19.02.14.
 */
public class task2 extends AsyncTask {
    private Reciever tab;
    private TCPConnection conn;
    public task2(Reciever t){
        tab = t;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try{
            conn = new TCPConnection(tab,new Socket("10.0.104.144",12345));
            conn.open();
            Thread.sleep(1000);
            conn.sendMessage("all:");
        }catch(IOException e){e.printStackTrace();} catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public TCPConnection getTCP(){return conn;}
}
