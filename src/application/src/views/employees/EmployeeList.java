package views.employees;

import controllers.EmployeesController;
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
import lib.views.Template;
import models.CheckInOut;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import org.jetbrains.annotations.NotNull;
import views.EmployeesViewController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Robin on 13/05/2017. <br>
 * This class injects the asked data into the components generated with fxml <br>
 * to show the company's list of employees
 */
public class EmployeeList extends EmployeesViewController implements Initializable
{
    /** The date of the checks */
    private final SimpleDate date;
    /** The list of employees */
    private ObservableList<EmployeeRow> rows;
    /** The tooltip text that is displayed when the mouse is over a table's row */
    private final String rowTooltipText = "Click here to see the employee's profile.";

    /** See the department's list of checks at the previous date */
    @FXML private Button btnPrevDate;
    /** See the department's list of checks at the next date */
    @FXML private Button btnNextDate;
    /** See the department's list of checks at another date */
    @FXML private DatePicker datePicker;
    /** The list of employees */
    @FXML private TableView<EmployeeRow> table;
    /** The employee's ID column */
    @FXML private TableColumn<EmployeeRow, IntegerProperty> columnId;
    /** The employee's firstname column */
    @FXML private TableColumn<EmployeeRow, StringProperty> columnFirstName;
    /** The employee's lastname column */
    @FXML private TableColumn<EmployeeRow, StringProperty> columnLastName;
    /** The employee's department's name column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<StandardDepartment>> columnDepartment;
    /** The employee's manager statue column */
    @FXML private TableColumn<EmployeeRow, Boolean> columnManager;
    /** The employee's starting hour column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> columnStartingHour;
    /** The employee's ending hour column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> columnEndingHour;
    /** The employee's check in time this date column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> columnArrivedAt;
    /** The employee's check out time this date column */
    @FXML private TableColumn<EmployeeRow, ObjectProperty<SimpleTime>> columnLeftAt;
    /** The employee's current overtime column */
    @FXML private TableColumn<EmployeeRow, DoubleProperty> columnOvertime;


