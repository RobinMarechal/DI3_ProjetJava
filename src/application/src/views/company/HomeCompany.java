package views.company;

import controllers.CompanyController;
import controllers.DepartmentsController;
import controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import lib.views.Template;
import models.Company;
import org.jetbrains.annotations.NotNull;
import views.CompanyViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 09/05/2017.<br>
 * Display the home view of the application <br>
 * The view contains informations at a specific date, like the number of checks in/out
 */
public class HomeCompany extends CompanyViewController implements Initializable
{
    /** The company */
    private Company company;
    /** The date */
    private SimpleDate date;

    // components
    /** The label that containts the name of the company */
    @FXML private Label labCompany;
    /** The label that containts the name of the boss */
    @FXML private Label labBossFirstName;
    /** The label that containts the name of the boss */
    @FXML private Label labBossLastName;
    /** The label that containts the number of employees (including managers) in the company */
    @FXML private Label labNbEmployees;
    /** The label that containts the name of standard departments in the company */
    @FXML private Label labNbDepartments;
    /** The button that open a dialog box containg a form to create an employee (or a manager) */
    @FXML private Button btnAddEmployee;
    /** The button that open a dialog box containg a form to create a standard department */
    @FXML private Button btnAddDepartment;
    /** The button that open a dialog box containg a form to modify company's information */
    @FXML private Button btnEditCompany;
    /** The progress bar that shows the proportion of checks in in function of the number of employees */
    @FXML private ProgressBar progressChecksIn;
    /** The progress bar that shows the proportion of checks out in function of the number of employees */
    @FXML private ProgressBar progressChecksOut;
    /** The label containing the number of checks in this day */
    @FXML private Label labNbChecksIn;
    /** The label containing the number of checks out this day */
    @FXML private Label labNbChecksOut;
    /** The label placed next to labNbChecksIn that show the total number of employees */
    @FXML private Label labNbEmployeesChecksIn;
    /** The label placed next to labNbChecksOut that show the total number of employees */
    @FXML private Label labNbEmployeesChecksOut;

    /**
     * Constructor <br>
     * Load the fxml view, or an empty one in case of loading failure
     *
     * @param company the company to display
     * @param date    the date of checks
     */
    public HomeCompany (@NotNull Company company, @NotNull SimpleDate date)
    {
        this.date = date;
        this.company = company;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/company/fxml/home.fxml"));
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
            e.printStackTrace();
            System.err.println("Failed to load company's home view...");
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
        display();
    }

    /**
     * Display the data inside of the UI components
     */
    private void display ()
    {
        int nbEmployeesInt = company.getNbEmployees();

        // Properties...
        final StringProperty companyName   = company.nameProperty();
        final StringProperty bossFirstName = company.getBoss().firstNameProperty();
        final StringProperty bossLastName  = company.getBoss().lastNameProperty();
        final IntegerBinding nbEmployees   = Bindings.size(company.getEmployeesList());
        final IntegerBinding nbDepartments = Bindings.size(company.getStandardDepartmentsList());
        final IntegerBinding nbChecksIn    = Bindings.integerValueAt(company.getTotalChecksInPerDay(), date);
        final IntegerBinding nbChecksOut   = Bindings.integerValueAt(company.getTotalChecksOutPerDay(), date);

        // middle labels
        labCompany.textProperty().bind(companyName);
        labBossFirstName.textProperty().bind(bossFirstName);
        labBossLastName.textProperty().bind(bossLastName);
        labNbEmployees.textProperty().bind(nbEmployees.asString());
        labNbDepartments.textProperty().bind(nbDepartments.asString());


        // Progress bars
        // Checks in
        try
        {
            progressChecksIn.setProgress((double) nbChecksIn.getValue() / nbEmployeesInt);
        }
        catch (Exception e)
        {
            progressChecksIn.setProgress(0);
        }
        labNbChecksIn.textProperty().bind(nbChecksIn.asString());
        labNbEmployeesChecksIn.textProperty().bind(nbEmployees.asString());

        // Checks out
        try
        {
            progressChecksOut.setProgress((double) nbChecksOut.getValue() / nbEmployeesInt);
        }
        catch (ArithmeticException e)
        {
            progressChecksOut.setProgress(0);
        }
        labNbChecksOut.textProperty().bind(nbChecksOut.asString());
        labNbEmployeesChecksOut.textProperty().bind(nbEmployees.asString());


        // Buttons
        btnAddEmployee.setOnAction(event -> new EmployeesController().openCreationEmployeeDialog());
        btnAddDepartment.setOnAction(event -> new DepartmentsController().openCreationDepartmentDialog());
        btnEditCompany.setOnAction(event -> new CompanyController().openEditCompanyDialog());


        // Progress updates
        nbEmployees.addListener((observable, oldValue, newValue) -> {
            long value = newValue.longValue();
            if(value == 0)
            {
                progressChecksIn.setProgress(0);
                progressChecksOut.setProgress(0);
            }
            else
            {
                progressChecksIn.setProgress((double) nbChecksIn.getValue() / value);
                progressChecksOut.setProgress((double) nbChecksOut.getValue() / value);
            }
        });

        nbChecksIn.addListener((observable, oldValue, newValue) -> {
            if(nbEmployees.getValue() != 0)
            {
                progressChecksIn.setProgress((double) nbChecksIn.getValue() / nbEmployees.getValue());
            }
        });

        nbChecksOut.addListener((observable, oldValue, newValue) -> {
            if(nbEmployees.getValue() != 0)
            {
                progressChecksOut.setProgress((double) nbChecksOut.getValue() / nbEmployees.getValue());
            }
        });
    }
}
