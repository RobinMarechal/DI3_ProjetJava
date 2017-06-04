package emulator.network;

import emulator.controllers.Controller;
import emulator.models.Check;
import emulator.models.Employee;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Robin on 26/05/2017.
 */
public class Client extends ClientBuilder implements Runnable
{
    private static final long serialVersionUID = -1001635844537299371L;

    private String serializationFilePath;
    private transient Stage window;
    private Controller controller;
    private transient boolean sync = false;

    private CopyOnWriteArrayList<String> queue = new CopyOnWriteArrayList<>();
    private transient IntegerProperty pendingChecks;

    public Client ()
    {
    }

    public Client (Stage window, JSONObject config)
    {
        super(config);
        setWindow(window);
        startClient();
    }

    private void startClient ()
    {
        new Thread(this).start();
    }

    private void serialize ()
    {
        try
        {
            FileOutputStream   fileOut = new FileOutputStream(serializationFilePath);
            ObjectOutputStream out     = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + serializationFilePath);
        }
        catch (IOException e)
        {
            System.out.println("Serialization failed");
            e.printStackTrace();
        }
    }

    public static Client start (Stage window, JSONObject config, String serializedDataPath)
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
            client.setConfig(config);
            client.setWindow(window);
            client.pendingChecks.setValue(client.queue.size());

            System.out.println("Client emulator deserialization OK");
        }
        catch (Exception e)
        {
            System.out.println("Client emulator deserialization failed");
            e.printStackTrace();
            client = new Client(window, config);
        }

        client.setSerializationFilePath(serializedDataPath);
        client.startClient();

        return client;
    }

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
                Platform.runLater(() -> isServerOnline.setValue(false));
                break;
            }
        }
    }

    protected void receiveEmployeeList (int response, DataInputStream in) throws IOException
    {
        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < response; i++)
        {
            String[] received       = in.readUTF().split(";");
            String[] receivedFormat = employeeDataFormat.split(";");

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

    protected void sendCheck (DataOutputStream out) throws IOException
    {
        out.writeUTF(queue.get(0));
        println("Check : " + queue.get(0));
    }

    protected void sync (DataOutputStream out) throws IOException
    {
        out.writeUTF(syncRequest);
        println(syncRequest);
    }

    public synchronized void askForSync ()
    {
        sync = true;
    }

    private synchronized void syncIsDone ()
    {
        sync = false;
    }

    public synchronized void addToQueue (Check c)
    {
        String format      = new String(checkDataFormat);
        String strDateTime = c.getDateTime().toLocalDateTime().format(DateTimeFormatter.ofPattern(dateTimeFormat));
        int    id          = c.getEmployeeId();

        String toSend = format.replace("{id}", id + "").replace("{datetime}", strDateTime);

        queue.add(toSend);
        Platform.runLater(() -> pendingChecks.setValue(pendingChecks.getValue() + 1));
    }

    private synchronized void removeFirstFromQueue ()
    {
        queue.remove(0);
        Platform.runLater(() -> pendingChecks.setValue(pendingChecks.getValue() - 1));
    }

    public void setWindow (Stage window)
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

    public void setSerializationFilePath (String serializationFilePath)
    {
        this.serializationFilePath = serializationFilePath;
    }

    public int getPendingChecks ()
    {
        return pendingChecks.get();
    }

    public IntegerProperty pendingChecksProperty ()
    {
        return pendingChecks;
    }

    @Override
    protected void initialize ()
    {
        super.initialize();
        pendingChecks = new SimpleIntegerProperty(this, "pendingChecks", 0);
    }
}
