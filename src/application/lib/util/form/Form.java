package application.lib.util.form;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import application.lib.views.CSSClasses;

import java.util.HashMap;

/**
 * Created by Robin on 23/05/2017.
 */
public class Form extends HashMap<String, Form.Field>
{
    public Form ()
    {
    }

    public void add (String key, FieldValueTypes valueType, Control field, FieldTypes type)
    {
        super.put(key, new Field(field, type, valueType));
    }

    public void add (String key, FieldValueTypes valueType, Control field)
    {
        add(key, valueType, field, FieldTypes.TEXTFIELD);
    }

    public void clearAll()
    {
        forEach((s, field) -> {
            if(field.getType() == FieldTypes.TEXTFIELD)
                ((TextField) field.getField()).clear();
        });
    }

    public class Field
    {
        private Control field;
        private FieldTypes type = FieldTypes.TEXTFIELD;
        private FieldValueTypes valueTypes = FieldValueTypes.UNDEFINED;

        private boolean validated = false;

        public Field ()
        {
        }

        public Field (Control field, FieldTypes type, FieldValueTypes valueType)
        {
            this.type = type;
            this.field = field;
            this.valueTypes = valueType;
        }

        public Field (Control field)
        {
            this.field = field;
        }

        public Control getField ()
        {
            return field;
        }

        public void setField (Control field)
        {
            this.field = field;
        }

        public FieldTypes getType ()
        {
            return type;
        }

        public void setType (FieldTypes type)
        {
            this.type = type;
        }

        public FieldValueTypes getValueTypes ()
        {
            return valueTypes;
        }

        public void setValueTypes (FieldValueTypes valueTypes)
        {
            this.valueTypes = valueTypes;
        }

        public void validate ()
        {
            field.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            validated = true;
        }

        public void unValidate ()
        {
            field.getStyleClass().add(CSSClasses.INPUT_INVALID);
            validated = false;
        }

        public boolean isValidated ()
        {
            return validated;
        }

        public void setValidated (boolean validated)
        {
            this.validated = validated;
        }
    }
}
