package application.controllers;

import application.lib.BaseController;
import application.lib.annotations.DisplayView;
import application.lib.annotations.UpdateModel;
import application.lib.form.Form;
import application.lib.views.BaseViewController;
import application.lib.views.Tabs;
import application.lib.views.Template;
import application.models.Company;
import application.models.Employee;
import application.models.Manager;
import application.models.StandardDepartment;
import application.views.dialogs.EditEmployeeDialog;
import application.views.employees.EmployeeList;
import application.views.employees.EmployeeProfile;
import fr.etu.univtours.marechal.SimpleDate;
import fr.etu.univtours.marechal.SimpleTime;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * Created by Robin on 25/04/2017. <br/>
 * MVC Controller class that handle every <br/>
 * actions related to the employees data
 */
public class EmployeesController extends BaseController
{
    /**
     * Display the employee's tab home view
     */
    @Override
    public void home ()
    {
        listAt(SimpleDate.TODAY);
    }

    /**
     * Display the list of employee's and their checks at a specific date
     *
     * @param date the date
     */
    @DisplayView
    public void listAt (SimpleDate date)
    {
        ObservableList<Employee> employees = Company.getCompany().getEmployeesList();

        EmployeeList view = new EmployeeList(employees, date);
        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    /**
     * Display the profile of an employee
     *
     * @param employee the employee to display
     */
    @DisplayView
    public void show (Employee employee)
    {
        System.out.println("Showing employee n°" + employee.getId() + "...");
        BaseViewController view = new EmployeeProfile(employee);

        Template.getInstance().setView(Tabs.EMPLOYEES, view);
    }

    /**
     * Open a dialog box allowing to create an employee (who can be a manager)
     */
    @DisplayView
    public void openCreationEmployeeDialog ()
    {
        final ObservableList<StandardDepartment> list = Company.getCompany().getStandardDepartmentsList();

        EditEmployeeDialog dialog = new EditEmployeeDialog(null, list);
        dialog.getBtnSubmit().setOnAction(event -> createEmployee(dialog.getForm()));
    }

    /**
     * Create an employee <br/>
     *
     * @param form the form containing the employees's information.
     * @return a {@link Employee} instance, or null if an the form has been unvalidated
     */
    @UpdateModel
    public Employee createEmployee (Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return null;
        }
        else
        {
            try
            {
                Employee           employee;
                StandardDepartment dep;

                final Form.Field shHoursField   = form.get("startingHourHour");
                final Form.Field shMinutesField = form.get("startingHourMinutes");
                final Form.Field ehHoursField   = form.get("endingHourHour");
                final Form.Field ehMinutesField = form.get("endingHourMinutes");

                // we retrieve the values
                final String  firstName      = ((TextField) form.get("firstName").getField()).getText();
                final String  lastName       = ((TextField) form.get("lastName").getField()).getText();
                final int     shHoursValue   = Integer.valueOf(((TextField) shHoursField.getField()).getText());
                final int     shMinutesValue = Integer.valueOf(((TextField) shMinutesField.getField()).getText());
                final int     ehHoursValue   = Integer.valueOf(((TextField) ehHoursField.getField()).getText());
                final int     ehMinutesValue = Integer.valueOf(((TextField) ehMinutesField.getField()).getText());
                final boolean isManager      = ((CheckBox) form.get("cbManager").getField()).isSelected();

                SimpleTime endingHour;
                SimpleTime startingHour;

                // Are the times values valid or not
                boolean timesAreValids = checkTimeFieldValue(shHoursValue, shHoursField, 24);
                timesAreValids &= checkTimeFieldValue(ehHoursValue, ehHoursField, 24);
                timesAreValids &= checkTimeFieldValue(shMinutesValue, shMinutesField, 60);
                timesAreValids &= checkTimeFieldValue(ehMinutesValue, ehMinutesField, 60);

                // If not, we prevent the creation and the dialog from closing
                if (!timesAreValids)
                {
                    return null;
                }

                // otherwise, we create the employee (or manager) with the value of the fields
                startingHour = SimpleTime.of(shHoursValue, shMinutesValue);
                endingHour = SimpleTime.of(ehHoursValue, ehMinutesValue);

                if (isManager)
                {
                    employee = Company.createManager(firstName, lastName);
                }
                else
                {
                    employee = Company.createEmployee(firstName, lastName);
                }

                employee.setStartingHour(startingHour);
                employee.setEndingHour(endingHour);

                dep = ((ComboBox<StandardDepartment>) form.get("department").getField()).getValue();

                // If a department was selected
                if (dep != null)
                {
                    dep.addEmployee(employee);
                }

                // Everything worked fine, we clear all the fields
                form.clearAll();

                // re-set default values
                ((TextField) shHoursField.getField()).setText(Employee.DEFAULT_STARTING_HOUR.getHour() + "");
                ((TextField) shMinutesField.getField()).setText(Employee.DEFAULT_STARTING_HOUR.getMinute() + "");
                ((TextField) ehHoursField.getField()).setText(Employee.DEFAULT_ENDING_HOUR.getHour() + "");
                ((TextField) ehMinutesField.getField()).setText(Employee.DEFAULT_ENDING_HOUR.getMinute() + "");

                return employee;
            }
            catch (NullPointerException e)
            {
                // A field is missing
                return null;
            }
        }
    }


