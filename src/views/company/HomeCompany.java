package views.company;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lib.time.SimpleDate;
import lib.views.Template;
import models.Company;
import views.CompanyViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 09/05/2017.
 */
public class HomeCompany extends CompanyViewController implements Initializable
{
    private Company company;

    // components
    @FXML private Label labelCompany;
    @FXML private Label labelBossName;
    @FXML private Label labelNbEmployees;
    @FXML private Label labelNbDepartments;
    @FXML private Button btnAddEmployee;
    @FXML private Button btnAddDepartment;
    @FXML private Button btnEditCompany;
    @FXML private ProgressBar progressChecksIn;
    @FXML private ProgressBar progressChecksOut;
    @FXML private Label labelChecksInProgress;
    @FXML private Label labelChecksOutProgress;

    public HomeCompany (Company company)
    {
        this.company = company;

        // constant
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/company/fxml/home.fxml"));
        loader.setController(this);

        try
        {
            pane = loader.load();
            pane.setPrefHeight(Template.STAGE_HEIGHT);
            pane.setPrefWidth(Template.CENTER_WIDTH);
        }
        catch (IOException e)
        {
            pane = new VBox();
            System.out.println("Failed to load company's home view...");
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        assert labelCompany != null;
        assert labelBossName != null;
        assert labelNbEmployees != null;
        assert labelNbDepartments != null;
        assert btnAddEmployee != null;
        assert btnAddDepartment != null;
        assert btnEditCompany != null;
        assert progressChecksIn != null;
        assert progressChecksOut != null;
        assert labelChecksInProgress != null;
        assert labelChecksOutProgress != null;

        display(company);
    }

    public Pane getPane ()
    {
        return pane;
    }

    private void display (Company company)
    {

        // constants

        final String nbEmployeesPrefix      = "Employés : ";
        final String nbDepartmentsPrefix    = "Départements : ";
        final String addEmployeeBtnString   = "Ajouter un employé";
        final String addDepartmentBtnString = "Ajouter un départment";
        final String editCompanyBtnString   = "Modifier la compagnie";

        // Properties
        final String   companyName            = company.getName();
        final String   bossFirstName          = company.getBoss().getFirstName();
        final String   bossLastName           = company.getBoss().getLastName();
        final int   nbEmployees            = company.getNbEmployees();
        final int   nbDepartments          = company.getNbStandardDepartments();
        final int nbChecksIn  = company.getTotalChecksInAt(SimpleDate.TODAY);
        final int nbChecksOut = company.getTotalChecksOutAt(SimpleDate.TODAY);
        final String checksInProgressTitle  = "Checks-in du jour : (" + nbChecksIn + "/" + nbEmployees + ")";
        final String checksOutProgressTitle = "Checks-out du jour : ("+ nbChecksOut+ "/"+ nbEmployees + ")";

        // middle labels
        labelCompany.setText(companyName);
        labelBossName.setText(bossFirstName+ " "+ bossLastName);
        labelNbEmployees.setText(nbEmployeesPrefix + nbEmployees);
        labelNbDepartments.setText(nbDepartmentsPrefix + nbDepartments);

        // Buttons
        btnAddEmployee.setText(addEmployeeBtnString);
        btnAddDepartment.setText(addDepartmentBtnString);
        btnEditCompany.setText(editCompanyBtnString);

        // Progress bars
        progressChecksIn.setProgress((double) nbChecksIn / nbEmployees);
        progressChecksOut.setProgress((double) nbChecksOut / nbEmployees);
        labelChecksInProgress.setText(checksInProgressTitle);
        labelChecksOutProgress.setText(checksOutProgressTitle);

        // refresh proggress bar on : checkin, checkout ou employee added/fired.
//        nbChecksInProp.addListener((observable, oldValue, newValue) -> {
//            progressChecksIn.setProgress(newValue.doubleValue() / nbEmployees.getValue());
//        });
//
//        nbChecksOutProp.addListener((observable, oldValue, newValue) -> {
//            progressChecksOut.setProgress(newValue.doubleValue() / nbEmployees.getValue());
//        });
//
//        nbEmployees.addListener((observable, oldValue, newValue) -> {
//            progressChecksIn.setProgress( progressChecksIn.getProgress() / newValue.doubleValue());
//            progressChecksOut.setProgress( progressChecksOut.getProgress() / newValue.doubleValue());
//        });
    }
}
