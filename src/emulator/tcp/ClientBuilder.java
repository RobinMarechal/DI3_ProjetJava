package emulator.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Robin on 26/05/2017.
 */
public class ClientBuilder
{
    protected int port;
    protected String IP;
    protected Socket socketClient;

    public ClientBuilder (String IP, int port)
    {
        this.port = port;
        this.IP = IP;
    }

    public void setConnection () throws Exception
    {
        socketClient = new Socket(IP, port);
    }

    public void println (String msg)
    {
        System.out.println("Client > " + msg);
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
