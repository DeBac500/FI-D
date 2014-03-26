package at.XDDominik.fi_d.fiatd;

/**
 * Ist fuer das Empfangen der Daten vom Server und die Snchronisation zustaendig
 * @author Dominik Backhausen dbackhausen@gmail.com
 * @version 0.9
 */
public interface Reciever {
    public void handleIn(Object o);
}
