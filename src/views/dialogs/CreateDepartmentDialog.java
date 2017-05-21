package views.dialogs;

import controllers.DepartmentsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.views.custom.components.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class CreateDepartmentDialog extends Dialog implements Initializable
{
    @FXML private Stage dialog;
    @FXML private TextField fieldName;
    @FXML private TextField fieldActivitySector;
    @FXML private TextField fieldManagerFirstName;
    @FXML private TextField fieldManagerLastName;
    @FXML private Button btnSubmit;


    public CreateDepartmentDialog ()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/fxml/createDepartment.fxml"));
        loader.setController(this);

        try
        {
            dialog = loader.load();
        }
        catch (IOException e)
        {
            dialog = new Stage();
            dialog.setTitle("Error");
            System.out.println("Failed to load departments's creation's dialog...");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        setDialogCloseShortcut(dialog);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.show();

        btnSubmit.setOnAction(event ->
        {
            new DepartmentsController().createDepartment(fieldName, fieldActivitySector, fieldManagerFirstName, fieldManagerLastName);
        });
    }
}

