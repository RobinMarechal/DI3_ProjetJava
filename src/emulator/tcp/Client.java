package emulator.tcp;

import emulator.controllers.Controller;
import emulator.models.Employee;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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

    public Client (Stage window, String hostname, int port)
    {
        super(hostname, port);
        this.window = window;
        this.controller = new Controller(this, null);
        window.setScene(new Scene(this.controller.displayView()));
        window.setResizable(false);
        window.show();

        new Thread(this).start();
    }

    public static Client start (Stage window, String hostname, int port)
    {
        return new Client(window, hostname, port);
    }


    @Override
    public void run ()
    {
        int c = 0;
        while (true)
        {
            try
            {
                if (!queue.isEmpty() || c % 60 == 0 || sync)
                {
                    setConnection();
                    println("Connected");

                    OutputStream     outputStream = socketClient.getOutputStream();
                    DataOutputStream out          = new DataOutputStream(outputStream);

                    if (c % 60 == 0 || sync)
                    {
                        out.writeUTF("SYNC");
                        System.out.println("SYNC");
                        syncIsDone();
                    }
                    else if (!queue.isEmpty())
                    {
                        out.writeUTF(queue.get(0));
                        System.out.println(queue.get(0));
                        removeFirstFromQueue();
                    }

                    InputStream     inputStream = socketClient.getInputStream();
                    DataInputStream in          = new DataInputStream(inputStream);
                    int             response    = in.readInt();

                    if (response > 0)
                    {
                        ObservableList<Employee> employees = FXCollections.observableArrayList();
                        for (int i = 0; i < response; i++)
                        {
                            String   s     = in.readUTF();
                            String[] split = s.split(";");
                            if (split.length >= 2)
                            {
                                int    id   = Integer.parseInt(split[0]);
                                String name = split[1];
                                employees.add(new Employee(name, id));
                            }
                        }

                        Platform.runLater(() -> controller.setEmployees(employees));
                    }

                    out.close();
                    in.close();
                    socketClient.close();
                }
                c++;
                Thread.sleep(50);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    protected boolean send (String text)
    {
        if (socketClient != null && !socketClient.isClosed())
        {
            try
            {
                send(text, socketClient);
                System.out.println("Sent");
                return true;
            }
            catch (Exception e)
            {
                System.out.println("Not sent");
                return false;
            }
        }

        System.out.println("Not sent");

        return false;
    }

    public synchronized void askForSync ()
    {
        sync = true;
    }

    private synchronized void syncIsDone ()
    {
        sync = false;
    }

    public synchronized void addToQueue (String s)
    {
        queue.add(s);
    }

    private synchronized void removeFirstFromQueue ()
    {
        queue.remove(0);
    }
}
