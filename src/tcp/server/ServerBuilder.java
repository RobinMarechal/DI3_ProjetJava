package tcp.server;

import tcp.Builder;

import java.net.ServerSocket;

/**
 * Created by Robin on 21/05/2017.
 */
public abstract class ServerBuilder extends Builder
{
    protected ServerSocket serverSocket;
    protected int port;

    public ServerBuilder (int port)
    {
        this.port = port;
    }

    @Override
    public void setConnection () throws Exception
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    @Override
    public void println (String msg)
    {
        System.out.println("Server > " + msg);
    }
}
