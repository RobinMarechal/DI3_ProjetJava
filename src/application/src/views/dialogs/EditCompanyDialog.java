package views.dialogs;

import controllers.CompanyController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lib.form.FieldValueTypes;
import lib.form.Form;
import lib.views.custom.components.Dialog;
import models.Company;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 21/05/2017. <br>
 * This class represents a dialog box allowing to update the company's information
 */
public class EditCompanyDialog extends Dialog implements Initializable
{
    /** The company */
    private Company company;

    /** The layout */
    @FXML private VBox root;
    /** The company's name input */
    @FXML private TextField fieldName;
    /** The boss firstname name input */
    @FXML private TextField fieldBossFirstName;
    /** The boss lastname input */
    @FXML private TextField fieldBossLastName;
    /** The management department's name input */
    @FXML private TextField fieldManagementDepartmentName;
    /** The  management department's activity sector input */
    @FXML private TextField fieldManagementDepartmentActivitySector;
    /** The submit button */
    @FXML private Button btnSubmit;

    /**
     * Constructor, load the fxml view file
     *
     * @param company the company
     */
    public EditCompanyDialog (@NotNull Company company)
    {
        this.company = company;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/dialogs/fxml/editCompany.fxml"));
        loader.setController(this);

        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            root = new VBox();
            setTitle("Error");
            System.err.println("Failed to load company's edition's dialog...");
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

        btnSubmit.setOnAction(event -> Platform.runLater(() ->
        {
            if (new CompanyController().updateCompany(form))
            {
                close();
            }
        }));
    }
}
