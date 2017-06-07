package views.employees;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lib.util.Closure;
import lib.views.CSSClasses;
import lib.views.Template;
import lib.views.custom.components.Link;
import models.CheckInOut;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import org.jetbrains.annotations.NotNull;
import views.EmployeesViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 13/05/2017.
 * This class injects the asked data into the components generated with fxml <br>
 * to show the profile of an employee
 */
public class EmployeeProfile extends EmployeesViewController implements Initializable
{
    /** The employee to display */
    private final Employee employee;
    /** The list of checks */
    private ObservableList<CheckInOut> checks;

    /** The {@link javafx.scene.control.Label} containing the firstname of the employee */
    @FXML private Label labFirstName;
    /** The {@link javafx.scene.control.Label} containing the lastname of the employee */
    @FXML private Label labLastName;
    /** The {@link javafx.scene.control.Label} containing the ID of the employee */
    @FXML private Label labId;
    /** The {@link javafx.scene.control.Label} containing the department's name of the employee */
    @FXML private Label labDep;
    /** The {@link javafx.scene.control.Label} containing the starting hour of the employee */
    @FXML private Label labStartsAt;
    /** The {@link javafx.scene.control.Label} containing the ending hour of the employee */
    @FXML private Label labEndsAt;
    /** The {@link javafx.scene.control.Label} containing the current overtime of the employee */
    @FXML private Label labOvertime;
    /** The {@link javafx.scene.control.Label} containing the firstname of the employee */
    @FXML private Label labOvertimeSuff;
    /** The {@link javafx.scene.control.Label} containing the overtime's value suffix of the employee */
    @FXML private Label labIsManager;
    /** The table containing the list of {@link CheckInOut} */
    @FXML private TableView<CheckInOut> table;
    /** The Check's date column */
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleDate>> colDate;
    /** The Check's check in time column */
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colArrivedAt;
    /** The Check's check out time column */
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colLeftAt;
    /** The button used to edit the employee's information */
    @FXML private Button btnEdit;
    /** The button used to fire an employee from the company */
    @FXML private Button btnFire;


    /**
     * Constructor <br>
     * Load the fxml view and launch initialize method
     *
     * @param employee the employee to display
     */
    public EmployeeProfile (@NotNull Employee employee)
    {
        super();

        this.employee = employee;
        checks = employee.getChecksInOut();

        // constant
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/employees/fxml/profile.fxml"));
        loader.setController(this);

        try
        {
            pane = loader.load();
            pane.setPrefHeight(Template.STAGE_HEIGHT);
            pane.setPrefWidth(Template.CENTER_WIDTH);
        }
        catch (IOException e)
        {
            pane = new BorderPane();
            System.err.println("Failed to load employees' list view...");
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

        final EmployeesController employeesController = new EmployeesController();

        btnEdit.setOnAction(event -> employeesController.openEditionEmployeeDialog(employee));
        btnFire.setOnAction(event -> employeesController.fireEmployee(employee));
    }

    /**
     * Display the left and right components
     */
    private void display ()
    {
        displayLeftComponents();
        displayRightComponents();
    }

    /**
     * Display the components placed on the right
     */
    private void displayRightComponents ()
    {
        initCellValueFactories();

        table.setItems(checks);
    }

    /**
     * Initialize the table's rows cell value factories
     */
    private void initCellValueFactories ()
    {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        colLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));
    }

    /**
     * Displays the components on the left
     */
    private void displayLeftComponents ()
    {
        ObjectProperty<StandardDepartment> dep            = employee.departmentProperty();
        Closure                            closure        = () -> new DepartmentsController().show(dep.getValue(), SimpleDate.TODAY);
        final ObservableList<String>       styleClass     = labOvertime.getStyleClass();
        final ObservableList<String>       styleClassSuff = labOvertimeSuff.getStyleClass();


        Link depLink = new Link(closure);
        depLink.textProperty().bind(dep.asString());
        depLink.setTooltipValue("Click to see the department profile.");

        labFirstName.textProperty().bind(employee.firstNameProperty());
        labLastName.textProperty().bind(employee.lastNameProperty());
        labId.textProperty().bind(employee.idProperty().asString());
        labDep.setText(null);
        labDep.setGraphic(depLink);
        labStartsAt.textProperty().bind(employee.startingHourProperty().asString());
        labEndsAt.textProperty().bind(employee.endingHourProperty().asString());
        labOvertime.textProperty().bind(employee.overtimeProperty().asString());
        labIsManager.setText(employee instanceof Manager ? "yes" : "no");

        if(employee.getOvertime() < 0)
        {
            styleClass.add(CSSClasses.Text.RED);
            styleClassSuff.add(CSSClasses.Text.RED);
        }

        labOvertime.textProperty().addListener((observable, oldValue, newValue) ->
        {
            final double                 overtime   = employee.getOvertime();

            // reset style class
            styleClass.removeAll(CSSClasses.Text.RED);
            styleClassSuff.removeAll(CSSClasses.Text.RED);

            // Set the style class based on the employee's overtime's value
            if (overtime < 0)
            {
                styleClass.add(CSSClasses.Text.RED);
                styleClassSuff.add(CSSClasses.Text.RED);
            }
        });
    }

}
