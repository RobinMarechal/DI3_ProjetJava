package application.views.dialogs;

import application.lib.form.FieldTypes;
import application.lib.form.FieldValueTypes;
import application.lib.form.Form;
import application.lib.views.custom.components.Dialog;
import application.models.Employee;
import application.models.Manager;
import application.models.StandardDepartment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class EditDepartmentDialog extends Dialog implements Initializable
{
    private final StandardDepartment department;
    private final ObservableList<Manager> managers;
    private final ObservableList<Employee> employees;

    // components
    @FXML private VBox root;
    @FXML private Label labTitle;
    @FXML private TextField fieldName;
    @FXML private TextField fieldActivitySector;
    @FXML private ComboBox<Employee> comboManagers;
    @FXML private Button btnSubmit;

    private String title;
    private Form form;


    public EditDepartmentDialog (StandardDepartment department, ObservableList<Manager> managers, ObservableList<Employee> employees)
    {
        this.department = department;
        this.managers = managers;
        this.employees = employees;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/dialogs/fxml/createDepartment.fxml"));
        loader.setController(this);

        if(department == null)
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
                System.out.println("Failed to load departments's creation's dialog...");
            }
            else
            {
                System.out.println("Failed to load departments's edition's dialog...");
            }
            e.printStackTrace();
        }
        finally
        {
            setContent(root);
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        setTitle(title);
        labTitle.setText(title);

        setComboBoxItems(managers, employees);

        if(department != null)
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

    public Form getForm ()
    {
        return form;
    }

    public Button getBtnSubmit ()
    {
        return btnSubmit;
    }

    public ComboBox<Employee> getMangeerComboBox ()
    {
        return comboManagers;
    }

    public void setComboBoxItems (ObservableList<Manager> managersList, ObservableList<Employee> employeesList)
    {
        employeesList.removeAll(managersList);
        ObservableList<Employee> items = comboManagers.getItems();
        items.clear();
        items.addAll(managersList);
        items.addAll(employeesList);
    }
}

