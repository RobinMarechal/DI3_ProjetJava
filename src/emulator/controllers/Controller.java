package emulator.controllers;

import emulator.models.Check;
import emulator.models.Employee;
import emulator.network.Client;
import emulator.views.View;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleDateTime;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.time.LocalTime;

/**
 * Created by Robin on 26/05/2017.
 */
public class Controller
{
    private View view;
    private final Client client;
    private ObservableList<Employee> employees;

    public Controller (Client client, ObservableList<Employee> employees)
    {
        this.client = client;
        this.employees = employees;
    }

    public void setEmployees (ObservableList<Employee> employees)
    {
        this.employees = employees;
        if (view != null)
        {
            view.getListView().setItems(employees);
        }
    }

    public Parent displayView ()
    {
        view = new View(this);

        view.getListView().setItems(employees);

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

        if(selectedItem != null)
        {
            int hours   = Integer.parseInt(view.getFieldHours().getText());
            int minutes = Integer.parseInt(view.getFieldMinutes().getText());

            SimpleDate     date = SimpleDate.fromLocalDate(view.getDatePicker().getValue());
            SimpleTime     time = SimpleTime.of(hours, minutes);
            SimpleDateTime sdt  = SimpleDateTime.fromDateAndTime(date, time);

            client.addToQueue(new Check(sdt, selectedItem.getId()));
        }

    }
}
