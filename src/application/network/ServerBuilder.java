package application.network;

import org.json.simple.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.<br/>
 * This class builds a server socket and prepare the configuration
 */
public abstract class ServerBuilder
{
    /** The string representing that the client want to synchronize the list of employees */
    protected String syncRequest = "SYNC";
    /** The format used to send and employee to the emulator */
    protected String employeeDataFormat = "{id};{name}";
    /** The format that the emulator uses to send a simulated check */
    protected String checkDataFormat = "{datetime};{id}";
    /** The Datetime format */
    protected String dateTimeFormat = "yyyy-MM-dd HH:mm";
    /** The server socket timeout time (0 = infinity) */
    protected int timeout = 0;
    /** The server port */
    protected int port = 8888;
    /** The sleep duration between each while loop iteration */
    protected int sleepDuration = 0;
    /** The server socket */
    protected ServerSocket serverSocket;

    /**
     * Config parameter constructor
     * @param config a JSON object containing the server config
     */
    public ServerBuilder (JSONObject config)
    {
        try
        {
            JSONObject network = (JSONObject) config.get("network");

            this.port = Integer.parseInt(network.get("port").toString());
            this.syncRequest = (String) network.get("sync_request");
            this.employeeDataFormat = (String) network.get("employee_data_format");
            this.checkDataFormat = (String) network.get("check_data_format");
            this.dateTimeFormat = (String) network.get("datetime_format");
            this.timeout = Integer.parseInt(network.get("server_timeout").toString());
            this.sleepDuration = Integer.parseInt(network.get("sleep_duration").toString());
        }
        catch (NullPointerException e)
        {
            System.err.println("Network configurations load failed, the file might not exist or might be wrongly constructed");
        }
    }

    /**
     * Create the server socket and set it's timeout duration
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void setConnection () throws IOException, IllegalArgumentException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout);
    }

    /**
     * Print some message in the console with a prefix that indicates that it's the server that wrote it
     * @param msg the message to print
     */
    public void println (String msg)
    {
        System.out.println("Server > " + msg);
    }

    /**
     * Receive some data from a client
     * @param client the client
     * @return the received data as a String
     * @throws Exception
     */
    public String receive (Socket client) throws Exception
    {
        InputStream     is  = client.getInputStream();
        DataInputStream in  = new DataInputStream(is);
        String          str = in.readUTF();
        println("Received !");
        return str;
    }
}
