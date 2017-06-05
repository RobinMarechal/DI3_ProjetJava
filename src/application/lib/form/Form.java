package application.lib.form;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import application.lib.views.CSSClasses;

import java.util.HashMap;

/**
 * Created by Robin on 23/05/2017. <br/>
 * This class represent a form with differents fields
 */
public class Form extends HashMap<String, Form.Field>
{
    /**
     * Add a field to the form
     *
     * @param key       the HashMap key
     * @param valueType the type of the value (e.g. firstname, date, datetime...)
     * @param field     the field
     * @param type      the type of field (e.g. TextField, ComboBox...)
     */
    public void add (String key, FieldValueTypes valueType, Control field, FieldTypes type)
    {
        super.put(key, new Field(field, type, valueType));
    }

    /**
     * Add a TextField field to the form
     *
     * @param key       the HashMap key
     * @param valueType the type of the value (e.g. firstname, date, datetime...)
     * @param field     the field
     */
    public void add (String key, FieldValueTypes valueType, Control field)
    {
        add(key, valueType, field, FieldTypes.TEXTFIELD);
    }

    /**
     * Reset every fields at it's default content
     */
    public void clearAll ()
    {
        forEach((s, field) ->
        {
            if (field.getType() == FieldTypes.TEXTFIELD)
            {
                ((TextField) field.getField()).clear();
            }
            else if (field.getType() == FieldTypes.CHECKBOX)
            {
                ((CheckBox) field.getField()).setSelected(false);
            }
            else if (field.getType() == FieldTypes.COMBOBOX)
            {
                ((ComboBox<Object>) field.getField()).getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Inner class representing a field of a form
     */
    public class Field
    {
        /** The field */
        private Control field;

        /** The Type of the control (e.g. TextField, ComboBox...) */
        private FieldTypes type = FieldTypes.TEXTFIELD;

        /** The type of the value (e.g. Name, Date, Datetime...) */
        private FieldValueTypes valueTypes = FieldValueTypes.UNDEFINED;

        /** Has the field been validated or not */
        private boolean validated = false;

        /** Default constructor */
        public Field ()
        {
        }

        /**
         * 3 parameter sconstructor
         *
         * @param field     the field extending {@link Control}
         * @param type      the Type of the control (e.g. TextField, ComboBox...)
         * @param valueType the type of the value (e.g. Name, Date, Datetime...)
         */
        public Field (Control field, FieldTypes type, FieldValueTypes valueType)
        {
            this.type = type;
            this.field = field;
            this.valueTypes = valueType;
        }

        /**
         * 1 parameter constructor
         *
         * @param field the field extending {@link Control}
         */
        public Field (Control field)
        {
            this.field = field;
        }

        /**
         * Get the field as {@link Control} instance
         *
         * @return the field as {@link Control} instance
         */
        public Control getField ()
        {
            return field;
        }

        /**
         * Set the field
         *
         * @param field the field
         */
        public void setField (Control field)
        {
            this.field = field;
        }

        /**
         * Get the type of the field (e.g. TextField, ComboBox...)
         *
         * @return the type of the field (e.g. TextField, ComboBox...)
         */
        public FieldTypes getType ()
        {
            return type;
        }

        /**
         * Set the type of the field (e.g. TextField, ComboBox...)
         *
         * @param type the type of the field (e.g. TextField, ComboBox...)
         */
        public void setType (FieldTypes type)
        {
            this.type = type;
        }

        /**
         * Get the type of the value (e.g. Name, Date, Datetime...)
         *
         * @return the type of the value (e.g. Name, Date, Datetime...)
         */
        public FieldValueTypes getValueTypes ()
        {
            return valueTypes;
        }

        /**
         * Set the type of the value (e.g. Name, Date, Datetime...)
         *
         * @param valueTypes the type of the value (e.g. Name, Date, Datetime...)
         */
        public void setValueTypes (FieldValueTypes valueTypes)
        {
            this.valueTypes = valueTypes;
        }

        /**
         * Mark the field as validated <br/>
         * If the field was previously unvalidated, the unvalidation CSS class is removed
         */
        public void validate ()
        {
            field.getStyleClass().remove(CSSClasses.INPUT_INVALID);
            validated = true;
        }

        /**
         * Mark the field as unvalidated <br/>
         * The field style is changed using CSS class
         */
        public void unValidate ()
        {
            field.getStyleClass().add(CSSClasses.INPUT_INVALID);
            validated = false;
        }

        /**
         * Know if the field has been validated or not
         *
         * @return true if it was validated, false otherwise
         */
        public boolean isValidated ()
        {
            return validated;
        }
    }
}
