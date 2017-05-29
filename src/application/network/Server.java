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
 * Created by Robin on 21/05/2017.
 */
public class Server extends ServerBuilder implements Runnable
{
    public Server (JSONObject config)
    {
        super(config);
    }

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
                            sendEmployeeList(out);
                            System.out.println(syncRequest + " done");
                        }
                        else
                        {
                            out.writeInt(0);
                            simulateCheck(receive);
                        }

                        client.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        println("Exception");
                    }

                }).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

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
            System.err.println("check failed");
        }

    }

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

