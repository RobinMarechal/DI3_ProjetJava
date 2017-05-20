package views.departments;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lib.util.Closure;
import lib.views.Template;
import lib.views.custom.components.Link;
import models.Manager;
import models.StandardDepartment;
import views.DepartmentsViewController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Robin on 13/05/2017.
 */
public class DepartmentsList extends DepartmentsViewController implements Initializable
{
    private final ObservableList<StandardDepartment> departements;
    private ObservableList<Row> rows;

    // jfx controls
    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, IntegerProperty> colId;
    @FXML private TableColumn<Row, Link> colName;
    @FXML private TableColumn<Row, StringProperty> colActSector;
    @FXML private TableColumn<Row, IntegerBinding> colNbEmployees;
    @FXML private TableColumn<Row, Link> colManager;

    public DepartmentsList (ObservableList<StandardDepartment> departments)
    {
        this.departements = departments;
        this.rows = FXCollections.observableArrayList();

        departments.addListener(new ListChangeListener<StandardDepartment>()
        {
            @Override
            public void onChanged (Change<? extends StandardDepartment> change)
            {
                while (change.next())
                {
                    List list = change.getAddedSubList();
                    for (Object o : list)
                    {
                        rows.add(new Row((StandardDepartment) o));
                    }
                }
            }
        });

        rows.addAll(departments.stream().map(Row::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/departments/fxml/list.fxml"));
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
            System.out.println("Failed to load departements' list view...");
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        prepareCellValueFactories();
        prepareCellFactories();
        table.setItems(rows);
    }

    private void prepareCellValueFactories ()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colActSector.setCellValueFactory(new PropertyValueFactory<>("activitySector"));
        colNbEmployees.setCellValueFactory(new PropertyValueFactory<>("nbEmployees"));
        colManager.setCellValueFactory(new PropertyValueFactory<>("manager"));
    }

    private void prepareCellFactories ()
    {
        // ID
        colId.setCellFactory(column -> new TableCell<Row, IntegerProperty>()
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

        // Department's name
        colName.setCellFactory(column -> new TableCell<Row, Link>()
        {
            @Override
            protected void updateItem (Link item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    final TableRow tableRow = getTableRow();
                    if (tableRow != null)
                    {
                        final Row row = (Row) tableRow.getItem();
                        if (row != null)
                        {
                            Closure closure = () -> new DepartmentsController().show(row.getDepartment());

                            textProperty().bind(item.textProperty());

                            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> item.trigger());
                            addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setUnderline(true));
                            addEventHandler(MouseEvent.MOUSE_EXITED, event -> setUnderline(false));
                        }
                    }
                }
            }
        });

        // Department's activity sector
        colActSector.setCellFactory(column -> new TableCell<Row, StringProperty>()
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

        // Nb employees
        colNbEmployees.setCellFactory(column -> new TableCell<Row, IntegerBinding>()
        {
            @Override
            protected void updateItem (IntegerBinding item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    textProperty().bind(item.asString());
                }
            }
        });

        // Manager
        colManager.setCellFactory(column -> new TableCell<Row, Link>()
        {
            @Override
            protected void updateItem (Link item, boolean empty)
            {
                super.updateItem(item, empty);

                if (item != null && !empty)
                {
                    final TableRow tableRow = getTableRow();
                    if (tableRow != null)
                    {
                        final Row row = (Row) tableRow.getItem();
                        if (row != null)
                        {
                            final Manager manager = row.getDepartment().getManager();
                            if (manager != null)
                            {
                                Closure closure = () -> new EmployeesController().show(manager);

                                textProperty().bind(item.textProperty());

                                addEventHandler(MouseEvent.MOUSE_CLICKED, event -> item.trigger());
                                addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setUnderline(true));
                                addEventHandler(MouseEvent.MOUSE_EXITED, event -> setUnderline(false));
                            }
                        }
                    }
                }
            }
        });
    }

    public class Row
    {
        private IntegerProperty id;
        private Link name;
        private StringProperty activitySector;
        private IntegerBinding nbEmployees;
        private Link manager;

        private StandardDepartment department;

        public Row (StandardDepartment department)
        {
            this.department = department;

            id = department.idProperty();
            activitySector = department.activitySectorProperty();
            nbEmployees = Bindings.size(department.getEmployeesList());

            // Links
            // name
            Closure closureName = () -> new DepartmentsController().show(department);
            name = new Link(department.nameProperty(), closureName);

            // manager
            Manager managerInstance = department.getManager();
            Closure closureManager  = () -> new EmployeesController().show(managerInstance);
            manager = new Link(closureManager);
            manager.textProperty()
                   .bind(Bindings.concat(managerInstance.firstNameProperty(), " ", managerInstance.lastNameProperty()));
        }

        public IntegerProperty getId ()
        {
            return id;
        }

        public Link getName ()
        {
            return name;
        }

        public StringProperty getActivitySector ()
        {
            return activitySector;
        }

        public IntegerBinding getNbEmployees ()
        {
            return nbEmployees;
        }

        public Link getManager ()
        {
            return manager;
        }

        public StandardDepartment getDepartment ()
        {
            return department;
        }
    }


}
