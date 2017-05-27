package application.lib.util.validator;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import application.lib.util.form.FieldTypes;
import application.lib.util.form.FieldValueTypes;
import application.lib.util.form.Form;

/**
 * Created by Robin on 21/05/2017.
 */
public class Validator
{
    private Form form;

    public Validator ()
    {
    }

    public Validator (Form form)
    {
        this.form = form;
    }

    public Form getForm ()
    {
        return form;
    }

    public void setForm (Form form)
    {
        this.form = form;
    }

    public boolean validateForm()
    {
        boolean result = true;

        for (Form.Entry<String, Form.Field> entry : form.entrySet())
        {
            if (!validateField(entry.getValue()))
                result = false;
        }
        
        return result;
    }

    public boolean validateField (Form.Field value)
    {
        final FieldTypes type = value.getType();
        final Control control = value.getField();
        final FieldValueTypes valueTypes = value.getValueTypes();

        if(type == FieldTypes.TEXTFIELD)
        {
            try
            {
                TextField field = (TextField) control;
                if(field.getText().matches(valueTypes.getRegexp()))
                {
                    value.validate();
                    return true;
                }
                else
                {
                    value.unValidate();
                    return false;
                }
            }
            catch(ClassCastException e)
            {
                value.unValidate();
                return false;
            }
        }

        return true;
    }
}
