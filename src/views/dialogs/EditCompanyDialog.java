package views.dialogs;

import controllers.CompanyController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.util.form.FieldValueTypes;
import lib.util.form.Form;
import lib.views.custom.components.Dialog;
import models.Company;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017.
 */
public class EditCompanyDialog extends Dialog implements Initializable
{
    private Company company;

    // FXML
    @FXML private Stage dialog;
    @FXML private TextField fieldName;
    @FXML private TextField fieldBossFirstName;
    @FXML private TextField fieldBossLastName;
    @FXML private TextField fieldManagementDepartmentName;
    @FXML private TextField fieldManagementDepartmentActivitySector;
    @FXML private Button btnSubmit;


    public EditCompanyDialog (Company company)
    {
        this.company = company;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dialogs/fxml/editCompany.fxml"));
        loader.setController(this);

        try
        {
            dialog = loader.load();
        }
        catch (IOException e)
        {
            dialog = new Stage();
            dialog.setTitle("Error");
            System.out.println("Failed to load company's edition's dialog...");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        setDialogShortcut(dialog);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.show();

        fieldName.setText(company.getName());
        fieldBossFirstName.setText(company.getBoss().getFirstName());
        fieldBossLastName.setText(company.getBoss().getLastName());
        fieldManagementDepartmentName.setText(company.getManagementDepartment().getName());
        fieldManagementDepartmentActivitySector.setText(company.getManagementDepartment().getActivitySector());

        Form form = new Form();
        form.add("companyName", FieldValueTypes.NAME, fieldName);
        form.add("bossFirstName", FieldValueTypes.FIRSTNAME, fieldBossFirstName);
        form.add("bossLastName", FieldValueTypes.LASTNAME, fieldBossLastName);
        form.add("managementDepartmentName", FieldValueTypes.NAME, fieldManagementDepartmentName);
        form.add("managementDepartmentActivitySector", FieldValueTypes.NAME, fieldManagementDepartmentActivitySector);

        btnSubmit.setOnAction(event -> new Thread(() -> Platform.runLater(() ->
        {
            if (new CompanyController().updateCompany(form))
            {
                dialog.close();
            }
        })).start());
    }
}
