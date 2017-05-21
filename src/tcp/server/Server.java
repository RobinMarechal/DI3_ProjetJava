package tcp.server;

import java.net.Socket;
import java.util.Scanner;

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
        Scanner sc = new Scanner(System.in);
        String  msg;
        while (true)
        {
            try
            {
                setConnection();
                println("Connected");
                Socket client = serverSocket.accept();

                String receive = receive(client);
                println(receive);

                if (receive.equalsIgnoreCase("stop"))
                {
                    client.close();
                    serverSocket.close();
                    println("Connection closed");
                    break;
                }

                msg = "You said : '" + receive + "'";//sc.nextLine();
                send(msg, client);

                if (msg.equalsIgnoreCase("stop"))
                {
                    client.close();
                    serverSocket.close();
                    println("Connection closed");
                    break;
                }

                client.close();
                serverSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                break;

            }
        }
    }
}

