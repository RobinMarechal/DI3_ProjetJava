package views.employees;

import controllers.DepartmentsController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lib.time.SimpleDate;
import lib.time.SimpleTime;
import lib.util.Closure;
import lib.views.Template;
import lib.views.custom.components.Link;
import models.CheckInOut;
import models.Employee;
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

    // Constants
    private final StringProperty prefixDep = new SimpleStringProperty(this, "prefixDep", "Département : ");
    private final StringProperty prefixId = new SimpleStringProperty(this, "prefixDep", "ID : ");
    private final StringProperty prefixStartsAt = new SimpleStringProperty(this, "prefixDep", "Embauche à : ");
    private final StringProperty prefixEndsAt = new SimpleStringProperty(this, "prefixDep", "Débauche à : ");
    private final StringProperty prefixOvertime = new SimpleStringProperty(this, "prefixDep", "Heures supplémentaires : ");

    // UI components
    @FXML private Label labFirstName;
    @FXML private Label labLastName;
    @FXML private Label labId;
    @FXML private Label labDep;
    @FXML private Label labStartsAt;
    @FXML private Label labEndsAt;
    @FXML private Label labAddMin;
    @FXML private TableView<CheckInOut> table;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleDate>> colDate;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colArrivedAt;
    @FXML private TableColumn<CheckInOut, ObjectProperty<SimpleTime>> colLeftAt;

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

        // Initialization of cell factories in order to color cell's content based on its value

        table.setOnSort(event ->
        {
            try
            {
                new Thread(() ->
                {
                    initCellValueFactories();
                    displayRightComponents();
                }).start();
            }
            catch (IllegalStateException e)
            {
                System.out.println("IllegalStateException has been thrown, Couldn't sort the table...");
            }
        });
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
        StandardDepartment dep     = employee.getDepartment();
        Closure            closure = () -> new DepartmentsController().show(dep);


        Link depLink = new Link(closure);
        depLink.textProperty().bind(Bindings.concat(prefixDep, dep.nameProperty()));
        depLink.setTooltipValue("Clickez pour voir le détail du département.");

        labFirstName.textProperty().bind(employee.firstNameProperty());

        labLastName.textProperty().bind(employee.lastNameProperty());
        labId.textProperty().bind(Bindings.concat(prefixId, employee.idProperty()));
        labDep.setText(null);
        labDep.setGraphic(depLink);
        labStartsAt.textProperty().bind(Bindings.concat(prefixStartsAt, employee.startingHourProperty()));
        labEndsAt.textProperty().bind(Bindings.concat(prefixEndsAt, employee.endingHourProperty()));
        labAddMin.textProperty().bind(Bindings.concat(prefixOvertime, employee.getOvertime()));
    }

}
