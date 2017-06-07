package network;

import lib.Helper;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Robin on 21/05/2017.<br>
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
    protected int port = 8080;
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

            int port = Integer.parseInt(network.get("port").toString());
            String syncRequest = (String) network.get("sync_request");
            String employeeDataFormat = (String) network.get("employee_data_format");
            String checkDataFormat = (String) network.get("check_data_format");
            String dateTimeFormat = (String) network.get("datetime_format");
            int timeout = Integer.parseInt(network.get("server_timeout").toString());
            int sleepDuration = Integer.parseInt(network.get("sleep_duration").toString());

            this.port = port;
            this.syncRequest = syncRequest;
            this.employeeDataFormat = employeeDataFormat;
            this.checkDataFormat = checkDataFormat;
            this.dateTimeFormat = dateTimeFormat;
            this.timeout = timeout;
            this.sleepDuration = sleepDuration;
        }
        catch (Exception e)
        {
            System.err.println("Network configurations loading failed, the file might not exist or might be wrongly constructed.\nA new " +
                    "one has been created with default values");
            
            JSONObject defaultConf = new JSONObject();
            JSONObject network = new JSONObject();
            network.put("port", this.port);
            network.put("sync_request", this.syncRequest);
            network.put("employee_data_format", this.employeeDataFormat);
            network.put("check_data_format", this.checkDataFormat);
            network.put("datetime_format", this.dateTimeFormat);
            network.put("server_timeout", this.timeout);
            network.put("sleep_duration", this.sleepDuration);

            defaultConf.put("network", network);

            try
            {
                String path  = Helper.serverConfigFilePath();
                File conf = new File(path);
                if(!conf.exists())
                {
                    String dir = path.substring(0, path.lastIndexOf("/"));
                    new File(dir).mkdirs();
                }

                FileWriter fw = new FileWriter(conf);
                fw.write(defaultConf.toJSONString());
                fw.close();
            }
            catch (IOException e1)
            {
                System.err.println("Impossible to create the server config file.");
            }
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
    public void println (@NotNull String msg)
    {
        System.out.println("Server > " + msg);
    }

    /**
     * Receive some data from a client
     * @param client the client
     * @return the received data as a String
     * @throws Exception
     */
    public String receive (@NotNull Socket client) throws Exception
    {
        InputStream     is  = client.getInputStream();
        DataInputStream in  = new DataInputStream(is);
        String          str = in.readUTF();
        println("Received !");
        return str;
    }
}
