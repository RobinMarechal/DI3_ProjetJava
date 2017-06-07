package views.dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import lib.views.custom.components.Dialog;
import models.Company;
import models.Employee;
import models.StandardDepartment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Robin on 26/05/2017. <br>
 * This class represents a dialog box allowing manage the list of employees of a standard department
 */
public class ManageEmployeesDialog extends Dialog implements Initializable
{
    /** The concerned department */
    private StandardDepartment department;
    /** The company of the department */
    private Company company;

    /** The layout */
    @FXML private GridPane root;
    /** The {@link ListView} containing the actual department's list of employees */
    @FXML private ListView<Employee> listOfDepEmployees;
    /** The {@link ListView} containing the list of the employees who don't have any department at the moment */
    @FXML private ListView<Employee> listOfNoDepEmployees;
    /** The button to add a no-department employee to the department */
    @FXML private Button btnAddToDep;
    /** The button to remove an employee from the department */
    @FXML private Button btnRemoveFromDep;
    /** The submit button */
    @FXML private Button btnSubmit;

    /**
     * Constructor, load the fxml view file
     *
     * @param department the department to manage
     */
    public ManageEmployeesDialog (@NotNull StandardDepartment department)
    {
        this.department = department;
        this.company = Company.getCompany();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/views/dialogs/fxml/manageEmployees.fxml"));
        loader.setController(this);

        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            root = new GridPane();
            setTitle("Error");
            System.err.println("Failed to load employee's edition dialog...");
            e.printStackTrace();
        }
        finally
        {
            setContent(root);
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
        setTitle("Manage the department's employees");

        fillLists();
    }

    /**
     * Fill both lists with the {@link ObservableList} attributes
     */
    public void fillLists ()
    {
        ObservableList<Employee> employeesList = FXCollections.observableArrayList(department.getEmployeesList());
        employeesList.remove(department.getManager());
        listOfDepEmployees.setItems(employeesList);
        listOfNoDepEmployees.setItems(company.getEmployeesWithoutDepartment());
    }

    /**
     * Get the submit button
     * @return the submit button
     */
    public Button getBtnSubmit ()
    {
        return btnSubmit;
    }

    /**
     * Get the button that add the selected no-department employee to the department
     * @return the button that add the selected no-department employee to the department
     */
    public Button getBtnAddToDep ()
    {
        return btnAddToDep;
    }

    /**
     * Get the button that remove the selected employee from the department
     * @return the button that remove the selected employee from the department
     */
    public Button getBtnRemoveFromDep ()
    {
        return btnRemoveFromDep;
    }

    /**
     * Get the {@link ListView} that contains the list of departments employee's
     * @return the {@link ListView} that contains the list of departments employee's
     */
    public ListView<Employee> getListOfDepEmployees ()
    {
        return listOfDepEmployees;
    }

    /**
     * Get the {@link ListView} that contains the employes that don't have any department at the moment
     * @return the {@link ListView} that contains the employes that don't have any department at the moment
     */
    public ListView<Employee> getListOfNoDepEmployees ()
    {
        return listOfNoDepEmployees;
    }
}
