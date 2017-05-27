package application.views.dialogs;

import application.controllers.DepartmentsController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.lib.util.form.FieldTypes;
import application.lib.util.form.FieldValueTypes;
import application.lib.util.form.Form;
import application.lib.views.custom.components.Dialog;
import application.models.Employee;
import application.models.Manager;
import application.models.StandardDepartment;

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
    @FXML private Stage dialog;
    @FXML private Label labTitle;
    @FXML private TextField fieldName;
    @FXML private TextField fieldActivitySector;
    @FXML private ComboBox<Employee> comboManagers;
    @FXML private Button btnSubmit;

    private String title = "Edition of the department";


    public EditDepartmentDialog (StandardDepartment department, ObservableList<Manager> managers, ObservableList<Employee> employees)
    {
        this.department = department;
        this.managers = managers;
        this.employees = employees;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/dialogs/fxml/createDepartment.fxml"));
        loader.setController(this);

        try
        {
            dialog = loader.load();
        }
        catch (IOException e)
        {
            dialog = new Stage();
            dialog.setTitle("Error");
            System.out.println("Failed to load departments's edition's dialog...");
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

        comboManagers.getItems().addAll(managers);
        comboManagers.getItems().addAll(employees);

        Manager depManager = department.getManager();

        fieldName.setText(department.getName());
        fieldActivitySector.setText(department.getActivitySector());
        comboManagers.setPromptText(depManager.toString());

        Form form = new Form();
        form.add("name", FieldValueTypes.NAME, fieldName);
        form.add("activitySector", FieldValueTypes.NAME, fieldActivitySector);
        form.add("manager", FieldValueTypes.UNDEFINED, comboManagers, FieldTypes.COMBOBOX);

        btnSubmit.setOnAction(event -> new Thread(() -> Platform.runLater(() ->
        {
            if (new DepartmentsController().updateDepartment(department, form))
            {
                dialog.close();
            }
        })).start());
    }
}

