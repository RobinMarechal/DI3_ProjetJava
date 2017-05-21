package tcp.client;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Robin on 21/05/2017.
 */
public class ClientView extends Application
{
    private Client client;
    private Thread thread;

    private String msgReceived = null;

    public ClientView ()
    {
        client = new Client(this, 8000, "localhost");
        thread = new Thread(client);
        thread.start();
    }

    public static void main (String[] args)
    {
        new ClientView();
        launch(args);
    }

    @Override
    public void start (Stage w) throws Exception
    {
        VBox root = new VBox(20);
        w.setScene(new Scene(root));

        w.setWidth(500);
        w.setHeight(350);
        root.setPadding(new Insets(20));

        ListView<Label> list = new ListView<>();
        list.setPrefWidth(500);
        list.setPrefHeight(250);

        TextField input = new TextField();
        input.setPrefWidth(300);
        Button btn = new Button("Send");
        btn.setPrefWidth(150);
        HBox hbox = new HBox(30, input, btn);

        root.getChildren().addAll(list, hbox);

        btn.setOnAction(event -> sendMsg(input, list));
        input.addEventFilter(KeyEvent.KEY_RELEASED, event ->
        {
            if (event.getCode() == KeyCode.ENTER)
            {
                sendMsg(input, list);
            }
        });

        w.show();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call () throws Exception
            {
                if(msgReceived != null)
                {
                    Label l = new Label(msgReceived);
                    list.getItems().add(l);
                    msgReceived = null;
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void sendMsg (TextField input, ListView<Label> list)
    {
        String text = input.getText();
        if (text != "")
        {
            Label l = new Label(text);
            l.setMaxWidth(Double.MAX_VALUE);
            l.setAlignment(Pos.CENTER_RIGHT);
            if (client.send(text))
            {
                list.getItems().add(l);
            }
            input.clear();
        }
    }

    public void setMsgReceived (String receivedMessage)
    {
        this.msgReceived = receivedMessage;
    }
}
