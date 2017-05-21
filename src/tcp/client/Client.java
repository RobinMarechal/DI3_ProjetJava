package tcp.client;

import java.util.Scanner;

/**
 * Created by Robin on 21/05/2017.
 */
public class Client extends ClientBuilder implements Runnable
{
    private final ClientView view;

    public Client (ClientView view, int port, String IP)
    {
        super(port, IP);
        this.view = view;
    }

    @Override
    public void run ()
    {
        int    tries = 0;
        Scanner sc = new Scanner(System.in);
        String msg;
        while (true)
        {
            try
            {
                setConnection();
                println("Connected");
                tries = 0;

                System.out.println("Client > ");
                msg = sc.nextLine();
                send(msg, socketClient);

                String received = receive(socketClient);
                println(received);

                view.setMsgReceived(received);

                if (received.equalsIgnoreCase("stop"))
                {
                    socketClient.close();
                    println("Connection closed");
                    break;
                }

                socketClient.close();
            }
            catch (Exception e)
            {
                println("Unable to connect...");
                tries++;
                if (tries == 30)
                {
                    break;
                }
            }
        }

        println("Connection ended.");
    }

    public boolean send (String text)
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
}
