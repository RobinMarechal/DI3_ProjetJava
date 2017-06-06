package emulator.network;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Robin on 26/05/2017.
 */
public class ClientBuilder implements Serializable
{
    private static final long serialVersionUID = -783484054127761751L;

    protected transient Socket socketClient;
    protected int sleepDuration = 50;
    protected int syncFrequency = 50;
    protected String hostname = "localhost";
    protected int port = 8888;
    protected String syncRequest = "SYNC";
    protected String employeeDataFormat = "{id};{name}";
    protected String checkDataFormat = "{datetime};{id}";
    protected String dateTimeFormat = "yyyy-MM-dd HH:mm";

    protected transient BooleanProperty isServerOnline;

    public ClientBuilder ()
    {
    }

    public ClientBuilder (JSONObject config)
    {
        setConfig(config);
    }

    public void setConnection () throws IllegalArgumentException, IOException
    {
        socketClient = new Socket(hostname, port);
        Platform.runLater(() -> isServerOnline.setValue(true));
    }

    public void println (String msg)
    {
        System.out.println("Client > " + msg);
    }

    public void setConfig (JSONObject config)
    {
        try
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
        catch (NullPointerException e)
        {
            System.err.println("Network configurations load failed, the file might not exist or might be wrongly constructed");
        }
    }

    public boolean isIsServerOnline ()
    {
        return isServerOnline.get();
    }

    public BooleanProperty isServerOnlineProperty ()
    {
        return isServerOnline;
    }

    protected void initialize ()
    {
        isServerOnline = new SimpleBooleanProperty(this, "isServerOnline", false);
    }

}
