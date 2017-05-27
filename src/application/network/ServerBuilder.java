package application.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.
 */
public abstract class ServerBuilder
{
    protected ServerSocket serverSocket;
    protected int port;

    public ServerBuilder (int port)
    {
        this.port = port;
    }

    public void setConnection () throws Exception
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void println (String msg)
    {
        System.out.println("Server > " + msg);
    }

    public void send (String msg, Socket client) throws Exception
    {
        OutputStream     os  = client.getOutputStream();
        DataOutputStream out = new DataOutputStream(os);

        out.writeUTF(msg);
    }

    public String receive (Socket client) throws Exception
    {
        InputStream     is  = client.getInputStream();
        DataInputStream in  = new DataInputStream(is);
        String          str = in.readUTF();
        System.out.println("Received !");
        return str;
    }
}
