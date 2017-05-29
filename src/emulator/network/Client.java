package emulator.network;

import emulator.controllers.Controller;
import emulator.models.Check;
import emulator.models.Employee;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.SocketTimeoutException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Robin on 26/05/2017.
 */
public class Client extends ClientBuilder implements Runnable
{
    private Stage window;
    private CopyOnWriteArrayList<String> queue = new CopyOnWriteArrayList<>();
    private Controller controller;
    private boolean sync = false;

    public Client (Stage window, JSONObject config)
    {
        super(config);
        this.window = window;
        this.controller = new Controller(this, null);
        window.setScene(new Scene(this.controller.displayView()));
        window.setResizable(false);
        window.show();
        window.setOnCloseRequest(event -> System.exit(1));

        new Thread(this).start();
    }

    public static Client start (Stage window, JSONObject config)
    {
        return new Client(window, config);
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

                    OutputStream     outputStream = socketClient.getOutputStream();
                    DataOutputStream out          = new DataOutputStream(outputStream);

                    if (counter % syncFrequency == 0 || sync)
                    {
                        sync(out);
                        syncIsDone();
                        counter = 0;
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

                counter++;
                Thread.sleep(sleepDuration);
            }
            catch (SocketTimeoutException e)
            {
                println("The client has timed-out.");
            }
            catch (IOException e)
            {
                println("IOException");
            }
            catch (Exception e)
            {
                println("Exception : " + e.getClass().getSimpleName());
                break; // ?
            }
        }
    }

    protected void receiveEmployeeList (int response, DataInputStream in) throws IOException
    {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
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
        System.out.println(queue.get(0));
    }

    protected void sync (DataOutputStream out) throws IOException
    {
        out.writeUTF(syncRequest);
        System.out.println(syncRequest);
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
    }

    private synchronized void removeFirstFromQueue ()
    {
        queue.remove(0);
    }
}
