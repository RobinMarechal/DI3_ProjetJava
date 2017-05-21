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
import models.StandardDepartment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class CreateEmployeeDialog extends Dialog implements Initializable
{
    private final ObservableList<StandardDepartment> departments;

    // FXML
    @FXML private Stage dialog;
    @FXML private TextField fieldFirstName;
    @FXML private TextField fieldLastName;
    @FXML private ComboBox comboDepartments;
    @FXML private Button btnSubmit;


    public CreateEmployeeDialog (ObservableList<StandardDepartment> departments)
    {
        this.departments = departments;

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
            System.out.println("Failed to load employee's creation dialog...");
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

        btnSubmit.setOnAction(event -> {
            new EmployeesController().createEmploye(fieldFirstName, fieldLastName, comboDepartments);
        });
    }
}
