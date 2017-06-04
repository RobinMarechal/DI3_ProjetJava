package emulator.views;

import emulator.controllers.Controller;
import emulator.models.Employee;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 26/05/2017.
 */
public class View implements Initializable
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 340;

    private final Controller controller;
    private VBox layout;

    @FXML private Label labServerState;
    @FXML private Label labPendingChecks;
    @FXML private DatePicker datePicker;
    @FXML private TextField fieldHours;
    @FXML private TextField fieldMinutes;
    @FXML private Button btnSync;
    @FXML private Button btnCheck;
    @FXML private Button btnReset;
    @FXML private Label labHours;
    @FXML private Label labMinutes;
    @FXML private ListView<Employee> listView;

    public View (Controller controller)
    {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/emulator/views/fxml/view.fxml"));
        loader.setController(this);

        try
        {
            layout = loader.load();
            layout.setPrefHeight(HEIGHT);
            layout.setPrefWidth(WIDTH);
        }
        catch (IOException e)
        {
            layout = new VBox();
            System.out.println("Failed to load emulator's view...");
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        datePicker.setValue(SimpleDate.TODAY.toLocalDate());
        //        prepareTimeFieldsBehavior();
        prepareButtonEvents();
        preparePendingChecksLabel();
        prepareServerStateLabel();
    }

    //    private void prepareTimeFieldsBehavior ()
    //    {
    //        LocalTime now = LocalTime.now();
    //        fieldHours.setText(now.getHour() + "");
    //        fieldMinutes.setText(now.getMinute() + "");
    //    }

    private void prepareButtonEvents ()
    {
        btnCheck.setOnAction(event -> controller.check());
        btnSync.setOnAction(event -> controller.sync());
        btnReset.setOnAction(event -> controller.resetTimeFields());
    }

    private void preparePendingChecksLabel ()
    {
        labPendingChecks.textProperty().bind(controller.getClient().pendingChecksProperty().asString());
    }

    private void prepareServerStateLabel ()
    {
        ObservableList<String> styleClass = labServerState.getStyleClass();

        styleClass.addAll("text-bold", "text-red");
        btnSync.getStyleClass().add("btn-desactivated");
        btnSync.setDisable(true);

        controller.getClient().isServerOnlineProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue.booleanValue())
            {
                styleClass.remove("text-red");
                styleClass.add("text-green");
                labServerState.setText("Online");
                btnSync.getStyleClass().remove("btn-desactivated");
                btnSync.setDisable(false);
            }
            else
            {
                styleClass.remove("text-green");
                styleClass.add("text-red");
                labServerState.setText("Offline");
                btnSync.getStyleClass().add("btn-desactivated");
                btnSync.setDisable(true);
            }
        });
    }

    // Getters

    public VBox getLayout ()
    {
        return layout;
    }

    public TextField getFieldHours ()
    {
        return fieldHours;
    }

    public TextField getFieldMinutes ()
    {
        return fieldMinutes;
    }

    public Label getLabHours ()
    {
        return labHours;
    }

    public Label getLabMinutes ()
    {
        return labMinutes;
    }

    public ListView<Employee> getListView ()
    {
        return listView;
    }

    public DatePicker getDatePicker ()
    {
        return datePicker;
    }

    public Label getLabServerState ()
    {
        return labServerState;
    }

    public Label getLabPendingChecks ()
    {
        return labPendingChecks;
    }
}
