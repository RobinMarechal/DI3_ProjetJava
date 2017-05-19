package views.employees;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Robin on 09/05/2017.
 */
public class EmployeeList extends EmployeesViewController implements Initializable
{
    private final SimpleDate date;
    private ObservableList<Row> obs;

    // components
    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, IntegerProperty> columnId;
    @FXML private TableColumn<Row, StringProperty> columnFirstName;
    @FXML private TableColumn<Row, StringProperty> columnLastName;
    @FXML private TableColumn<Row, StringProperty> columnDepartment;
    @FXML private TableColumn<Row, Boolean> columnManager;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnStartingHour;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnEndingHour;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnArrivedAt;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnLeftAt;

    // constants
    private final String classEmployeeCell = "employees-table-cell";
    private final String classStartingHour = "starting-hour";
    private final String classEndingHour = "ending-hour";
    private final String classLeft = "left";
    private final String classArrived = "arrived";
    private final String classNotArrivedYet = "not-arrived-yet";
    private final String classNotLeftYet = "not-left-yet";
    private final String rowTooltipText = "Clickez ici pour voir le profil de l'employé.";

    public EmployeeList (ObservableList<Employee> employees, SimpleDate date)
    {
        this.date = date;

        employees.addListener(new ListChangeListener<Employee>()
        {
            @Override
            public void onChanged (Change<? extends Employee> c)
            {
                while (c.next())
                {
                    List l = c.getAddedSubList();
                    obs.add(new Row((Employee) l.get(0)));
                }
            }
        });

        obs = FXCollections.observableArrayList();

        for (Employee e : employees)
        {
            obs.add(new Row(e));
        }

        // constant
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/employees/fxml/list.fxml"));
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
        initCellFactories();
        display();

        // Initialization of cell factories in order to color cell's content based on its value

        table.setOnSort(event ->
        {
            initCellFactories();
            display();
        });

        table.setRowFactory(e ->
        {
            TableRow<Row> row = new TableRow<>();

            // Adding the tooltip to the row
            row.setTooltip(new Tooltip(rowTooltipText));

            // On row click ==> see employee's profile
            row.setOnMouseClicked(event ->
            {
                Row cRow = row.getItem();
                if(cRow != null)
                {
                    Employee emp = cRow.getEmployeeInstance();
                    new EmployeesController().show(emp);
                }

            });
            return row;
        });
    }

    public Pane getPane ()
    {
        return pane;
    }

