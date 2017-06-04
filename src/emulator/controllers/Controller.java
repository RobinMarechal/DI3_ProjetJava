package emulator.controllers;

import emulator.models.Check;
import emulator.models.Employee;
import emulator.network.Client;
import emulator.views.View;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Robin on 26/05/2017.
 */
public class Controller implements Serializable
{
    private static final long serialVersionUID = -4666892423167245858L;

    private transient View view;
    private final Client client;
    private ArrayList<Employee> employees;

    public Controller (Client client, ArrayList<Employee> employees)
    {
        this.client = client;
        this.employees = employees;
    }

    public void setEmployees (ArrayList<Employee> employees)
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
            if(employeeToSelect != null)
            {
                listView.getSelectionModel().select(employeeToSelect);
            }
        }
    }

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

    public void sync ()
    {
        client.askForSync();
    }

    public void resetTimeFields ()
    {
        LocalTime now = LocalTime.now();
        view.getFieldHours().setText(now.getHour() + "");
        view.getFieldMinutes().setText(now.getMinute() + "");
    }

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

    public void printList ()
    {
        if (employees != null)
        {
            employees.forEach(System.out::println);
        }
        else
        {
            System.out.println("employees list null");
        }
    }

    public Client getClient ()
    {
        return client;
    }
}
