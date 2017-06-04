package application.views.departments;

import application.controllers.DepartmentsController;
import application.controllers.EmployeesController;
import application.models.*;
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
import application.lib.util.Closure;
import application.lib.views.Template;
import application.lib.views.custom.components.Link;
import application.views.DepartmentsViewController;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Robin on 22/05/2017.
 * EmployeesClass = the class of the employees (i.e Employee or Manager)
 */
public class DepartmentProfile extends DepartmentsViewController implements Initializable
{
    private final StandardDepartment department;
    private final SimpleDate date;
    private ObservableList<Row> rows;

    @FXML private Label labName;
    @FXML private Label labId;
    @FXML private Label labSector;
    @FXML private Label labManager;
    @FXML private Label labNbEmployees;
    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, IntegerProperty> colId;
    @FXML private TableColumn<Row, StringProperty> colFirstName;
    @FXML private TableColumn<Row, StringProperty> colLastName;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> colArrivedAt;
    @FXML private TableColumn<Row, ObjectProperty<SimpleTime>> colLeftAt;
    @FXML private Button btnPrevDate;
    @FXML private Button btnNextDate;
    @FXML private DatePicker datePicker;
    @FXML private Button btnManageEmployees;
    @FXML private Button btnEdit;
    @FXML private Button btnRemove;

    private final String rowTooltipText = "Click here to see the employee's profile.";

    public DepartmentProfile (StandardDepartment department, SimpleDate date)
    {
        this.department = department;
        this.date = date;

        rows = FXCollections.observableArrayList();

        rows.addAll(department.getEmployeesList().stream().map(Row::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/departments/fxml/show.fxml"));
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
            e.printStackTrace();
        }
    }

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

    private void display ()
    {
        displayLeftComponents();
        displayRightComponents();
    }

    private void displayRightComponents ()
    {
        prepareRowEvent();
        initCellValueFactories();
        initCellFactories();
        initDatePickerBar();

        table.setItems(rows);
        table.setSortPolicy(new Callback<TableView<Row>, Boolean>() {
            @Override
            public Boolean call (TableView<Row> param)
            {
                Comparator<Row> comparator = new Comparator<Row>() {
                    @Override
                    public int compare (Row o1, Row o2)
                    {
                        return o1.getId().getValue().compareTo(o2.getId().getValue());
                    }
                };

                FXCollections.sort(table.getItems(), comparator);
                return true;
            }
        });
    }

    private void prepareRowEvent ()
    {

        table.setRowFactory(e ->
        {
            TableRow<Row> row = new TableRow<>();

            // Adding the tooltip to the row
            row.setTooltip(new Tooltip(rowTooltipText));
            row.getStyleClass().add("mouse-hand");

            // On row click ==> see employee's profile
            row.setOnMouseClicked(event ->
            {
                Row cRow = row.getItem();
                if (cRow != null)
                {
                    Employee emp = cRow.getEmployee();
                    new EmployeesController().show(emp);
                }

            });
            return row;
        });
    }

    private void initDatePickerBar ()
    {
        btnNextDate.setOnAction(event -> new DepartmentsController().show(department, date.plusDays(1)));
        btnPrevDate.setOnAction(event -> new DepartmentsController().show(department, date.minusDays(1)));
        datePicker.setOnAction(event -> new DepartmentsController().show(department, SimpleDate.fromLocalDate(datePicker.getValue())));
        datePicker.setShowWeekNumbers(false);
        datePicker.setPromptText(date.toString());
    }


    private void initCellValueFactories ()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colArrivedAt.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        colLeftAt.setCellValueFactory(new PropertyValueFactory<>("leftAt"));
    }

    private void initCellFactories ()
    {
        colId.setCellFactory(param -> new TableCell<Row, IntegerProperty>()
        {
            @Override
            protected void updateItem (IntegerProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item.asString());
                }
            }
        });


        colFirstName.setCellFactory(param -> new TableCell<Row, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item);
                }
            }
        });


        colLastName.setCellFactory(param -> new TableCell<Row, StringProperty>()
        {
            @Override
            protected void updateItem (StringProperty item, boolean empty)
            {
                if (!empty && item != null)
                {
                    textProperty().bind(item);
                }
            }
        });

        colArrivedAt.setCellFactory(param -> new TableCell<Row, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                if (!empty && item != null && item.getValue() != null)
                {
                    textProperty().bind(item.asString());
                }
            }
        });

        colLeftAt.setCellFactory(param -> new TableCell<Row, ObjectProperty<SimpleTime>>()
        {
            @Override
            protected void updateItem (ObjectProperty<SimpleTime> item, boolean empty)
            {
                if (!empty && item != null && item.getValue() != null)
                {
                    textProperty().bind(item.asString());
                }
            }
        });
    }

    private void displayLeftComponents ()
    {
        StandardDepartment standarDep = department;
        Manager            manager    = standarDep.getManager();
        Closure            closure    = () -> new EmployeesController().show(standarDep.getManager());
        Link               link       = new Link(closure);
        link.textProperty().bind(Bindings.concat(manager.firstNameProperty(), " ", manager.lastNameProperty()));

        labName.textProperty().bind(department.nameProperty());
        labId.textProperty().bind(standarDep.idProperty().asString());
        labSector.textProperty().bind(department.activitySectorProperty());
        labManager.setText(null);
        labManager.setGraphic(link);
        labNbEmployees.textProperty().bind(Bindings.size(department.getEmployees()).asString());
    }

    public class Row
    {
        private Employee employee;

        private IntegerProperty id;
        private StringProperty firstName;
        private StringProperty lastName;
        private ObjectProperty<SimpleTime> arrivedAt = new SimpleObjectProperty<>(this, "arrivedAt");
        private ObjectProperty<SimpleTime> leftAt = new SimpleObjectProperty<>(this, "leftAt");

        public Row (Employee employee)
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
                                int index = table.getItems().indexOf(Row.this);
                                // And we refresh it
                                table.getItems().set(index, Row.this);
                            }
                        }
                    }
                }
            });
        }

        public Employee getEmployee ()
        {
            return employee;
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

        public ObjectProperty<SimpleTime> getArrivedAt ()
        {
            return arrivedAt;
        }

        public ObjectProperty<SimpleTime> getLeftAt ()
        {
            return leftAt;
        }
    }
}