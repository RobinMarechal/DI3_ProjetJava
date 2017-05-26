package views.company;

import controllers.CompanyController;
import controllers.DepartmentsController;
import controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private SimpleDate date;

    // Constants
    /** progressBar animation duration in millisecond */
    private final int animationDuration = 300;

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

    public HomeCompany (Company company, SimpleDate date)
    {
        this.date = date;
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
        display(company);
    }

    public Pane getPane ()
    {
        return pane;
    }

    private void display (Company company)
    {

        // constants
        final String nbEmployeesPrefix      = "Employees: ";
        final String nbDepartmentsPrefix    = "Departments: ";

        // Properties... Have fun
        final StringProperty   companyName            = company.nameProperty();
        final StringProperty   bossFirstName          = company.getBoss().firstNameProperty();
        final StringProperty   bossLastName           = company.getBoss().lastNameProperty();
        final StringExpression bossName               = Bindings.concat(bossFirstName, " ", bossLastName);
        final IntegerBinding   nbEmployeesInt         = Bindings.size(company.getEmployeesList());
        final StringExpression nbEmployees            = Bindings.concat(nbEmployeesPrefix, nbEmployeesInt);
        final StringExpression nbDepartments          = Bindings.concat(nbDepartmentsPrefix, Bindings.size(company.getStandardDepartmentsList()));
        final DoubleBinding    nbChecksIn             = Bindings.doubleValueAt(company.getTotalChecksInPerDay(), date);
        final DoubleBinding    nbChecksOut            = Bindings.doubleValueAt(company.getTotalChecksOutPerDay(), date);
        final StringExpression checksInProgressTitle  = Bindings.concat("Today's checks in: (", IntegerBinding.integerExpression(nbChecksIn), "/", nbEmployeesInt, ")");
        final StringExpression checksOutProgressTitle = Bindings.concat("Today's checks out : (", IntegerBinding.integerExpression(nbChecksOut), "/", nbEmployeesInt, ")");

        // middle labels
        labelCompany.textProperty().bind(companyName);
        labelBossName.textProperty().bind(bossName);
        labelNbEmployees.textProperty().bind(nbEmployees);
        labelNbDepartments.textProperty().bind(nbDepartments);


        // Buttons
        btnAddEmployee.setOnAction(event -> new EmployeesController().openCreationEmployeeDialog());
        btnAddDepartment.setOnAction(event -> new DepartmentsController().openCreationDepartmentDialog());
        btnEditCompany.setOnAction(event -> new CompanyController().openEditCompanyDialog());

        // Progress bars
        progressChecksIn.progressProperty().bind(Bindings.divide(nbChecksIn, nbEmployeesInt));
        progressChecksOut.progressProperty().bind(Bindings.divide(nbChecksOut, nbEmployeesInt));
        labelChecksInProgress.textProperty().bind(checksInProgressTitle);
        labelChecksOutProgress.textProperty().bind(checksOutProgressTitle);
    }
}
