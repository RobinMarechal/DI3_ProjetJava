package controllers;

import com.sun.istack.internal.NotNull;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Check;
import models.Employee;
import network.Client;
import views.View;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Robin on 26/05/2017. <br>
 * MVC's controller class
 */
public class Controller implements Serializable
{
    private static final long serialVersionUID = -4666892423167245858L;

    /** The MVC's view */
    private transient View view;
    /** The TCP client */
    private final Client client;
    /** The list of employees */
    private ArrayList<Employee> employees;

    /**
     * Create the object
     *
     * @param client    the TCP client
     * @param employees the list of employees
     */
    public Controller (@NotNull Client client, @NotNull ArrayList<Employee> employees)
    {
        this.client = client;
        this.employees = employees;
    }

    /**
     * Set the list of employees <br>
     * This method updates update the view by refreshing the {@link ListView}. <br>
     * The selected element is not lost.
     *
     * @param employees the new list of employees
     */
    public void setEmployees (@NotNull ArrayList<Employee> employees)
    {
        this.employees = employees;
        if (view != null)
        {
            ListView<Employee> listView = view.getListView();

            int      idSelected       = -1; // No employee selected
            Employee employeeToSelect = null;

            // We try to get the ID of the employee selected, if there is one
            try
            {
                idSelected = listView.getSelectionModel().getSelectedItem().getId();
            }
            catch (NullPointerException e)
            {
                // No employee selected, we don't need to do anything
            }

            // We reload the items of the listView
            listView.setItems(FXCollections.observableArrayList(employees));

            // We try to get the employee with the ID idSelected
            try
            {
                final int finalIdSelected = idSelected;
                employeeToSelect = listView.getItems()
                                           .stream()
                                           .filter(employee -> employee.getId() == finalIdSelected)
                                           .limit(1)
                                           .collect(Collectors.toList())
                                           .get(0);
            }
            catch (IndexOutOfBoundsException e)
            {
                // No employee selected, we don't need to do anything
            }

            // We select the employee selected, if there was one
            if (employeeToSelect != null)
            {
                listView.getSelectionModel().select(employeeToSelect);
            }
        }
    }

    /**
     * Display the view <br>
     * This method fills the {@link ListView} and add constraints to the time textfields
     *
     * @return The layout generated by the view
     */
    public Parent displayView ()
    {
        view = new View(this);
        if (employees != null)
        {
            view.getListView().setItems(FXCollections.observableArrayList(employees));
        }

        TextField fieldHours   = view.getFieldHours();
        TextField fieldMinutes = view.getFieldMinutes();

        prepareTextPropertyListener(fieldHours, 24);
        prepareTextPropertyListener(fieldMinutes, 60);

        resetTimeFields();

        return view.getLayout();
    }

    /**
     * Limit a {@link TextField} value to the numbers between
     *
     * @param field The field to constraint
     * @param limit The limit to not reach
     */
    private void prepareTextPropertyListener (TextField field, int limit)
    {
        field.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*"))
            {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
            else if (!newValue.isEmpty())
            {
                int v = Integer.parseInt(newValue);
                if (v >= limit)
                {
                    field.setText(oldValue);
                }

            }
            refreshLabsTime();
        });
    }

    /**
     * Refresh the time {@link javafx.scene.control.Label}s with the content of time {@link TextField}s
     */
    private void refreshLabsTime ()
    {
        String sHours   = view.getFieldHours().getText();
        String sMinutes = view.getFieldMinutes().getText();

        int hours   = sHours.isEmpty() ? 0 : Integer.parseInt(sHours);
        int minutes = sMinutes.isEmpty() ? 0 : Integer.parseInt(sMinutes);

        SimpleTime time = SimpleTime.of(hours, minutes);

        view.getLabHours().setText(time.getHour() + "");
        view.getLabMinutes().setText(time.getMinute() + "");
    }

    /**
     * Ask the client for the synchronisation of the list of employees with the server
     */
    public void sync ()
    {
        client.askForSync();
    }

    /**
     * Reset the time {@link TextField}s with the actual local time
     */
    public void resetTimeFields ()
    {
        LocalTime now = LocalTime.now();
        view.getFieldHours().setText(now.getHour() + "");
        view.getFieldMinutes().setText(now.getMinute() + "");
    }

    /**
     * Add a check to a queue that the client will send to the server as soon as possible <br>
     * This works even if the server is offline, the checks will be sent as soon as the server is online
     */
    public void check ()
    {
        Employee selectedItem = view.getListView().getSelectionModel().getSelectedItem();

        if (selectedItem != null)
        {
            int hours   = Integer.parseInt(view.getFieldHours().getText());
            int minutes = Integer.parseInt(view.getFieldMinutes().getText());

            SimpleDate     date = SimpleDate.fromLocalDate(view.getDatePicker().getValue());
            SimpleTime     time = SimpleTime.of(hours, minutes);
            SimpleDateTime sdt  = SimpleDateTime.fromDateAndTime(date, time);

            client.addToQueue(new Check(sdt, selectedItem.getId()));
        }

    }

    /**
     * Get the {@link Client} instance
     * @return the {@link Client} instance
     */
    public Client getClient ()
    {
        return client;
    }
}
