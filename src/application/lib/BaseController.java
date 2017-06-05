package application.lib;

import application.lib.annotations.DisplayView;
import application.lib.form.Form;
import application.lib.form.validator.Validator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 25/04/2017.
 * MVC Controller
 * All application's controllers whould extends this class
 */
abstract public class BaseController
{
    /**
     * Display the home view
     */
    @DisplayView
    abstract public void home();

    /**
     * Perform a form validation using Validator lib class <br />
     * Each unvalidated fields are automatically decorated using CSS     *
     *
     * @param form the form to validate
     * @return - true : every fields was validated <br/> - false : at least one field has been unvalidated
     */
    public boolean validateForm (@NotNull Form form)
    {
        Validator validator = new Validator(form);
        boolean   allPassed;

        try
        {
            allPassed = validator.validateForm();
        }
        catch (ClassCastException e)
        {
            // The Form object was wrongly constructed
            System.out.println("Form validation failed: ClassCastException");
            allPassed = false;
        }

        return allPassed;
    }
}
