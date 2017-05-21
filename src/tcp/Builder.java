package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.
 */
public abstract class Builder
{
    public abstract void setConnection () throws Exception;

    public abstract void println (String msg);

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