    /**
     * Verify the value of a time field <br/>
     * A hours field shouldn't reach 23 <br/>
     * A minutes field shouldn't reach 59
     *
     * @param value the value to verify
     * @param field the field to modify in case of incorrect value
     * @param limit the limit that the value shouldn't reach
     * @return true if the value is correct, false otherwise
     */
    private boolean checkTimeFieldValue (int value, Form.Field field, int limit)
    {
        if (value < 0 || value >= limit)
        {
            field.unValidate();
            return false;
        }

        return true;
    }

    /**
     * Open a dialog box allowing to update an employee
     * @param employee the employee to update
     */
    @DisplayView
    public void openEditionEmployeeDialog (Employee employee)
    {
        final ObservableList<StandardDepartment> list = Company.getCompany().getStandardDepartmentsList();

        EditEmployeeDialog dialog = new EditEmployeeDialog(employee, list);
        dialog.getBtnSubmit().setOnAction(event ->
        {
            if (updateEmployee(employee, dialog.getForm()))
            {
                dialog.close();
            }
        });
    }


    /**
     * update an employee <br/>
     *
     * @param form the form containing the employees's information.
     * @return true if the employee was updated, false otherwise
     */
    @DisplayView
    public boolean updateEmployee (Employee employee, Form form)
    {
        boolean allPassed = validateForm(form);

        if (!allPassed)
        {
            return false;
        }
        else
        {
            try
            {
                StandardDepartment dep;

                final Form.Field shHoursField   = form.get("startingHourHour");
                final Form.Field shMinutesField = form.get("startingHourMinutes");
                final Form.Field ehHoursField   = form.get("endingHourHour");
                final Form.Field ehMinutesField = form.get("endingHourMinutes");

                // we retrieve the values
                final String  firstName      = ((TextField) form.get("firstName").getField()).getText();
                final String  lastName       = ((TextField) form.get("lastName").getField()).getText();
                final int     shHoursValue   = Integer.valueOf(((TextField) shHoursField.getField()).getText());
                final int     shMinutesValue = Integer.valueOf(((TextField) shMinutesField.getField()).getText());
                final int     ehHoursValue   = Integer.valueOf(((TextField) ehHoursField.getField()).getText());
                final int     ehMinutesValue = Integer.valueOf(((TextField) ehMinutesField.getField()).getText());
                final boolean isManager      = ((CheckBox) form.get("cbManager").getField()).isSelected();


                SimpleTime endingHour;
                SimpleTime startingHour;

                // Are the times values valid or not
                boolean timesAreValids = checkTimeFieldValue(shHoursValue, shHoursField, 24);
                timesAreValids &= checkTimeFieldValue(ehHoursValue, ehHoursField, 24);
                timesAreValids &= checkTimeFieldValue(shMinutesValue, shMinutesField, 60);
                timesAreValids &= checkTimeFieldValue(ehMinutesValue, ehMinutesField, 60);

                // If not, we prevent the edition and the dialog from closing
                if (!timesAreValids)
                {
                    return false;
                }

                // If we wasn't manager but the checkbox is checked
                if (isManager && !(employee instanceof Manager))
                {
                    // We upgrade him
                    employee = employee.upgradeToManager();

                    // then we reload the view because the employee instance the view is showing is now invalid
                    show(employee);
                }

                // otherwise, we create the employee (or manager) with the value of the fields
                startingHour = SimpleTime.of(shHoursValue, shMinutesValue);
                endingHour = SimpleTime.of(ehHoursValue, ehMinutesValue);


                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setStartingHour(startingHour);
                employee.setEndingHour(endingHour);

                dep = ((ComboBox<StandardDepartment>) form.get("department").getField()).getValue();

                // If a department was selected
                if (dep != null)
                {
                    dep.addEmployee(employee);
                }

                // Everything worked fine, we clear all the fields
                form.clearAll();

                // re-set default values
                ((TextField) shHoursField.getField()).setText(Employee.DEFAULT_STARTING_HOUR.getHour() + "");
                ((TextField) shMinutesField.getField()).setText(Employee.DEFAULT_STARTING_HOUR.getMinute() + "");
                ((TextField) ehHoursField.getField()).setText(Employee.DEFAULT_ENDING_HOUR.getHour() + "");
                ((TextField) ehMinutesField.getField()).setText(Employee.DEFAULT_ENDING_HOUR.getMinute() + "");

                return true;
            }
            catch (NullPointerException e)
            {
                // A field is missing
                System.out.println("NullPointerException");
                return false;
            }
        }
    }

    /**
     * Open a confirmation dialog to confirm the firing of an employee
     *
     * @param employee the employee to fire
     */
    @UpdateModel
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
