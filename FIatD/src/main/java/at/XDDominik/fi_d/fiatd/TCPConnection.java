package at.XDDominik.fi_d.fiatd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import Server.Probenziehung;
import at.XDDominik.fi_d.fiatd.ClientServerZiehung.ServerReciever;

/**
 *  Connection Class to a single Client
 *  @author Dominik Backhausen
 *
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
     *  constructor to create a instance of this Class
     *  @param sc The Server Controller wich is needed to notify the programm when a new Message was receafed!
     *  @param socket The Soket wich is the Conection to the Client
     *
     */
    public TCPConnection(Reciever sc, Socket socket) {
        this.controller = sc;
        this.socket = socket;
        this.ID = socket.getPort();
        t = new Thread(this);

    }

    /**
     *  The Run-Method of a Thread wich is waitang for a new Message
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
     *  This Opens the Conection to the CLient
     *  @throws java.io.IOException Is throwen when the Streams could not be created
     */
    public void open() throws IOException {
        this.streamOut = new ObjectOutputStream(socket.getOutputStream());
        this.streamIn = new ObjectInputStream(socket.getInputStream());
        this.run=true;
        t.start();
    }

    /**
     *  This Methode close the Conection to the CLient and Logout the User form the Database!
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
     *  This Methode sends a New Object to the Client
     *  @param msg the Object which should be send
     */
    public void sendMessage(Object msg) {
        try{
            streamOut.writeObject(msg);
            streamOut.flush();
            System.out.println("Senden erfolgreich!!!");
        }catch(Exception e){
            System.err.println("Senden der Nachricht Fehlgeschlagen!");
        }
    }
}