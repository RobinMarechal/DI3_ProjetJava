package views;

import com.sun.istack.internal.NotNull;
import controllers.Controller;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Employee;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 26/05/2017. <br>
 * This class display the principal view of the emulator
 */
public class View implements Initializable
{
    /** The width of the window */
    public static final int WIDTH = 600;
    /** The height of the window */
    public static final int HEIGHT = 340;

    /** The MVC's controller */
    private final Controller controller;
    /** The layout */
    private VBox layout;

    /** The label bound with de server state Client's attribute */
    @FXML private Label labServerState;
    /** The label showing the number of pending checks */
    @FXML private Label labPendingChecks;
    /** The datepicker to select a date */
    @FXML private DatePicker datePicker;
    /** The hours field to enter an hour */
    @FXML private TextField fieldHours;
    /** the minutes field to entier a number of minutes */
    @FXML private TextField fieldMinutes;
    /** The button used to ask the server for a sychronisation of the list of employees */
    @FXML private Button btnSync;
    /** The button to add a check to send to the server */
    @FXML private Button btnCheck;
    /** The button to reset the time fields to the actual time */
    @FXML private Button btnReset;
    /** The label showing the hour of the check */
    @FXML private Label labHours;
    /** The label showing the numbe of minutes of the checks (rounded to the nearest quarter) */
    @FXML private Label labMinutes;
    /** The {@link javafx.scene.control.ListView} containing the list of employees */
    @FXML private ListView<Employee> listView;

    /**
     * Constructor
     *
     * @param controller the controller of the view
     */
    public View (@NotNull Controller controller)
    {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/fxml/view.fxml"));
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
            System.err.println("Failed to load emulator's view...");
        }
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        datePicker.setValue(SimpleDate.TODAY.toLocalDate());

        prepareButtonEvents();
        preparePendingChecksLabel();
        prepareServerStateLabel();
    }

    /**
     * Prepare the events when the buttons are clicked
     */
    private void prepareButtonEvents ()
    {
        btnCheck.setOnAction(event -> controller.check());
        btnSync.setOnAction(event -> controller.sync());
        btnReset.setOnAction(event -> controller.resetTimeFields());
    }

    /**
     * Prepare the pendingChecksLabel {@link Label} content
     */
    private void preparePendingChecksLabel ()
    {
        labPendingChecks.textProperty().bind(controller.getClient().pendingChecksProperty().asString());
    }

    /**
     * Prepare the serverStateLabel {@link Label} content
     */
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

    /**
     * Get the layout of the view
     *
     * @return the layout of the view
     */
    public VBox getLayout ()
    {
        return layout;
    }

    /**
     * Get the {@link TextField} containing the number of hours
     *
     * @return the {@link TextField} containing the number of hours
     */
    public TextField getFieldHours ()
    {
        return fieldHours;
    }

    /**
     * Get the {@link TextField} containing the number of minutes
     *
     * @return the {@link TextField} containing the number of minutes
     */
    public TextField getFieldMinutes ()
    {
        return fieldMinutes;
    }

    /**
     * Get the label showing the hour of the check
     *
     * @return the label showing the hour of the check
     */
    public Label getLabHours ()
    {
        return labHours;
    }

    /**
     * Get the label showing the number of minutes of the check
     *
     * @return the label showing the number of minutes of the check
     */
    public Label getLabMinutes ()
    {
        return labMinutes;
    }

    /**
     * Get the {@link ListView} containing the list of employees
     *
     * @return the {@link ListView} containing the list of employees
     */
    public ListView<Employee> getListView ()
    {
        return listView;
    }

    /**
     * Get the datepicker
     *
     * @return the datepicker
     */
    public DatePicker getDatePicker ()
    {
        return datePicker;
    }
}
