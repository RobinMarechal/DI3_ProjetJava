package views.departments;

import controllers.DepartmentsController;
import controllers.EmployeesController;
import fr.etu.univtours.marechal.SimpleDate;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
 * Created by Robin on 13/05/2017. <br>
 * This class injects the asked data into the components generated with fxml <br>
 * to show the company's list of standard departments
 */
public class DepartmentsList extends DepartmentsViewController implements Initializable
{
    /** The list of departments */
    private final ObservableList<StandardDepartment> departements;
    /** The items of the TableView */
    private ObservableList<DepartmentRow> rows;

    /** The Table containing the list of departments */
    @FXML private TableView<DepartmentRow> table;
    /** The department's ID column */
    @FXML private TableColumn<DepartmentRow, IntegerProperty> colId;
    /** The department's name column */
    @FXML private TableColumn<DepartmentRow, Link> colName;
    /** The department's activity sector column */
    @FXML private TableColumn<DepartmentRow, StringProperty> colActSector;
    /** The department's number of employees column */
    @FXML private TableColumn<DepartmentRow, IntegerBinding> colNbEmployees;
    /** The department's manager column */
    @FXML private TableColumn<DepartmentRow, Link> colManager;


    /**
     * Constructor <br>
     * Load the fxml view and launch initialize method
     *
     * @param departments The company's list of departments
     */
    public DepartmentsList (ObservableList<StandardDepartment> departments)
    {
        this.departements = departments;
        this.rows = FXCollections.observableArrayList();

        // When the observable list is updated
        departments.addListener(new ListChangeListener<StandardDepartment>()
        {
            @Override
            public void onChanged (Change<? extends StandardDepartment> change)
            {
                while (change.next())
                {
                    if (change.wasAdded())
                    {
                        List list = change.getAddedSubList();
                        for (Object o : list)
                        {
                            rows.add(new DepartmentRow((StandardDepartment) o));
                        }
                    }
                    else if (change.wasRemoved())
                    {
                        List list = change.getRemoved();
                        for (Object o : list)
                        {
                            StandardDepartment removed = ((StandardDepartment) o);
                            int                           id      = removed.getId();
                            if (table != null && table.getItems() != null)
                            {
                                ObservableList<DepartmentRow> items = table.getItems();
                                for (DepartmentRow row : items)
                                {
                                    if (row.getId().getValue() == id)
                                    {
                                        items.remove(row);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        rows.addAll(departments.stream().map(DepartmentRow::new).collect(Collectors.toList()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/departments/fxml/list.fxml"));
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
            System.err.println("Failed to load departements' list view...");
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
        prepareCellValueFactories();
        prepareCellFactories();
        table.setItems(rows);
    }

    /**
     * Initialize the TableView's columns celle value factories
     */
    private void prepareCellValueFactories ()
    {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colActSector.setCellValueFactory(new PropertyValueFactory<>("activitySector"));
        colNbEmployees.setCellValueFactory(new PropertyValueFactory<>("nbEmployees"));
        colManager.setCellValueFactory(new PropertyValueFactory<>("manager"));
    }

    /**
     * Initialize the TableView's columns celle factories
     */
    private void prepareCellFactories ()
    {
        // ID
        colId.setCellFactory(column -> new TableCell<DepartmentRow, IntegerProperty>()
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

        // Department's name
        colName.setCellFactory(column -> new TableCell<DepartmentRow, Link>()
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
                        final DepartmentRow row = (DepartmentRow) tableRow.getItem();
                        if (row != null)
                        {
                            textProperty().bind(item.textProperty());

                            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> item.trigger());
                            addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setUnderline(true));
                            addEventHandler(MouseEvent.MOUSE_EXITED, event -> setUnderline(false));
                        }
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

        // Department's activity sector
        colActSector.setCellFactory(column -> new TableCell<DepartmentRow, StringProperty>()
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

        // Nb employees
        colNbEmployees.setCellFactory(column -> new TableCell<DepartmentRow, IntegerBinding>()
        {
            @Override
            protected void updateItem (IntegerBinding item, boolean empty)
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

        // Manager
        colManager.setCellFactory(column -> new TableCell<DepartmentRow, Link>()
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
                        final DepartmentRow row = (DepartmentRow) tableRow.getItem();
                        if (row != null)
                        {
                            final Manager manager = row.getDepartment().getManager();
                            if (manager != null)
                            {
                                Closure closure = () -> new EmployeesController().show(manager);

                                textProperty().bind(item.textProperty());

                                // Link events
                                addEventHandler(MouseEvent.MOUSE_CLICKED, event -> item.trigger());
                                addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setUnderline(true));
                                addEventHandler(MouseEvent.MOUSE_EXITED, event -> setUnderline(false));
                            }
                        }
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
    }

    /**
     * Inner class representing a row of the TableView <br>
     * This class must remain public because the TableColumn's cell value factories
     * uses it's getters <br>
     * An inner class is used here because 2 objects are bound to the Table. <br>
     * This class allows me to regroup these two classes into a single one
     */
    public class DepartmentRow
    {
        /** The concerned department */
        private StandardDepartment department;

        /** The department's ID property */
        private IntegerProperty id;
        /** The department's name {@link Link} */
        private Link name;
        /** The department's activity sector property */
        private StringProperty activitySector;
        /** The department's nb of employees property */
        private IntegerBinding nbEmployees;
        /** The department's manager's name {@link Link} */
        private Link manager;

        /**
         * Constructor <br>
         * Prepare the properties value
         *
         * @param department the concerned department
         */
        public DepartmentRow (StandardDepartment department)
        {
            this.department = department;

            id = department.idProperty();
            activitySector = department.activitySectorProperty();
            nbEmployees = Bindings.size(department.getEmployeesList());

            // Links
            // name
            Closure closureName = () -> new DepartmentsController().show(department, SimpleDate.TODAY);
            name = new Link(closureName);
            name.textProperty().bind(department.nameProperty());

            // manager
            Manager managerInstance = department.getManager();
            Closure closureManager  = () -> new EmployeesController().show(managerInstance);
            manager = new Link(closureManager);
            manager.textProperty().bind(department.managerProperty().asString());
        }

        /**
         * Get the department's ID property
         * @return the department's ID property
         */
        public IntegerProperty getId ()
        {
            return id;
        }


        /**
         * Get the department's name {@link Link}
         * @return the department's name {@link Link}
         */
        public Link getName ()
        {
            return name;
        }


        /**
         * Get the department's activity sector property
         * @return the department's activity sector property
         */
        public StringProperty getActivitySector ()
        {
            return activitySector;
        }


        /**
         * Get the department's number of employees property
         * @return the department's number of employees property
         */
        public IntegerBinding getNbEmployees ()
        {
            return nbEmployees;
        }


        /**
         * Get the department's manager {@link Link}
         * @return the department's manager {@link Link}
         */
        public Link getManager ()
        {
            return manager;
        }


        /**
         * Get the concerned department's
         * @return the concerned department's
         */
        public StandardDepartment getDepartment ()
        {
            return department;
        }
    }


}
