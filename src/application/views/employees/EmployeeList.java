package application.views.employees;

import application.controllers.EmployeesController;
import application.lib.views.Template;
import application.models.CheckInOut;
import application.models.Employee;
import application.models.StandardDepartment;
import application.views.EmployeesViewController;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleTime;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Robin on 09/05/2017.
 */
public class EmployeeList extends EmployeesViewController implements Initializable
{
    private final SimpleDate date;
    private ObservableList<Row> rows;

    // components
    @FXML private Button btnPrevDate;
    @FXML private Button btnNextDate;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, IntegerProperty> columnId;
    @FXML private TableColumn<Row, StringProperty> columnFirstName;
    @FXML private TableColumn<Row, StringProperty> columnLastName;
    @FXML private TableColumn<Row, ObjectProperty<StandardDepartment>> columnDepartment;
    @FXML private TableColumn<Row, Boolean> columnManager;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnStartingHour;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnEndingHour;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnArrivedAt;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> columnLeftAt;
    @FXML private TableColumn<Row, DoubleProperty> columnOvertime;

    private final String rowTooltipText = "Click here to see the employee's profile.";

    public EmployeeList (ObservableList<Employee> employees, SimpleDate date)
    {
        this.date = date;

        employees.addListener(new ListChangeListener<Employee>()
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
                            rows.add(new Row((Employee) o));
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
                }
            }
        });

        rows = FXCollections.observableArrayList();

        rows.addAll(employees.stream().map(Row::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/employees/fxml/list.fxml"));
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

        table.setRowFactory(e ->
        {
            TableRow<Row> row = new TableRow<>();

            // Adding the tooltip to the row
            row.setTooltip(new Tooltip(rowTooltipText));

            // On row click ==> see employee's profile
            row.setOnMouseClicked(event ->
            {
                Row cRow = row.getItem();
                if (cRow != null)
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
        btnPrevDate.setOnAction(event -> new EmployeesController().listAt(date.minusDays(1)));
        btnNextDate.setOnAction(event -> new EmployeesController().listAt(date.plusDays(1)));
        datePicker.setOnAction(event -> new EmployeesController().listAt(SimpleDate.fromLocalDate(datePicker.getValue())));
        datePicker.setShowWeekNumbers(false);
        datePicker.setPromptText(date.toString());

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        columnManager.setCellValueFactory(new PropertyValueFactory<>("manager"));
        columnStartingHour.setCellValueFactory(new PropertyValueFactory<>("startingHour"));
        columnEndingHour.setCellValueFactory(new PropertyValueFactory<>("endingHour"));
        columnArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        columnLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));
        columnOvertime.setCellValueFactory(new PropertyValueFactory<>("overtime"));

        table.setItems(rows);
    }

    private void initCellFactories ()
    {
        prepareCellFactories();

        prepareColumnArrivedAtCellFactory();
        prepareColumnLeftAtCellFactory();
    }

    private void prepareColumnLeftAtCellFactory ()
    {
        columnLeftAt.setCellFactory(column -> new TableCell<Row, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                Row r = (Row) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
            }

            //            private void updateStyle (Employee emp, ObjectProperty<SimpleTime> item, boolean empty)
            //            {
            //                if (emp == null || item.getRegexp() == null || empty)
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
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
            }

            //            private void updateStyle (Employee emp, ObjectProperty<SimpleTime> item, boolean empty)
            //            {
            //                if (emp == null || item.getRegexp() == null || empty)
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

    private void prepareCellFactories ()
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
                }
            }
        });

        columnDepartment.setCellFactory(column -> new TableCell<Row, ObjectProperty<StandardDepartment>>()
        {
            @Override
            protected void updateItem (ObjectProperty<StandardDepartment> item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item.asString());
                }
            }
        });

        columnOvertime.setCellFactory(column -> new TableCell<Row, DoubleProperty>()
        {
            @Override
            protected void updateItem (DoubleProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item.asString());
                }
            }
        });
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
        private ObjectProperty<StandardDepartment> department;
        private boolean manager = false;
        private ObjectProperty<SimpleTime> startingHour;
        private ObjectProperty<SimpleTime> endingHour;
        private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");
        private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");
        private DoubleProperty overtime;

        private Row (Employee employee)
        {
            employeeInstance = employee;
            departmentInstance = employee.getDepartment();
            int empId = employee.getId();

            id = employee.idProperty();
            firstName = employee.firstNameProperty();
            lastName = employee.lastNameProperty();

            department = employee.departmentProperty();

            // We don't only want to know if he is a manager,
            // we also want to know if he is THE manager of the department
            if (departmentInstance != null && departmentInstance.getManager() != null)
            {
                manager = departmentInstance.getManager().getId() == empId ? true : false;
            }

            // Starting and ending hour properties
            startingHour = employee.startingHourProperty();
            endingHour = employee.endingHourProperty();

            // overtime property
            overtime = employee.overtimeProperty();

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
            employeeInstance.getChecksInOut().addListener(new ListChangeListener<CheckInOut>()
            {
                @Override
                public void onChanged (Change<? extends CheckInOut> change)
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
                                    int index = table.getItems().indexOf(Row.this);
                                    // And we refresh it
                                    table.getItems().set(index, Row.this);
                                }
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

        public ObjectProperty<StandardDepartment> getDepartment ()
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

        public DoubleProperty getOvertime ()
        {
            return overtime;
        }
    }

}
