package at.XDDominik.fi_d.fiatd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.ClientServerZiehung.ServerReciever;

/**
 * Verbindungsklasse welche alle Methoden enthält um eine TCP-Verbindung zu erstellen
 * @author Dominik Backhausen dominik.backhausen@gmail.com
 * @version 0.9
 */
public class TCPConnection implements Runnable {
    private int ID;
    private ObjectOutputStream streamOut = null;
    private ObjectInputStream streamIn = null;
    private Socket socket = null;
    private boolean run;
    private Reciever controller;
    private Thread t;

    /**
     * Konstrukor um eine Instanz dieser Klasse zu
     * @param sc Benachrichtigt das Programm wenn eine neue Nachricht empfangen wird
     * @param socket Socket der fuer die Verbindung benötigt wird
     */
    public TCPConnection(Reciever sc, Socket socket) {
        this.controller = sc;
        this.socket = socket;
        this.ID = socket.getPort();
        t = new Thread(this);

    }

    /**
     * Wartet auf eine neue Nachricht
     */
    @Override
    public void run() {
        System.out.println("Server Thread " + ID + " running.");
        while (run){
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {}
            try{
                Object o = streamIn.readObject();
                System.out.println("FFFFFFFF:   Empfangen!!!!");
                if(controller != null){
                    if(o instanceof LinkedList && this.controller instanceof ServerReciever){
                        System.out.println(o.toString());
                        LinkedList<Probenziehung> em = (LinkedList<Probenziehung>)o;
                        ServerReciever to = (ServerReciever)this.controller;
                        to.setLinkedList(em);
                    }else
                        controller.handleIn(o);
                }
            }catch(Exception e){
                e.printStackTrace();
                System.err.println(ID + " Conectin timed out!");
                this.close();
            }
        }
    }

    /**
     * Oeffnet die Verbindung zum Client
     * @throws java.io.IOException wird geworfen wenn der Stream nicht erstellt werden kann
     */
    public void open() throws IOException {
        this.streamOut = new ObjectOutputStream(socket.getOutputStream());
        this.streamIn = new ObjectInputStream(socket.getInputStream());
        this.run=true;
        t.start();
    }

    /**
     * Schließt die Verbindung und loggt den User aus
     */
    public void close() {
        try{
            this.run = false;
            if (socket != null)    socket.close();
            if (streamIn != null)  streamIn.close();
            if (streamOut != null) streamOut.close();
        }catch(IOException e){

        }
    }

    /**
     * Sendet eine Nachricht an den Client
     * @param msg Nachricht die gesendet wird
     */
    public void sendMessage(Object msg) {
        try{
            streamOut.writeObject(msg);
            streamOut.flush();
            System.out.println("Senden erfolgreich!");
        }catch(Exception e){
            System.err.println("Senden der Nachricht Fehlgeschlagen!");
        }
    }
}