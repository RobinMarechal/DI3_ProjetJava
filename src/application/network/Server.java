package application.network;

import fr.etu.univtours.marechal.SimpleDateTime;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import application.models.Company;
import application.models.Employee;

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
    public Server (int port)
    {
        super(port);
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

                if (receive.equals("SYNC"))
                {
                    sendEmployeeList(out);
                    System.out.println("SYNC done");
                }
                else
                {
                    out.writeInt(0);
                    simulateCheck(receive);
                }

                client.close();
                serverSocket.close();
                Thread.sleep(50);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                break;

            }
        }
    }

    private void simulateCheck (String receive)
    {
        //datetime;id
        try{
            int id = Integer.parseInt(receive.split(";")[1]);
            String strDT = receive.split(";")[0];

            LocalDateTime  ldt = LocalDateTime.parse(strDT, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            SimpleDateTime std = SimpleDateTime.fromLocalDateTime(ldt);

            Employee e = Company.getCompany().getEmployee(id);

            if(e != null)
            {
                Platform.runLater(() ->  e.doCheck(std));
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
            String str = e.getId() + ";" + e.getFirstName() + e.getLastName();
            out.writeUTF(str);
        }
    }
}

