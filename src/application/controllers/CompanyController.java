package application.controllers;

import application.lib.BaseController;
import application.lib.util.form.Form;
import application.lib.views.Tabs;
import application.lib.views.Template;
import application.models.Boss;
import application.models.Company;
import application.models.ManagementDepartment;
import application.views.CompanyViewController;
import application.views.company.HomeCompany;
import application.views.dialogs.EditCompanyDialog;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Robin on 26/04/2017.
 */
public class CompanyController extends BaseController implements Observer
{
    public CompanyController ()
    {
        Company.getCompany();
    }

    @Override
    public void home ()
    {
        CompanyViewController view = new HomeCompany(Company.getCompany(), SimpleDate.TODAY);

        Template.getInstance().setView(Tabs.COMPANY, view);
    }

    @Override
    public void update (Observable o, Object arg)
    {
        System.out.println("Company's home view updated...");
        home();
    }

    public void openEditCompanyDialog ()
    {
        new EditCompanyDialog(Company.getCompany());
    }

    public boolean updateCompany (Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return false;
        }
        else
        {
            String companyName   = ((TextField) form.get("companyName").getField()).getText();
            String bossFirstName = ((TextField) form.get("bossFirstName").getField()).getText();
            String bossLastName  = ((TextField) form.get("bossLastName").getField()).getText();
            String manDepName    = ((TextField) form.get("managementDepartmentName").getField()).getText();
            String manDepSector  = ((TextField) form.get("managementDepartmentActivitySector").getField()).getText();

            Company              company              = Company.getCompany();
            Boss                 boss                 = company.getBoss();
            ManagementDepartment managementDepartment = company.getManagementDepartment();

            company.setName(companyName);
            boss.setFirstName(bossFirstName);
            boss.setLastName(bossLastName);
            managementDepartment.setName(manDepName);
            managementDepartment.setActivitySector((manDepSector));

            return true;
        }
    }

    public void openImportEmployeesDialog ()
    {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());

        new Thread(() -> Platform.runLater(() ->
        {
            try
            {
                Company.getCompany().loadEmployeesFromCSVFile(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Your file has been imported succesfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failure");
                alert.setContentText("An error occurred, your file might have not be imported entirely.");
                alert.show();
                e.printStackTrace();
            }
        })).start();
    }

    public void openImportDepartmentsDialog ()
    {
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(new Stage());

        new Thread(() -> Platform.runLater(() ->
        {
            try
            {
                Company.getCompany().loadDepartmentsFromCSVFile(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Your file has been imported succesfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failure");
                alert.setContentText("An error occurred, your file might have not be imported entirely.");
                alert.show();
                e.printStackTrace();
            }
        })).start();
    }

    public void exportEmployees ()
    {
        new Thread(() -> Platform.runLater(() ->
        {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            fc.setInitialFileName("employees.csv");
            File dest = fc.showSaveDialog(new Stage());

            Company.getCompany().saveEmployeesToCSV(dest);
        })).start();
    }

    public void exportDepartments ()
    {
        new Thread(() -> Platform.runLater(() ->
        {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File("."));
            fc.setInitialFileName("departments.csv");
            File dest = fc.showSaveDialog(new Stage());

            Company.getCompany().saveDepartmentsToCSV(dest);
        })).start();
    }
}