    /**
     * Constructor <br>
     * Load the fxml view and launch initialize method
     *
     * @param employees the list of employees
     * @param date      the date of the employee's checks
     */
    public EmployeeList (@NotNull ObservableList<Employee> employees, @NotNull SimpleDate date)
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
                }
            }
        });

        rows = FXCollections.observableArrayList();

        rows.addAll(employees.stream().map(EmployeeRow::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/employees/fxml/list.fxml"));
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
        prepareCellFactories();
        display();

        table.setRowFactory(e ->
        {
            TableRow<EmployeeRow> row = new TableRow<>();

            // Adding the tooltip to the row
            row.setTooltip(new Tooltip(rowTooltipText));

            // On row click ==> see employee's profile
            row.setOnMouseClicked(event ->
            {
                EmployeeRow cRow = row.getItem();
                if (cRow != null)
                {
                    Employee emp = cRow.getEmployeeInstance();
                    new EmployeesController().show(emp);
                }
            });

            return row;
        });
    }

    /**
     * Prepare the action events, the table cell value factories and fill the table
     */
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

    /**
     * Initiliaze the others cells factories
     */
    private void prepareCellFactories ()
    {
        columnId.setCellFactory(column -> new TableCell<EmployeeRow, IntegerProperty>()
        {
            @Override
            protected void updateItem (IntegerProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
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

        columnFirstName.setCellFactory(column -> new TableCell<EmployeeRow, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
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

        columnLastName.setCellFactory(column -> new TableCell<EmployeeRow, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
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

        columnDepartment.setCellFactory(column -> new TableCell<EmployeeRow, ObjectProperty<StandardDepartment>>()
        {
            @Override
            protected void updateItem (ObjectProperty<StandardDepartment> item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
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

        columnStartingHour.setCellFactory(column -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                EmployeeRow r = (EmployeeRow) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });

        columnEndingHour.setCellFactory(column -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                EmployeeRow r = (EmployeeRow) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
                }
            }
        });

        columnArrivedAt.setCellFactory(column -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                EmployeeRow r = (EmployeeRow) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
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

        columnLeftAt.setCellFactory(column -> new TableCell<EmployeeRow, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                super.updateItem(item, empty);

                EmployeeRow r = (EmployeeRow) getTableRow().getItem();

                if (item != null && r != null)
                {
                    if (item.getValue() != null)
                    {
                        textProperty().bind(item.asString());
                    }
                }
                else
                {
                    // In the case we deleted an item, we need the unbind it, then removing the text
                    textProperty().unbind();
                    setText("");
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

        columnOvertime.setCellFactory(column -> new TableCell<EmployeeRow, DoubleProperty>()
        {
            @Override
            protected void updateItem (DoubleProperty item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
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
     * Inner class representing a row of the TableView <br>
     * This class must remain public because the TableColumn's cell value factories
     * uses it's getters <br>
     * An inner class is used here because 3 objects are bound to the Table. <br>
     * This class allows me to regroup these three classes into a single one
     */
    public class EmployeeRow
    {
        /** The concerned Employee's intance */
        private Employee employeeInstance;

        /** The employee's ID */
        private IntegerProperty id;
        /** The employee's firstname */
        private StringProperty firstName;
        /** The employee's lastname */
        private StringProperty lastName;
        /** The employee's department's name */
        private ObjectProperty<StandardDepartment> department;
        /** The employee's manager statue */
        private boolean manager = false;
        /** The employee's starting hour */
        private ObjectProperty<SimpleTime> startingHour;
        /** The employee's ending hour */
        private ObjectProperty<SimpleTime> endingHour;
        /** The employee's check in time */
        private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");
        /** The employee's check out time */
        private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");
        /** The employee's current overtime */
        private DoubleProperty overtime;

        /**
         * Constructor <br>
         * Prepare the properties value
         *
         * @param employee the concerned employee
         */
        private EmployeeRow (Employee employee)
        {
            employeeInstance = employee;
            StandardDepartment dep   = employee.getDepartment();
            int                empId = employee.getId();

            id = employee.idProperty();
            firstName = employee.firstNameProperty();
            lastName = employee.lastNameProperty();

            department = employee.departmentProperty();

            manager = employee instanceof Manager;

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
                                    int index = table.getItems().indexOf(EmployeeRow.this);
                                    // And we refresh it
                                    table.getItems().set(index, EmployeeRow.this);
                                }
                            }
                        }
                    }
                }
            });
        }

        /**
         * Get the id property
         *
         * @return the id property
         */
        public IntegerProperty getId ()
        {
            return id;
        }

        /**
         * Get the firstname property
         *
         * @return the firstname property
         */
        public StringProperty getFirstName ()
        {
            return firstName;
        }

        /**
         * Get the lastname property
         *
         * @return the lastname property
         */
        public StringProperty getLastName ()
        {
            return lastName;
        }

        /**
         * Get the departments property
         *
         * @return the departments property
         */
        public ObjectProperty<StandardDepartment> getDepartment ()
        {
            return department;
        }

        /**
         * Is the employee a manager or not
         *
         * @return true if he is a manager, else otherwise
         */
        public boolean isManager ()
        {
            return manager;
        }

        /**
         * Get the starting hour property
         *
         * @return the starting hour property
         */
        public ObjectProperty<SimpleTime> getStartingHour ()
        {
            return startingHour;
        }

        /**
         * Get the ending hour property
         *
         * @return the ending hour property
         */
        public ObjectProperty<SimpleTime> getEndingHour ()
        {
            return endingHour;
        }

        /**
         * Get the check in time property
         *
         * @return the check in time property
         */
        public ObjectProperty<SimpleTime> getArrivedAt ()
        {
            return arrivedAt;
        }

        /**
         * Get the check out time property
         *
         * @return the check out time property
         */
        public ObjectProperty<SimpleTime> getLeftAt ()
        {
            return leftAt;
        }

        /**
         * Get the overtime property
         * @return the overtime property
         */
        public DoubleProperty getOvertime ()
        {
            return overtime;
        }

        /**
         * Get the concerned employee
         * @return the concerned employee
         */
        public Employee getEmployeeInstance ()
        {
            return employeeInstance;
        }
    }

}
