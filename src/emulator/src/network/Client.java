package network;

import com.sun.istack.internal.NotNull;
import controllers.Controller;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Check;
import models.Employee;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Robin on 26/05/2017. <br>
 * This client represents the TCP client
 */
public class Client extends ClientBuilder implements Runnable
{
    private static final long serialVersionUID = -1001635844537299371L;

    /** The path of the serialization file */
    private String serializationFilePath;
    /** The main stage */
    private transient Stage window;
    /** the MVC's controller */
    private Controller controller;
    /** true if the a synchronisation of the list of employees has been asked and has not been done yet */
    private transient boolean sync = false;

    /** The checks queue. The elements are sent one by one to the server when it's online */
    private CopyOnWriteArrayList<String> queue = new CopyOnWriteArrayList<>();
    /** The size of the queue. Used to be bound to the view */
    private transient IntegerProperty pendingChecks;

    /**
     * Default constructor
     */
    public Client ()
    {
    }

    /**
     * 2 parameters conctructor
     *
     * @param window the main window
     * @param config the JSON object containing the network configs
     */
    public Client (@NotNull Stage window, JSONObject config)
    {
        super(config);
        initialize();
        prepareWindow(window);
        startClient();
    }

    /**
     * Start the server side into a new thread
     */
    private void startClient ()
    {
        new Thread(this).start();
    }

