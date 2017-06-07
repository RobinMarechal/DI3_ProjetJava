package views.dialogs;

import lib.form.FieldTypes;
import lib.form.FieldValueTypes;
import lib.form.Form;
import lib.views.custom.components.Dialog;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.<br>
 * This class represents a dialog box allowing to update an employee or to create one
 */
public class EditEmployeeDialog extends Dialog implements Initializable
{
    /** The employee to update or null if it's a creation */
    private Employee employee;
    /** The list of all standard departments */
    private ObservableList<StandardDepartment> departments;
    /** The title of the dialog box */
    private String title;
    /** The form */
    private Form form;

    /** The layout */
    @FXML private HBox root;
    /** The title dialog box label */
    @FXML private Label labTitle;
    /** The employee's firstname input */
    @FXML private TextField fieldFirstName;
    /** The employee's lastname input */
    @FXML private TextField fieldLastName;
    /** The combobox allowing to select the department the employee is working in */
    @FXML private ComboBox comboDepartments;
    /** The employee's starting hour hours input */
    @FXML private TextField startingHourHour;
    /** The employee's starting hour minutes input */
    @FXML private TextField startingHourMin;
    /** The employee's ending hour hours input */
    @FXML private TextField endingHourHour;
    /** The employee's ending hour minutes input */
    @FXML private TextField endingHourMin;
    /** The check box to check if the employee must be a manager */
    @FXML private CheckBox cbManager;
    /** The submit button */
    @FXML private Button btnSubmit;

    /**
     * Constructor, load the fxml view file
     *
     * @param employee    the employee to update, or null if it's a creation
     * @param departments the list of all standard departments
     */
    public EditEmployeeDialog (Employee employee, ObservableList<StandardDepartment> departments)
    {
        this.employee = employee;
        this.departments = departments;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/dialogs/fxml/editEmployee.fxml"));
        loader.setController(this);

        if (employee == null)
        {
            title = "Create en employee";
        }
        else
        {
            title = "Update an employee";
        }

        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            root = new HBox();
            setTitle("Error");
            if (employee == null)
            {
                System.err.println("Failed to load employee's creation dialog...");
            }
            else
            {
                System.err.println("Failed to load employee's edition dialog...");
            }
            e.printStackTrace();
        }
        finally
        {
            setContent(root);
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
        setTitle(title);
        labTitle.setText(title);

        display();
        buildFormObject();
        //        prepareClickEvent();
        formatTimeFieldsInput(startingHourHour, 24);
        formatTimeFieldsInput(endingHourHour, 24);
        formatTimeFieldsInput(startingHourMin, 60);
        formatTimeFieldsInput(endingHourMin, 60);
    }

    /**
     * Build the form object
     */
    private void buildFormObject ()
    {
        form = new Form();
        form.add("firstName", FieldValueTypes.FIRSTNAME, fieldFirstName);
        form.add("lastName", FieldValueTypes.LASTNAME, fieldLastName);
        form.add("startingHourHour", FieldValueTypes.HOURS, startingHourHour);
        form.add("startingHourMinutes", FieldValueTypes.MINUTES, startingHourMin);
        form.add("endingHourHour", FieldValueTypes.HOURS, endingHourHour);
        form.add("endingHourMinutes", FieldValueTypes.MINUTES, endingHourMin);
        form.add("department", FieldValueTypes.UNDEFINED, comboDepartments, FieldTypes.COMBOBOX);
        form.add("cbManager", FieldValueTypes.UNDEFINED, cbManager, FieldTypes.CHECKBOX);
    }

    /**
     * Add constraints to content of the time fields: <br>
     * - An hours fields value should remain greater or equals than 0 and lower than 24 <br>
     * - A minutes fields value should remain greater or equals than 0 and lower than 60 <br>
     *
     * @param field The field to compel
     * @param limit the value that should not be reached
     */
    private void formatTimeFieldsInput (@NotNull TextField field, int limit)
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

    /**
     * Display the dialog box content <br>
     * Fill the inputs with the employee's information if it's an update of an existing one
     */
    private void display ()
    {
        comboDepartments.setItems(departments);

        if (employee == null)
        {
            startingHourHour.setText(Employee.DEFAULT_STARTING_HOUR.getHour() + "");
            startingHourMin.setText(Employee.DEFAULT_STARTING_HOUR.getMinute() + "");
            endingHourHour.setText(Employee.DEFAULT_ENDING_HOUR.getHour() + "");
            endingHourMin.setText(Employee.DEFAULT_ENDING_HOUR.getMinute() + "");
        }
        else
        {
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

    /**
     * Get the submit button
     * @return the submit button
     */
    public Button getBtnSubmit ()
    {
        return btnSubmit;
    }

    /**
     * Get the form object
     * @return the form object
     */
    public Form getForm ()
    {
        return form;
    }
}

