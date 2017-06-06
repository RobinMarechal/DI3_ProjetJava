package application.network;

import application.models.Company;
import application.models.Employee;
import fr.etu.univtours.marechal.SimpleDateTime;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Robin on 21/05/2017. <br/>
 * This class represents the network part of the application that receives data from the emulator
 */
public class Server extends ServerBuilder implements Runnable
{
    /**
     * 1 parameter constructor
     *
     * @param config the config
     */
    public Server (JSONObject config)
    {
        super(config);
    }

    /**
     * Runs the server which should receive from clients and answer to them.
     */
    @Override
    public void run ()
    {
        try
        {
            setConnection();
            println("Connected");
            while (true)
            {
                // We can accept multiple clients
                Socket client = serverSocket.accept();
                println("Client accepted");

                // For each new client, we start a new Thread

                new Thread(() ->
                {
                    try
                    {
                        String receive = receive(client);
                        println(receive);

                        OutputStream     outputStream = client.getOutputStream();
                        DataOutputStream out          = new DataOutputStream(outputStream);

                        if (receive.equals(syncRequest))
                        {
                            // It's a sync request
                            sendEmployeeList(out);
                            System.out.println(syncRequest + " done");
                        }
                        else
                        {
                            // It's not a sync request, so it should be a check simulation
                            out.writeInt(0);
                            simulateCheck(receive);
                        }

                        client.close();
                    }
                    catch (java.lang.Exception e)
                    {
                        println("Exception");
                    }

                }).start();
            }
        }
        catch (IOException e)
        {
            System.err.println("An error occured while created during the communication");
        }
        
        try
        {
            serverSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("The server socket couldn't be closed.");
        }
    }

    /**
     * Simulate a check for an employee with the received data. <br/>
     * If the received data is incorrect, then is does nothing
     * @param receive the data that should contain a employee's check information (ID and datetime)
     */
    private synchronized void simulateCheck (String receive)
    {
        try
        {
            String data[]       = receive.split(";");
            String dataFormat[] = checkDataFormat.split(";");

            int    id    = -1;
            String strDT = null;

            for (int i = 0; i < dataFormat.length; i++)
            {
                if (dataFormat[i].equals("{id}"))
                {
                    id = Integer.parseInt(data[i]);
                }
                else
                {
                    strDT = data[i];
                }
            }

            LocalDateTime  ldt = LocalDateTime.parse(strDT, DateTimeFormatter.ofPattern(dateTimeFormat));
            SimpleDateTime std = SimpleDateTime.fromLocalDateTime(ldt);

            Employee e = Company.getCompany().getEmployee(id);

            if (e != null)
            {
                Platform.runLater(() -> e.doCheck(std));
            }
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException e)
        {
            System.err.println("Failed to simulate a check");
        }

    }

    /**
     * Send the employee's list <br/>
     * First, it sends an integer representing the number of employees that will be sent. <br/>
     * then, foreach employees, it sends a string containg and employee's name and ID.
     * @param out the destination out stream
     * @throws IOException
     */
    private void sendEmployeeList (DataOutputStream out) throws IOException
    {
        ObservableList<Employee> list = Company.getCompany().getEmployeesList();

        out.writeInt(list.size());

        for (Employee e : list)
        {
            String format = new String(employeeDataFormat);
            String toSend = format.replace("{id}", e.getId() + "").replace("{name}", e.getFirstName() + " " + e.getLastName());
            out.writeUTF(toSend);
        }
    }
}

