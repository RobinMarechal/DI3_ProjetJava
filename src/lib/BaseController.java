package lib;

import lib.util.form.Form;
import lib.util.validator.Validator;

/**
 * Created by Robin on 25/04/2017.
 */
abstract public class BaseController
{
    abstract public void home();

    public boolean validateForm (Form form)
    {
        Validator validator = new Validator(form);
        boolean   allPassed;

        try
        {
            allPassed = validator.validateForm();
        }
        catch (ClassCastException e)
        {
            System.out.println("Department creation failed: ClassCastException");
            allPassed = false;
        }

        return allPassed;
    }

}
