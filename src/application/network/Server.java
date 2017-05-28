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
import java.net.SocketTimeoutException;
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
        while (true)
        {
            try
            {
                setConnection();
                println("Connected");
                Socket client = serverSocket.accept();
                println("Accepted");

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
                serverSocket.close();
                Thread.sleep(sleepDuration);
            }
            catch (SocketTimeoutException e)
            {
                println("The server has timed-out.");
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

    private void simulateCheck (String receive)
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

