package emulator.network;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Robin on 26/05/2017.
 */
public class ClientBuilder
{
    protected final int sleepDuration;
    protected final int syncFrequency;
    protected Socket socketClient;
    protected final String hostname;
    protected final int port;
    protected final String syncRequest;
    protected final String employeeDataFormat;
    protected final String checkDataFormat;
    protected final String dateTimeFormat;


    public ClientBuilder (JSONObject config)
    {
        JSONObject network = (JSONObject) config.get("network");

        this.port = Integer.parseInt(network.get("port").toString());
        this.hostname = (String) network.get("hostname");
        this.syncRequest = (String) network.get("sync_request");
        this.employeeDataFormat = (String) network.get("employee_data_format");
        this.checkDataFormat = (String) network.get("check_data_format");
        this.dateTimeFormat = (String) network.get("datetime_format");
        this.sleepDuration = Integer.parseInt(network.get("sleep_duration").toString());
        this.syncFrequency = Integer.parseInt(network.get("sync_frequency").toString());
    }

    public void setConnection () throws IllegalArgumentException, IOException
    {
        socketClient = new Socket(hostname, port);
    }

    public void println (String msg)
    {
        System.out.println("Client > " + msg);
    }
}
