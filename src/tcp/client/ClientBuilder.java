package tcp.client;

import tcp.Builder;

import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.
 */
public abstract class ClientBuilder extends Builder
{
    protected int port;
    protected String IP;
    protected Socket socketClient;

    public ClientBuilder (int port, String IP)
    {
        this.port = port;
        this.IP = IP;
    }

    @Override
    public void setConnection () throws Exception
    {
        socketClient = new Socket(IP, port);
    }

    @Override
    public void println (String msg)
    {
        System.out.println("Client > " + msg);
    }
}