    private void display ()
    {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        columnManager.setCellValueFactory(new PropertyValueFactory<>("manager"));
        columnStartingHour.setCellValueFactory(new PropertyValueFactory<>("startingHour"));
        columnEndingHour.setCellValueFactory(new PropertyValueFactory<>("endingHour"));
        columnArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        columnLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));

        table.setItems(obs);
    }

    private void initCellFactories ()
    {
        prepareClickEvents();

        prepareColumnManagerCellFactory();
        prepareColumnArrivedAtCellFactory();
        prepareColumnLeftAtCellFactory();
    }

    private void prepareColumnLeftAtCellFactory ()
    {
        // leftAt colum : red if left earlier, green if left on time (or later)
        columnLeftAt.setCellFactory(column -> new TableCell<Row, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                Row r = (Row) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() == null) // Not left yet
                    {
                        //                        textProperty().setValue("...");
                    }
                    else // Left
                    {
                        textProperty().bind(item.asString());
                    }
                    //                    updateStyle(r.getEmployeeInstance(), item, empty);
                    //                    textProperty().addListener((observable, oldValue, newValue) -> updateStyle(r.getEmployeeInstance(), item, empty));
                }
            }

            //            private void updateStyle (Employee emp, ObjectProperty<SimpleTime> item, boolean empty)
            //            {
            //                if (emp == null || item.getValue() == null || empty)
            //                {
            //                    return;
            //                }
            //
            //                ObservableList<String> styleClass = getStyleClass();
            //                styleClass.removeAll(classLeft, classNotLeftYet, CSSClasses.Text.RED, CSSClasses.Text.GREEN);
            //
            //                styleClass.addAll(classEmployeeCell, classEndingHour);
            //
            //                SimpleTime leavTime = emp.getLeavingTimeAt(date);
            //
            //                // Text and arrived/not arrived style class
            //                if (leavTime == null) // Not left yet
            //                {
            //                    styleClass.add(classNotLeftYet);
            //                    styleClass.add(CSSClasses.Text.GREEN);
            //                    //                    textProperty().setValue("...");
            //                }
            //                else // Left
            //                {
            //                    //                    textProperty().bind(item.asString());
            //                    styleClass.add(classLeft);
            //
            //                    try
            //                    {
            //                        if (emp.verifyCheckOutTimeAt(date) == -1) // if he left too early => problem
            //                        {
            //                            styleClass.add(CSSClasses.Text.RED);
            //                        }
            //                        else // => Ok
            //                        {
            //                            styleClass.add(CSSClasses.Text.GREEN);
            //                        }
            //                    }
            //                    catch (ModelException e)
            //                    {
            //                        e.print();
            //                    }
            //                }
            //            }
        });
    }

    private void prepareColumnArrivedAtCellFactory ()
    {
        // arrivedAt column : red if arrived late, green if arrived on time (or earlier)
        columnArrivedAt.setCellFactory(column -> new TableCell<Row, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                Row r = (Row) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() == null) // Not arrived yet
                    {
                        //                        textProperty().setValue("...");
                    }
                    else // Arrived
                    {
                        textProperty().bind(item.asString());
                    }

                    //                    updateStyle(r.getEmployeeInstance(), item, empty);
                    //                    textProperty().addListener((observable, oldValue, newValue) -> updateStyle(r.getEmployeeInstance(), item, empty));
                }
            }

            //            private void updateStyle (Employee emp, ObjectProperty<SimpleTime> item, boolean empty)
            //            {
            //                if (emp == null || item.getValue() == null || empty)
            //                {
            //                    return;
            //                }
            //
            //                ObservableList<String> styleClass = getStyleClass();
            //                styleClass.removeAll(classArrived, classNotArrivedYet, CSSClasses.Text.RED, CSSClasses.Text.GREEN);
            //
            //                styleClass.addAll(classEmployeeCell, classStartingHour);
            //
            //                SimpleTime arrTime = emp.getArrivingTimeAt(date);
            //
            //                // Text and arrived/not arrived style class
            //                if (arrTime == null)
            //                {
            //                    styleClass.add(classNotArrivedYet);
            //                }
            //                else
            //                {
            //                    styleClass.add(classArrived);
            //                }
            //
            //                // late or on time class style
            //                try
            //                {
            //                    if (emp.verifyCheckInTimeAt(date) == -1) // if he arrived late => problem
            //                    {
            //                        styleClass.add(CSSClasses.Text.RED);
            //                    }
            //                    else // => Ok
            //                    {
            //                        styleClass.add(CSSClasses.Text.GREEN);
            //                    }
            //                }
            //                catch (ModelException e)
            //                {
            //                    e.print();
            //                }
            //            }
        });
    }

    private void prepareColumnManagerCellFactory ()
    {
        // managerColumn : blue if manager
        //        columnManager.setCellFactory(column -> new TableCell<Row, Boolean>()
        //        {
        //            @Override
        //            protected void updateItem (Boolean item, boolean empty)
        //            {
        //                super.updateItem(item, empty);
        //
        //                String styleClass = "";
        //
        //                if (item != null && !empty)
        //                {
        //                    boolean isManager = item.booleanValue();
        //                    if (isManager)
        //                    {
        //                        styleClass = "text-blue";
        //                    }
        //
        //                    setText(isManager ? "Oui" : "Non");
        //
        //                    getStyleClass().addAll(classEmployeeCell, "manager", styleClass);
        //                }
        //            }
        //        });
    }

    private void prepareClickEvents ()
    {
        columnFirstName.setCellFactory(column -> new TableCell<Row, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item);
                    setTooltip(new Tooltip("Clickez pour voir le profil de l'employé."));
                }
            }
        });

        columnLastName.setCellFactory(column -> new TableCell<Row, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item);
                    setTooltip(new Tooltip("Clickez pour voir le profil de l'employé."));
                }
            }
        });

        columnId.setCellFactory(column -> new TableCell<Row, IntegerProperty>()
        {
            @Override
            protected void updateItem (IntegerProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item.asString());
                    setTooltip(new Tooltip("Clickez pour voir le profil de l'employé."));
                }
            }
        });

        columnDepartment.setCellFactory(column -> new TableCell<Row, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    //                    textProperty().bind(((SimpleObjectProperty<StandardDepartment>) item.property()).as);
                    textProperty().bind(item);
                    setTooltip(new Tooltip("Clickez pour voir le détail du département."));
                }
            }
        });
    }

    private void setUpLinkCell (TableCell<Row, Link> tableCell, Link item)
    {
        tableCell.setOnMouseClicked(event ->
        {
            item.trigger();
        });

        tableCell.getStyleClass().add("mouse-hand");
        tableCell.getStyleClass().add("link");
    }

    // Inner class that represents an employee with all its relative information
    // (like department name, if he's manager, checks...)
    // Needs to stay public because of reflection
    public class Row
    {
        private Employee employeeInstance;
        private StandardDepartment departmentInstance;

        private IntegerProperty id;
        private StringProperty firstName;
        private StringProperty lastName;
        private StringProperty department;
        private boolean manager = false;
        private ObjectProperty<SimpleTime> startingHour;
        private ObjectProperty<SimpleTime> endingHour;
        private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");
        private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");

        private Row (Employee e)
        {
            employeeInstance = e;
            departmentInstance = e.getDepartment();

            int empId = e.getId();

            Closure employeeClosure = () -> new EmployeesController().show(employeeInstance);
            Closure depClosure      = () -> new DepartmentsController().show(departmentInstance);

            id = e.idProperty();
            firstName = e.firstNameProperty();
            lastName = e.lastNameProperty();

            if (e.departmentProperty().getValue() == null) // The employee was just created and has no department yet...
            {
                // We create an empty tmp StringProperty
                department = new SimpleStringProperty(this, "department", "");

                // When we set set employee's department, we override the tmp StringProperty with the good one
                e.departmentProperty()
                 .addListener((observable, oldValue, newValue) -> {
                     final StandardDepartment value = e.departmentProperty().getValue();
                     if(value != null)
                        department = value.nameProperty();
                 });
            }
            else // The employee has a department, easy
            {
                department = e.departmentProperty().getValue().nameProperty();
            }


            if (departmentInstance != null)
            {
                manager = departmentInstance.getManager().getId() == empId ? true : false;
            }

            startingHour = e.startingHourProperty();
            endingHour = e.endingHourProperty();

            arrivedAt.addListener((observable, oldValue, newValue) -> System.out.println("In (" + empId + ") : from '" + oldValue + "' to '" + newValue + "'"));
            leftAt.addListener((observable, oldValue, newValue) -> System.out.println("Out (" + empId + ") : from '" + oldValue + "' to '" + newValue + "'"));

            if (e.arrivingTimePropertyAt(date) != null)
            {
                arrivedAt.bind(e.arrivingTimePropertyAt(date));
            }

            if (e.leavingTimePropertyAt(date) != null)
            {
                leftAt.bind(e.leavingTimePropertyAt(date));
            }

            employeeInstance.getChecksInOut().addListener(new ListChangeListener<CheckInOut>()
            {
                @Override
                public void onChanged (Change<? extends CheckInOut> change)
                {
                    while (change.next())
                    {
                        if (change.wasAdded())
                        {
                            CheckInOut added = change.getAddedSubList().get(0);
                            if (added != null && added.getDate() != null && added.getDate().equals(date))
                            {
                                if (added.getLeftAt() != null)
                                {
                                    leftAt.setValue(added.getLeftAt());
                                }
                                else if (added.getArrivedAt() != null)
                                {
                                    arrivedAt.setValue(added.getArrivedAt());
                                }

                                int index = table.getItems().indexOf(Row.this);
                                table.getItems().set(index, Row.this);

                            }
                        }
                    }
                }
            });
        }

        public IntegerProperty getId ()
        {
            return id;
        }

        public StringProperty getFirstName ()
        {
            return firstName;
        }

        public StringProperty getLastName ()
        {
            return lastName;
        }

        public StringProperty getDepartment ()
        {
            return department;
        }

        public boolean isManager ()
        {
            return manager;
        }

        public SimpleTime getStartingHour ()
        {
            return startingHour.getValue();
        }

        public ObjectProperty<SimpleTime> startingHourProperty ()
        {
            return startingHour;
        }

        public SimpleTime getEndingHour ()
        {
            return endingHour.getValue();
        }

        public ObjectProperty<SimpleTime> getArrivedAt ()
        {
            return arrivedAt;
        }

        public ObjectProperty<SimpleTime> getLeftAt ()
        {
            return leftAt;
        }

        public Employee getEmployeeInstance ()
        {
            return employeeInstance;
        }

        public StandardDepartment getDepartmentInstance ()
        {
            return departmentInstance;
        }
    }

}
