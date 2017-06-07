package network;

import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import models.Check;
import models.Employee;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Robin on 26/05/2017. <br>
 * This class is a TCP client builder
 */
public class ClientBuilder implements Serializable
{
    private static final long serialVersionUID = -783484054127761751L;

    /** The client socket */
    protected transient Socket socketClient;
    /** The pause duration between each client iteration */
    protected int sleepDuration = 50;
    /** The number of iteration between each synchronisation request */
    protected int syncFrequency = 50;
    /** the server hostname */
    protected String hostname = "localhost";
    /** The server port */
    protected int port = 8080;
    /** The list of employees synchronisation request */
    protected String syncRequest = "SYNC";
    /** The format of the reveiving employees */
    protected String employeeDataFormat = "{id};{name}";
    /** The format of the sending checks */
    protected String checkDataFormat = "{datetime};{id}";
    /** The format of the datetime of the checks */
    protected String dateTimeFormat = "yyyy-MM-dd HH:mm";

    /** Property telling if the server is online or not */
    protected transient BooleanProperty isServerOnline;

    /**
     * Default constructor
     */
    public ClientBuilder ()
    {
    }

    /**
     * Configuration contructor
     * @param config the JSON object containing the client configurations
     */
    public ClientBuilder (JSONObject config)
    {
        setConfig(config);
    }

    /**
     * Create the client socket
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void setConnection () throws IllegalArgumentException, IOException
    {
        socketClient = new Socket(hostname, port);
        Platform.runLater(() -> isServerOnline.setValue(true));
    }

    /**
     * Print a message to the console with a prefix showing that the message comes from the TCP client
     * @param msg the message to print
     */
    public void println (String msg)
    {
        System.out.println("Client > " + msg);
    }

    /**
     * Prepare the configuration of the TCP client
     * @param config a JSON object containing the configurations of the TCP client
     */
    public void setConfig (@NotNull JSONObject config)
    {
        try
        {
            JSONObject network = (JSONObject) config.get("network");

            int port = Integer.parseInt(network.get("port").toString());
            String hostname = (String) network.get("hostname");
            String syncRequest = (String) network.get("sync_request");
            String employeeDataFormat = (String) network.get("employee_data_format");
            String checkDataFormat = (String) network.get("check_data_format");
            String dateTimeFormat = (String) network.get("datetime_format");
            int sleepDuration = Integer.parseInt(network.get("sleep_duration").toString());
            int syncFrequency = Integer.parseInt(network.get("sync_frequency").toString());

            this.port = port;
            this.hostname = hostname;
            this.syncRequest = syncRequest;
            this.employeeDataFormat = employeeDataFormat;
            this.checkDataFormat = checkDataFormat;
            this.dateTimeFormat = dateTimeFormat;
            this.sleepDuration = sleepDuration;
            this.syncFrequency = syncFrequency;
        }
        catch (Exception e)
        {
            System.err.println("Network configurations loading failed, the file might not exist or might be wrongly constructed.\nA new "
                    + "one has been created with default values");

            JSONObject defaultConf = new JSONObject();
            JSONObject network     = new JSONObject();
            network.put("port", this.port);
            network.put("hostname", this.hostname);
            network.put("sync_request", this.syncRequest);
            network.put("employee_data_format", this.employeeDataFormat);
            network.put("check_data_format", this.checkDataFormat);
            network.put("datetime_format", this.dateTimeFormat);
            network.put("sleep_duration", this.sleepDuration);
            network.put("sync_frequency", this.syncFrequency);

            defaultConf.put("network", network);

            try
            {
                String path  = new File(".").getCanonicalPath() + "/data/config/clientConfig.json";
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

        Check.setCheckDataFormat(checkDataFormat);
        Check.setDateTimeFormat(dateTimeFormat);
        Employee.setEmployeeDataFormat(employeeDataFormat);
    }

    /**
     * Know if the server is online or not
     * @return True if the server is oneline, false otherwise
     */
    public boolean isServerOnline ()
    {
        return isServerOnline.get();
    }

    /**
     * Get the isServerOnline propety which can be used for bindings
     * @return the isServerOnline propety which can be used for bindings
     */
    public BooleanProperty isServerOnlineProperty ()
    {
        return isServerOnline;
    }

    /**
     * Initialize the object instance
     */
    protected void initialize ()
    {
        isServerOnline = new SimpleBooleanProperty(this, "isServerOnline", false);
    }

}
