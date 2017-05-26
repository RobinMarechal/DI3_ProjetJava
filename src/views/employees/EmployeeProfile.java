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
import views.EmployeesViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 13/05/2017.
 */
public class EmployeeProfile extends EmployeesViewController implements Initializable
{
    /** employee to display */
    private final Employee employee;
    private ObservableList<CheckInOut> checks;

    private String testV = "abc";

    // UI components
    @FXML private Label labFirstName;
    @FXML private Label labLastName;
    @FXML private Label labId;
    @FXML private Label labDep;
    @FXML private Label labStartsAt;
    @FXML private Label labEndsAt;
    @FXML private Label labOvertime;
    @FXML private Label labOvertimeSuff;
    @FXML private Label labIsManager;
    @FXML private TableView<CheckInOut> table;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleDate>> colDate;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colArrivedAt;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colLeftAt;
    @FXML private Button btnEdit;
    @FXML private Button btnFire;

    public EmployeeProfile (Employee emp)
    {
        super();

        this.employee = emp;
        checks = emp.getChecksInOut();

        // constant
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/employees/fxml/profile.fxml"));
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
            System.out.println("Failed to load employees' list view...");
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        display();

        final EmployeesController employeesController = new EmployeesController();

        btnEdit.setOnAction(event -> employeesController.openEditionEmployeeDialog(employee));
        btnFire.setOnAction(event -> employeesController.fireEmployee(employee));
    }

    private void display ()
    {
        displayLeftComponents();
        displayRightComponents();
    }

    private void displayRightComponents ()
    {
        initCellValueFactories();

        table.setItems(checks);
    }

    private void initCellValueFactories ()
    {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        colLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));
    }

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
//                styleClass.add(CSSClasses.Text.BOLD);
            }
        });
    }

}
