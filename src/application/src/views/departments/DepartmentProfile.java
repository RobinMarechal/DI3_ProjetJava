package views.departments;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lib.util.Closure;
import lib.views.Template;
import lib.views.custom.components.Link;
import models.CheckInOut;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import org.jetbrains.annotations.NotNull;
import views.DepartmentsViewController;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Robin on 22/05/2017. <br>
 * This class injects the asked data into the components generated with fxml <br>
 * to show the profile of a standard department
 */
public class DepartmentProfile extends DepartmentsViewController implements Initializable
{
    /** The department to display */
    private final StandardDepartment department;
    /** The date of employee's checks */
    private final SimpleDate date;
    /** The items of the Table object */
    private ObservableList<EmployeeRow> rows;
    /** The tooltip text that is displayed when the mouse is over a table's row */
    private final String rowTooltipText = "Click here to see the employee's profile.";

    /** The label containing the name of the department */
    @FXML private Label labName;
    /** The label containing the ID of the department */
    @FXML private Label labId;
    /** The label containing the activity sector of the department */
    @FXML private Label labSector;
    /** The label containing the name of the department's manager */
    @FXML private Label labManager;
    /** The label containing the the number of the department's employees */
    @FXML private Label labNbEmployees;
    /** The list of the department's employees */
    @FXML private TableView<EmployeeRow> table;
    /** The employee's ID column */
    @FXML private TableColumn<EmployeeRow, IntegerProperty> colId;
    /** The employee's firstname column */
    @FXML private TableColumn<EmployeeRow, StringProperty> colFirstName;
    /** The employee's lastname column */
    @FXML private TableColumn<EmployeeRow, StringProperty> colLastName;
    /** The employee's check in time column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> colArrivedAt;
    /** The employee's check out time column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> colLeftAt;
    /** See the department's list of checks at the previous date */
    @FXML private Button btnPrevDate;
    /** See the department's list of checks at the next date */
    @FXML private Button btnNextDate;
    /** See the department's list of checks at another date */
    @FXML private DatePicker datePicker;
    /** Open the employees management dialog box */
    @FXML private Button btnManageEmployees;
    /** Open the department's edition dialog box */
    @FXML private Button btnEdit;
    /** Open the departments deletion dialog box */
    @FXML private Button btnRemove;


