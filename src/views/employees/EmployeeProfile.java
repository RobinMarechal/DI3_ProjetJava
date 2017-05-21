package views.employees;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
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
import lib.time.SimpleDate;
import lib.time.SimpleTime;
import lib.util.Closure;
import lib.views.CSSClasses;
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
    private final StringProperty prefixDep = new SimpleStringProperty(this, "prefixDep", "Department: ");
    private final StringProperty prefixId = new SimpleStringProperty(this, "prefixDep", "ID : ");
    private final StringProperty prefixStartsAt = new SimpleStringProperty(this, "prefixDep", "Starting hour: ");
    private final StringProperty prefixEndsAt = new SimpleStringProperty(this, "prefixDep", "Ending hour: ");
    private final StringProperty prefixOvertime = new SimpleStringProperty(this, "prefixDep", "Overtime: ");

    // UI components
    @FXML private Label labName;
    @FXML private Label labId;
    @FXML private Label labDep;
    @FXML private Label labStartsAt;
    @FXML private Label labEndsAt;
    @FXML private Label labAddMin;
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
        
        checks.addListener(new ListChangeListener<CheckInOut>()
        {
            @Override
            public void onChanged (Change<? extends CheckInOut> c)
            {
                System.out.println("Changed employee " + emp.getId());
            }
        });

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

        btnEdit.setOnAction(event -> new EmployeesController().openEditionEmployeeDialog(employee));
        btnFire.setOnAction(event -> new EmployeesController().fireEmployee(employee));
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
        final StringExpression nameBinding = Bindings.concat(employee.firstNameProperty(), " ", employee.lastNameProperty());

        labName.textProperty().bind(nameBinding);
        labId.textProperty().bind(Bindings.concat(prefixId, employee.idProperty()));
        labDep.setText(null);
        labDep.setGraphic(depLink);
        labStartsAt.textProperty().bind(Bindings.concat(prefixStartsAt, employee.startingHourProperty()));
        labEndsAt.textProperty().bind(Bindings.concat(prefixEndsAt, employee.endingHourProperty()));
        labAddMin.textProperty().bind(Bindings.concat(prefixOvertime, employee.overtimeProperty()));

        labAddMin.textProperty().addListener((observable, oldValue, newValue) ->
        {
            final double                 overtime   = employee.getOvertime();
            final ObservableList<String> styleClass = labAddMin.getStyleClass();

            // reset style class
            styleClass.removeAll(CSSClasses.Text.RED);

            // Set the style class based on the employee's overtime's value
            if (overtime < 0)
            {
                styleClass.add(CSSClasses.Text.RED);
//                styleClass.add(CSSClasses.Text.BOLD);
            }
        });
    }

}
