package controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lib.BaseController;
import lib.time.SimpleDate;
import lib.util.validator.Validator;
import lib.views.BaseViewController;
import lib.views.CSSClasses;
import lib.views.Tabs;
import lib.views.Template;
import models.Company;
import models.Employee;
import models.Manager;
import models.StandardDepartment;
import org.intellij.lang.annotations.Language;
import views.dialogs.CreateEmployeeDialog;
import views.dialogs.EditEmployeeDialog;
import views.employees.EmployeeList;
import views.employees.EmployeeProfile;

import java.util.Optional;

/**
 * Created by Robin on 25/04/2017.
 */
public class EmployeesController extends BaseController
{
    @Language ("RegExp") private String nameRegexp = "^([A-Z]?[a-z]+)(([ -][A-Z]?[a-z]+))*$";

    public EmployeesController ()
    {
    }

    @Override
    public void home ()
    {
        listAt(SimpleDate.TODAY);
    }

    public void listAt (SimpleDate date)
    {
        ObservableList<Employee> employees = Company.getCompany().getEmployeesList();
        BaseViewController       view      = new EmployeeList(employees, date);

        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    public void show (int id)
    {
        Employee employee = Company.getCompany().getEmployee(id);
        show(employee);

        // Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    public void show (Employee employee)
    {
        System.out.println("Showing employee n°" + employee.getId() + "...");
        BaseViewController view = new EmployeeProfile(employee);

        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    public void openCreationEmployeeDialog ()
    {
        final ObservableList<StandardDepartment> list = Company.getCompany().getStandardDepartmentsList();
        new CreateEmployeeDialog(list);
    }

    public Employee createEmploye (TextField fieldFirstName, TextField fieldLastName, ComboBox comboDepartments)
    {
        return createEmploye(fieldFirstName, fieldLastName, comboDepartments, false);
    }

    public Employee createEmploye (TextField fieldFirstName, TextField fieldLastName, ComboBox comboDepartments,
                                   boolean isManager)
    {
        String firstName = null;
        String lastName  = null;

        if (Validator.make(fieldFirstName, nameRegexp))
        {
            firstName = fieldFirstName.getText().trim();
            fieldFirstName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldFirstName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(fieldLastName, nameRegexp))
        {
            lastName = fieldLastName.getText().trim();
            fieldLastName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldLastName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        StandardDepartment dep = null;
        if (comboDepartments != null)
        {
            dep = (StandardDepartment) comboDepartments.getValue();

            if (dep == null)
            {
                comboDepartments.getStyleClass().add(CSSClasses.INPUT_INVALID);
            }
            else
            {
                comboDepartments.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            }
        }

        if (firstName != null && lastName != null)
        {
            Employee e;
            if (isManager)
            {
                e = Company.createManager(firstName, lastName);
            }
            else
            {
                e = Company.createEmployee(firstName, lastName);
            }

            if (dep != null)
            {
                dep.addEmployee(e);
            }

            fieldFirstName.clear();
            fieldLastName.clear();
            fieldFirstName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            fieldLastName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            if (comboDepartments != null)
            {
                comboDepartments.getStyleClass().remove(CSSClasses.INPUT_INVALID);
                comboDepartments.valueProperty().set(null);
            }

            return e;
        }

        return null;
    }

    public void openEditionEmployeeDialog (Employee employee)
    {
        final ObservableList<StandardDepartment> list = Company.getCompany().getStandardDepartmentsList();
        new EditEmployeeDialog(employee, list);
    }

    public boolean updateEmployee (Employee employee, TextField fieldFirstName, TextField fieldLastName, ComboBox
            comboDepartments)
    {
        String firstName = null;
        String lastName  = null;

        if (Validator.make(fieldFirstName, nameRegexp))
        {
            firstName = fieldFirstName.getText().trim();
            fieldFirstName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldFirstName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        if (Validator.make(fieldLastName, nameRegexp))
        {
            lastName = fieldLastName.getText().trim();
            fieldLastName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
        }
        else
        {
            fieldLastName.getStyleClass().add(CSSClasses.INPUT_INVALID);
        }

        StandardDepartment dep = null;
        if (comboDepartments != null)
        {
            dep = (StandardDepartment) comboDepartments.getValue();

            if (dep == null)
            {
                comboDepartments.getStyleClass().add(CSSClasses.INPUT_INVALID);
            }
            else
            {
                comboDepartments.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            }
        }

        if (firstName != null && lastName != null)
        {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);

            // if the selected a department
            if (dep != null)
            {
                // If (he's not a manager) || (he is but he isn't the manager of his department)
                if (!(employee instanceof Manager) || ((Manager) employee).getManagedDepartment() == null)
                {
                    dep.addEmployee(employee);
                }
                else if(dep != employee.getDepartment())
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText("The department has not beed updated.");
                    alert.setContentText("Reason: The employee is the manager of his department, you can't move him " +
                            "to another department while it's the case.");
                    alert.show();
                }
            }

            fieldFirstName.clear();
            fieldLastName.clear();
            fieldFirstName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            fieldLastName.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            if (comboDepartments != null)
            {
                comboDepartments.getStyleClass().remove(CSSClasses.INPUT_INVALID);
                comboDepartments.valueProperty().set(null);
            }

            show(employee);
            return true;
        }

        return false;
    }

    public void fireEmployee (Employee employee)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Firing " + employee.getFirstName() + " " + employee.getLastName());
        alert.setHeaderText("Firing " + employee.getFirstName() + " " + employee.getLastName());
        alert.setContentText("Are you sure? \nThis can't be undone!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            try
            {
                employee.fire();
                new EmployeesController().home();
            }
            catch (Exception e)
            {
                Alert failed = new Alert(Alert.AlertType.ERROR);
                failed.setHeaderText("Error");
                failed.setContentText(e.getMessage());
                failed.show();
            }
        }

        alert.close();
    }
}