    /**
     * Constructor <br>
     * Load the fxml view and launch initialize method
     *
     * @param department the department to display
     * @param date       the date of the employee's checks
     */
    public DepartmentProfile (@NotNull StandardDepartment department, @NotNull SimpleDate date)
    {
        this.department = department;
        this.date = date;

        rows = FXCollections.observableArrayList();

        rows.addAll(department.getEmployeesList().stream().map(EmployeeRow::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/departments/fxml/show.fxml"));
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
            e.printStackTrace();
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

        department.getEmployeesList().addListener(new ListChangeListener<Employee>()
        {
            @Override
            public void onChanged (Change<? extends Employee> change)
            {
                while (change.next())
                {
                    if (change.wasAdded())
                    {
                        List list = change.getAddedSubList();
                        for (Object o : list)
                        {
                            rows.add(new EmployeeRow((Employee) o));
                        }
                    }
                    else if (change.wasRemoved())
                    {
                        List list = change.getRemoved();
                        for (Object o : list)
                        {
                            final int id = ((Employee) o).getId();
                            rows.removeIf(row -> row.getId().getValue() == id);
                        }
                    }
                    table.refresh();
                    table.sort();
                }
            }
        });

        final DepartmentsController departmentsController = new DepartmentsController();

        btnEdit.setOnAction(event -> departmentsController.openEditionDepartmentDialog(department));
        btnRemove.setOnAction(event -> departmentsController.removeDepartment(department));
        btnManageEmployees.setOnAction(event -> departmentsController.openManageEmployeesDialog(department));
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
        prepareRowEvent();
        initCellValueFactories();
        initCellFactories();
        initDatePickerBar();

        table.setItems(rows);
        table.setSortPolicy(param ->
        {
            Comparator<EmployeeRow> comparator = (r1, r2) -> r1.getId().getValue().compareTo(r2.getId().getValue());

            FXCollections.sort(table.getItems(), comparator);
            return true;
        });
    }

    /**
     * Prepare the events on the table's rows
     */
    private void prepareRowEvent ()
    {

        table.setRowFactory(e ->
        {
            TableRow<EmployeeRow> row = new TableRow<>();

            // Adding the tooltip to the row
            row.setTooltip(new Tooltip(rowTooltipText));
            row.getStyleClass().add("mouse-hand");

            // On row click ==> see employee's profile
            row.setOnMouseClicked(event ->
            {
                EmployeeRow cRow = row.getItem();
                if (cRow != null)
                {
                    Employee emp = cRow.getEmployee();
                    new EmployeesController().show(emp);
                }

            });
            return row;
        });
    }

    /**
     * Initialize the datepicker, the buttons around and their events
     */
    private void initDatePickerBar ()
    {
        btnNextDate.setOnAction(event -> new DepartmentsController().show(department, date.plusDays(1)));
        btnPrevDate.setOnAction(event -> new DepartmentsController().show(department, date.minusDays(1)));
        datePicker.setOnAction(event -> new DepartmentsController().show(department, SimpleDate.fromLocalDate(datePicker.getValue())));
        datePicker.setShowWeekNumbers(false);
        datePicker.setPromptText(date.toString());
    }


    /**
     * Initialize the table's rows cell value factories
     */
    private void initCellValueFactories ()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        colLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));
    }


    /**
     * Initialize the table's rows cell factories
     */
    private void initCellFactories ()
    {
        colId.setCellFactory(param -> new TableCell<EmployeeRow, IntegerProperty>()
        {
            @Override
            protected void updateItem (IntegerProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item.asString());
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });


        colFirstName.setCellFactory(param -> new TableCell<EmployeeRow, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item);
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });


        colLastName.setCellFactory(param -> new TableCell<EmployeeRow, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item);
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });

        colArrivedAt.setCellFactory(param -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                if (!empty && item != null && item.getValue() != null)
                {
                    textProperty().bind(item.asString());
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });

        colLeftAt.setCellFactory(param -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                if (!empty && item != null && item.getValue() != null)
                {
                    textProperty().bind(item.asString());
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });
    }

    /**
     * Displays the components on the left
     */
    private void displayLeftComponents ()
    {
        StandardDepartment standarDep = department;

        Manager manager = standarDep.getManager();
        Closure closure = () -> new EmployeesController().show(standarDep.getManager());
        Link    link    = new Link(closure);

        link.textProperty().bind(Bindings.concat(manager.firstNameProperty(), " ", manager.lastNameProperty()));

        labName.textProperty().bind(department.nameProperty());
        labId.textProperty().bind(standarDep.idProperty().asString());
        labSector.textProperty().bind(department.activitySectorProperty());
        labManager.setText(null);
        labManager.setGraphic(link);
        labNbEmployees.textProperty().bind(Bindings.size(department.getEmployees()).asString());
    }

    /**
     * Inner class representing a row of the TableView <br>
     * This class must remain public because the TableColumn's cell value factories
     * uses it's getters <br>
     * An inner class is used here because 2 objects are bound to the Table. <br>
     * This class allows me to regroup these two classes into a single one
     */
    public class EmployeeRow
    {
        /** The employee concerned */
        private Employee employee;

        /** The employee's ID */
        private IntegerProperty id;
        /** The employee's firstname */
        private StringProperty firstName;
        /** The employee's lastname */
        private StringProperty lastName;
        /** The check in time value, if there is one */
        private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");
        /** The check out time value, if there is one */
        private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");

        /**
         * Constructor <br>
         * Prepare the properties value
         *
         * @param employee the concerned employee
         */
        public EmployeeRow (Employee employee)
        {
            this.employee = employee;

            id = employee.idProperty();
            firstName = employee.firstNameProperty();
            lastName = employee.lastNameProperty();

            // arriving and leaving hour properties
            if (employee.arrivingTimePropertyAt(date) != null)
            {
                arrivedAt.setValue(employee.getArrivingTimeAt(date));
            }
            if (employee.leavingTimePropertyAt(date) != null)
            {
                leftAt.setValue(employee.getLeavingTimeAt(date));
            }

            // If the CheckInOut list changes, typically when an employee checks in or out...
            employee.getChecksInOut().addListener((ListChangeListener<CheckInOut>) change ->
            {
                while (change.next())
                {
                    if (change.wasAdded()) // If the employee checks in or out
                    {
                        // In case we add more than 1 check...
                        List<CheckInOut> added = (List<CheckInOut>) change.getAddedSubList();

                        for (CheckInOut addedItem : added)
                        {
                            // If the addedItem is not null, it's date isn't null and corresponding to the view's date parameter
                            if (addedItem != null && addedItem.getDate() != null && addedItem.getDate().equals(date))
                            {
                                // If what we added was a checkOut
                                if (addedItem.getLeftAt() != null)
                                {
                                    leftAt.setValue(addedItem.getLeftAt());
                                }
                                // Otherwise, it was maybe a checkIn
                                else if (addedItem.getArrivedAt() != null)
                                {
                                    arrivedAt.setValue(addedItem.getArrivedAt());
                                }

                                // We retrieve the concerned TableView row
                                int index = table.getItems().indexOf(EmployeeRow.this);
                                // And we refresh it
                                table.getItems().set(index, EmployeeRow.this);
                            }
                        }
                    }
                }
            });
        }

        /**
         * Get the concerned employee
         *
         * @return the concerned employee
         */
        public Employee getEmployee ()
        {
            return employee;
        }

        /**
         * Get the concerned employee's ID property
         *
         * @return the concerned employee's ID property
         */
        public IntegerProperty getId ()
        {
            return id;
        }

        /**
         * Get the concerned employee's firstname property
         *
         * @return the concerned employee's firstname property
         */
        public StringProperty getFirstName ()
        {
            return firstName;
        }

        /**
         * Get the concerned employee's lastname property
         *
         * @return the concerned employee's lastname property
         */
        public StringProperty getLastName ()
        {
            return lastName;
        }

        /**
         * Get the concerned employee's check in time property
         *
         * @return the concerned employee's check in time property
         */
        public ObjectProperty<SimpleTime> getArrivedAt ()
        {
            return arrivedAt;
        }

        /**
         * Get the concerned employee's check out time property
         *
         * @return the concerned employee's check out time property
         */
        public ObjectProperty<SimpleTime> getLeftAt ()
        {
            return leftAt;
        }
    }
}