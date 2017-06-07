package lib.form.validator;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import lib.form.FieldTypes;
import lib.form.FieldValueTypes;
import lib.form.Form;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Robin on 21/05/2017.<br>
 * A form validator
 */
public class Validator
{
    /** The form to validate */
    private Form form;

    /** Default constructor */
    public Validator ()
    {
    }

    /**
     * 1 parameter constructor
     *
     * @param form the form to validate
     */
    public Validator (Form form)
    {
        this.form = form;
    }

    /**
     * Get the form
     *
     * @return the form
     */
    public Form getForm ()
    {
        return form;
    }

    /**
     * Set the form
     *
     * @param form the form
     */
    public void setForm (@NotNull Form form)
    {
        this.form = form;
    }

    /**
     * Performs the validation of the form <br>
     * Each field is tested based on the fields' {@link FieldTypes} and their {@link FieldValueTypes} with regexp test
     *
     * @return true if every fields were validated, false otherwise
     */
    public boolean validateForm ()
    {
        boolean result = true;

        for (Form.Entry<String, Form.Field> entry : form.entrySet())
        {
            if (!validateField(entry.getValue()))
            {
                result = false;
            }
        }

        return result;
    }

    /**
     * Test if a field is valid or not <br>
     * Each field is tested based on it's {@link FieldTypes} and it's {@link FieldValueTypes} with regexp  <br>
     * If a field is unvalidated, a CSS style is applied to it.
     *
     * @param formField the form field instance
     * @return true if the content is valid, false otherwise
     */
    public boolean validateField (@NotNull Form.Field formField)
    {
        final FieldTypes      type       = formField.getType();
        final Control         control    = formField.getField();
        final FieldValueTypes valueTypes = formField.getValueTypes();

        if (type == FieldTypes.TEXTFIELD)
        {
            try
            {
                TextField field = (TextField) control;
                if (field.getText().matches(valueTypes.getRegexp()))
                {
                    formField.validate();
                    return true;
                }
                else
                {
                    formField.unValidate();
                    return false;
                }
            }
            catch (ClassCastException e)
            {
                formField.unValidate();
                return false;
            }
        }

        return true;
    }
}
