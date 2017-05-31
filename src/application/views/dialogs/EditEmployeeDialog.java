package application.views.dialogs;

import application.controllers.EmployeesController;
import application.lib.util.form.FieldTypes;
import application.lib.util.form.FieldValueTypes;
import application.lib.util.form.Form;
import application.lib.views.custom.components.Dialog;
import application.models.Employee;
import application.models.Manager;
import application.models.StandardDepartment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class EditEmployeeDialog extends Dialog implements Initializable
{
    private Employee employee;
    private ObservableList<StandardDepartment> departments;

    private final String title = "Update the employee";

    @FXML private Stage dialog;
    @FXML private Label labTitle;
    @FXML private TextField fieldFirstName;
    @FXML private TextField fieldLastName;
    @FXML private ComboBox comboDepartments;
    @FXML private Button btnSubmit;
    @FXML private TextField startingHourHour;
    @FXML private TextField startingHourMin;
    @FXML private TextField endingHourHour;
    @FXML private TextField endingHourMin;
    @FXML private CheckBox cbManager;

    public EditEmployeeDialog (Employee employee, ObservableList<StandardDepartment> list)
    {
        this.employee = employee;
        this.departments = list;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/dialogs/fxml/createEmployee.fxml"));
        loader.setController(this);

        try
        {
            dialog = loader.load();
        }
        catch (IOException e)
        {
            dialog = new Stage();
            dialog.setTitle("Error");
            System.out.println("Failed to load employee's edition dialog...");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        dialog.setTitle(title);
        labTitle.setText(title);
        setDialogShortcut(dialog);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.show();

        display();
        prepareClickEvent();
        formatTimeFieldsInput(startingHourHour, 24);
        formatTimeFieldsInput(endingHourHour, 24);
        formatTimeFieldsInput(startingHourMin, 60);
        formatTimeFieldsInput(endingHourMin, 60);

        //        comboDepartments.setItems(departments);
        //
        //        fieldFirstName.setText(employee.getFirstName());
        //        fieldLastName.setText(employee.getLastName());
        //        startingHourHour.setText(employee.getStartingHour().getHour() + "");
        //        startingHourMin.setText(employee.getStartingHour().getMinute() + "");
        //        endingHourHour.setText(employee.getEndingHour().getHour() + "");
        //        endingHourMin.setText(employee.getEndingHour().getMinute() + "");
        //        cbManager.setSelected(employee instanceof Manager);


        //        Form form = new Form();
        //        form.add("firstName", FieldValueTypes.FIRSTNAME, fieldFirstName);
        //        form.add("lastName", FieldValueTypes.LASTNAME, fieldLastName);
        //        form.add("startingHourHour", FieldValueTypes.HOURS, startingHourHour);
        //        form.add("startingHourMinutes", FieldValueTypes.MINUTES, startingHourMin);
        //        form.add("endingHourHour", FieldValueTypes.HOURS, endingHourHour);
        //        form.add("endingHourMinutes", FieldValueTypes.MINUTES, endingHourMin);
        //        form.add("department", FieldValueTypes.UNDEFINED, comboDepartments, FieldTypes.COMBOBOX);
        //        form.add("cbManager", FieldValueTypes.UNDEFINED, cbManager, FieldTypes.CHECKBOX);

    }

    private void formatTimeFieldsInput (TextField field, int limit)
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
        });
    }

    private void prepareClickEvent ()
    {
        Form form = new Form();
        form.add("firstName", FieldValueTypes.FIRSTNAME, fieldFirstName);
        form.add("lastName", FieldValueTypes.LASTNAME, fieldLastName);
        form.add("startingHourHour", FieldValueTypes.HOURS, startingHourHour);
        form.add("startingHourMinutes", FieldValueTypes.MINUTES, startingHourMin);
        form.add("endingHourHour", FieldValueTypes.HOURS, endingHourHour);
        form.add("endingHourMinutes", FieldValueTypes.MINUTES, endingHourMin);
        form.add("department", FieldValueTypes.UNDEFINED, comboDepartments, FieldTypes.COMBOBOX);
        form.add("cbManager", FieldValueTypes.UNDEFINED, cbManager, FieldTypes.CHECKBOX);

        btnSubmit.setOnAction(event -> new Thread(() -> Platform.runLater(() ->
        {
            if (new EmployeesController().updateEmployee(employee, form))
            {
                dialog.close();
            }
        })).start());
    }

    private void display ()
    {
        comboDepartments.setItems(departments);
        if (employee.getDepartment() != null)
        {
            comboDepartments.getSelectionModel().select(employee.getDepartment());
        }

        fieldFirstName.setText(employee.getFirstName());
        fieldLastName.setText(employee.getLastName());
        startingHourHour.setText(employee.getStartingHour().getHour() + "");
        startingHourMin.setText(employee.getStartingHour().getMinute() + "");
        endingHourHour.setText(employee.getEndingHour().getHour() + "");
        endingHourMin.setText(employee.getEndingHour().getMinute() + "");
        cbManager.setSelected(employee instanceof Manager);
    }
}

