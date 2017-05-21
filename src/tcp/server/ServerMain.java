package tcp.server;

/**
 * Created by Robin on 21/05/2017.
 */
public class ServerMain
{
    public static void main (String[] args)
    {
        new Thread(new Server(8000)).start();
    }
}