    /**
     * Serialize the Client and the Controller
     */
    private void serialize ()
    {
        try
        {
            // We create the folders if they doesn't exist yet
            String dirPath = serializationFilePath.substring(0, serializationFilePath.lastIndexOf("/"));
            new File(dirPath).mkdirs();

            FileOutputStream   fileOut = new FileOutputStream(serializationFilePath);
            ObjectOutputStream out     = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + serializationFilePath);
        }
        catch (IOException e)
        {
            System.err.println("Serialization failed");
            e.printStackTrace();
        }
    }

    /**
     * Create a {@link Client} object and starts the server side into a new thread
     *
     * @param window             the main window
     * @param config             the JSON object containing the network configurations
     * @param serializedDataPath the serialization file path
     * @return the created {@link Client} instance
     */
    public static Client start (@NotNull Stage window, JSONObject config, @NotNull String serializedDataPath)
    {
        Client client;

        try
        {
            File file = new File(serializedDataPath);

            FileInputStream   fileIn = new FileInputStream(file);
            ObjectInputStream in     = new ObjectInputStream(fileIn);
            client = (Client) in.readObject();
            in.close();
            fileIn.close();

            client.initialize();
            client.pendingChecks.setValue(client.queue.size());
            client.setConfig(config);
            client.prepareWindow(window);
            client.startClient();

            System.out.println("Client emulator deserialization done");
        }
        catch (Exception e)
        {
            System.err.println("Client emulator deserialization failed, maybe the serialization file doesn't exist");
            client = new Client(window, config);
        }

        client.setSerializationFilePath(serializedDataPath);

        return client;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run ()
    {
        long counter = 0;
        while (true)
        {
            try
            {
                if (!queue.isEmpty() || counter % syncFrequency == 0 || sync)
                {
                    setConnection();
                    println("Connected");

                    OutputStream     os  = socketClient.getOutputStream();
                    DataOutputStream out = new DataOutputStream(os);

                    if (counter % syncFrequency == 0 || sync)
                    {
                        sync(out);
                        syncIsDone();
                        counter = 1;
                    }
                    else if (!queue.isEmpty())
                    {
                        sendCheck(out);
                        removeFirstFromQueue();
                    }

                    InputStream     inputStream = socketClient.getInputStream();
                    DataInputStream in          = new DataInputStream(inputStream);

                    int response = in.readInt();

                    if (response > 0)
                    {
                        receiveEmployeeList(response, in);
                    }

                    out.close();
                    in.close();
                    socketClient.close();
                }

                //                counter++;
                counter++;
                Thread.sleep(sleepDuration);
            }
            catch (IOException e)
            {
                println("Server offline");
                Platform.runLater(() -> isServerOnline.setValue(false));
            }
            catch (Exception e)
            {
                println("Exception : " + e.getClass().getSimpleName());
                e.printStackTrace();
                Platform.runLater(() -> isServerOnline.setValue(false));
                break;
            }
        }
    }

    /**
     * Reception of the list of employees sent by the server
     *
     * @param nbOfIncomingPackets sent by the server, representing the number of packets the client should receive
     * @param in                  the input stream to read
     * @throws IOException
     */
    protected void receiveEmployeeList (int nbOfIncomingPackets, @NotNull DataInputStream in) throws IOException
    {
        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < nbOfIncomingPackets; i++)
        {
            String[] received       = in.readUTF().split(";");
            String[] receivedFormat = Employee.getEmployeeDataFormat().split(";");

            if (received.length == 2)
            {
                int    id;
                String name;

                if (receivedFormat[0].equals("{id}"))
                {
                    id = Integer.parseInt(received[0]);
                    name = received[1];
                }
                else
                {
                    id = Integer.parseInt(received[1]);
                    name = received[0];
                }

                employees.add(new Employee(name, id));
            }
        }

        Platform.runLater(() -> controller.setEmployees(employees));
    }

    /**
     * Send a simulated check to the server
     *
     * @param out the stream to write
     * @throws IOException
     */
    protected void sendCheck (@NotNull DataOutputStream out) throws IOException
    {
        out.writeUTF(queue.get(0));
        println("Check : " + queue.get(0));
    }

    /**
     * Sent a synchronisation check request to the server
     *
     * @param out the stream to write
     * @throws IOException
     */
    protected void sync (@NotNull DataOutputStream out) throws IOException
    {
        out.writeUTF(syncRequest);
        println(syncRequest);
    }

    /**
     * Ask the {@link Client} for a synchronisation of the list of employees. <br>
     * Nothing is sent, it only sets the <tt>sync</tt> attribute to <tt>true</tt> <br>
     * This method is <b>thread safe</b>
     */
    public synchronized void askForSync ()
    {
        sync = true;
    }

    /**
     * Sets the <tt>sync</tt> attribute to <tt>false</tt>, which means that the synchronisation request has been sent <br>
     * This method is <b>thread safe</b>
     */
    private synchronized void syncIsDone ()
    {
        sync = false;
    }

    /**
     * Add a {@link Check} instance to the sending queue. <br>
     * This method is <b>thread safe</b>
     *
     * @param checkToSend the {@link Check} instance to add to the sending queue
     */
    public synchronized void addToQueue (@NotNull Check checkToSend)
    {
        String toSend = checkToSend.toString();

        queue.add(toSend);
        Platform.runLater(() -> pendingChecks.setValue(pendingChecks.getValue() + 1));
    }

    /**
     * Remove the first element from the sending queue. <br>
     * This method is <tt>thread safe</tt>
     */
    private synchronized void removeFirstFromQueue ()
    {
        queue.remove(0);
        Platform.runLater(() -> pendingChecks.setValue(pendingChecks.getValue() - 1));
    }

    /**
     * prepare the main window
     *
     * @param window the main window
     */
    public void prepareWindow (@NotNull Stage window)
    {
        this.window = window;

        if (this.controller == null)
        {
            this.controller = new Controller(this, null);
        }

        window.setTitle("Emulator");
        window.setScene(new Scene(this.controller.displayView()));
        window.setResizable(false);
        window.show();
        window.setOnCloseRequest(event ->
        {
            serialize();
            System.exit(1);
        });
    }

    /**
     * Set the serialization file path
     *
     * @param serializationFilePath the serialization file path
     */
    public void setSerializationFilePath (@NotNull String serializationFilePath)
    {
        this.serializationFilePath = serializationFilePath;
    }

    /**
     * Get the number of pending checks
     *
     * @return the number of pending checks
     */
    public int getPendingChecks ()
    {
        return pendingChecks.get();
    }

    /**
     * Get the number of pending checks property
     *
     * @return the number of pending checks property
     */
    public IntegerProperty pendingChecksProperty ()
    {
        return pendingChecks;
    }

    /**
     * Initialize the object and the pending checks property
     */
    @Override
    protected void initialize ()
    {
        super.initialize();
        pendingChecks = new SimpleIntegerProperty(this, "pendingChecks", 0);
    }
}
