package views.dialogs;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lib.form.FieldTypes;
import lib.form.FieldValueTypes;
import lib.form.Form;
import lib.views.custom.components.Dialog;
import models.Employee;
import models.Manager;
import models.StandardDepartment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017. <br>
 * This class represents a dialog box allowing to update a standard department or to create one
 */
public class EditDepartmentDialog extends Dialog implements Initializable
{
    /** The department to update or null if it's a creation */
    private final StandardDepartment department;
    /** The list of all managers that don't manage any department */
    private final ObservableList<Manager> managers;
    /** The list of all employees without any department */
    private final ObservableList<Employee> employees;
    /** The title of the dialog box */
    private String title;
    /** The form */
    private Form form;

    /** The layout */
    @FXML private VBox root;
    /** The title dialog box label */
    @FXML private Label labTitle;
    /** The department's name input */
    @FXML private TextField fieldName;
    /** The department's activity sector input */
    @FXML private TextField fieldActivitySector;
    /** The combobox allowing to select the manager */
    @FXML private ComboBox<Employee> comboManagers;
    /** The submit button */
    @FXML private Button btnSubmit;

    /**
     * Constructor, load the fxml view file
     *
     * @param department the department to update, or null if it's a creation
     * @param managers   the list of all managers that don't manage any department
     * @param employees  the list of all employee's without any department
     */
    public EditDepartmentDialog (StandardDepartment department, ObservableList<Manager> managers, ObservableList<Employee>
            employees)
    {
        this.department = department;
        this.managers = managers;
        this.employees = employees;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/fxml/editDepartment.fxml"));
        loader.setController(this);

        if (department == null)
        {
            title = "Create a department";
        }
        else
        {
            title = "Update a department";
        }

        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            root = new VBox();
            setTitle("Error");
            if (department == null)
            {
                System.err.println("Failed to load departments's creation's dialog...");
            }
            else
            {
                System.err.println("Failed to load departments's edition's dialog...");
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

        setComboBoxItems(managers, employees);

        if (department != null)
        {
            Manager depManager = department.getManager();

            fieldName.setText(department.getName());
            fieldActivitySector.setText(department.getActivitySector());
            comboManagers.setPromptText(depManager.toString());
        }

        form = new Form();
        form.add("name", FieldValueTypes.NAME, fieldName);
        form.add("activitySector", FieldValueTypes.NAME, fieldActivitySector);
        form.add("manager", FieldValueTypes.UNDEFINED, comboManagers, FieldTypes.COMBOBOX);
    }

    /**
     * Get the {@link Form} containing all the inputs
     *
     * @return the {@link Form} containing all the inputs
     */
    public Form getForm ()
    {
        return form;
    }

    /**
     * Get the submit button object
     *
     * @return the submit button object
     */
    public Button getBtnSubmit ()
    {
        return btnSubmit;
    }

    /**
     * Get manager combobox object
     *
     * @return manager combobox object
     */
    public ComboBox<Employee> getMangeerComboBox ()
    {
        return comboManagers;
    }

    /**
     * Set the manager combobox items
     *
     * @param managersList  the list of the managers that don't manage any department
     * @param employeesList the list of all the employees without any department
     */
    public void setComboBoxItems (ObservableList<Manager> managersList, ObservableList<Employee> employeesList)
    {
        employeesList.removeAll(managersList);
        ObservableList<Employee> items = comboManagers.getItems();
        items.clear();
        items.addAll(managersList);
        items.addAll(employeesList);
    }
}

