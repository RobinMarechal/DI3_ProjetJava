package views.dialogs;

import controllers.EmployeesController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.views.custom.components.Dialog;
import models.Employee;
import models.StandardDepartment;

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

    @FXML private Stage dialog;
    @FXML private TextField fieldFirstName;
    @FXML private TextField fieldLastName;
    @FXML private ComboBox comboDepartments;
    @FXML private Button btnSubmit;

    public EditEmployeeDialog (Employee employee, ObservableList<StandardDepartment> list)
    {
        this.employee = employee;
        this.departments = list;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/fxml/createEmployee.fxml"));
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
        setDialogCloseShortcut(dialog);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.show();

        comboDepartments.setItems(departments);

        fieldFirstName.setText(employee.getFirstName());
        fieldLastName.setText(employee.getLastName());
        comboDepartments.setPromptText(employee.getDepartment().getName());

        btnSubmit.setOnAction(event ->
        {
            if (new EmployeesController().updateEmployee(employee, fieldFirstName, fieldLastName, comboDepartments))
            {
                dialog.close();
            }
        });
    }
}

