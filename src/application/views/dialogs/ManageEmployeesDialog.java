package application.views.dialogs;

import application.lib.views.custom.components.Dialog;
import application.models.Company;
import application.models.Employee;
import application.models.StandardDepartment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 26/05/2017.
 */
public class ManageEmployeesDialog extends Dialog implements Initializable
{
    private StandardDepartment department;
    private Company company;

    @FXML private GridPane root;
    @FXML private ListView<Employee> listOfDepEmployees;
    @FXML private ListView<Employee> listOfNoDepEmployees;
    @FXML private Button btnAddToDep;
    @FXML private Button btnRemoveFromDep;
    @FXML private Button btnSubmit;

    public ManageEmployeesDialog (StandardDepartment department, Company company)
    {
        this.department = department;
        this.company = company;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/dialogs/fxml/manageEmployees.fxml"));
        loader.setController(this);

        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            root = new GridPane();
            stage.setTitle("Error");
            System.out.println("Failed to load employee's edition dialog...");
            e.printStackTrace();
        }
        finally
        {
            setContent(root);
        }
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        stage.setTitle("Manage the department's employees");

        fillLists();
        prepareButtonClickEvents();
    }

    private void fillLists ()
    {
        ObservableList<Employee> employeesList = FXCollections.observableArrayList(department.getEmployeesList());
        employeesList.remove(department.getManager());
        listOfDepEmployees.setItems(employeesList);
        listOfNoDepEmployees.setItems(company.getEmployeesWithoutDepartment());
    }

    private void prepareButtonClickEvents ()
    {
        btnAddToDep.setOnAction(event ->
        {
            new Thread(() -> Platform.runLater(() ->
            {
                System.out.println("right to left");
                final ObservableList<Employee> selectedItems = listOfNoDepEmployees.getSelectionModel().getSelectedItems();
                for (Employee item : selectedItems)
                {
                    department.addEmployee(item);
                }
                fillLists();
                listOfNoDepEmployees.getSelectionModel().select(listOfNoDepEmployees.getItems().size() - 1);
            })).start();
        });

        btnRemoveFromDep.setOnAction(event ->
        {
            new Thread(() -> Platform.runLater(() ->
            {
                System.out.println("left to right");
                final ObservableList<Employee> selectedItems = listOfDepEmployees.getSelectionModel().getSelectedItems();
                for (Employee item : selectedItems)
                {
                    department.removeEmployee(item);
                }
                fillLists();
                listOfDepEmployees.getSelectionModel().select(listOfDepEmployees.getItems().size() - 1);
            })).start();
        });

        btnSubmit.setOnAction(event -> stage.close());
    }


}
