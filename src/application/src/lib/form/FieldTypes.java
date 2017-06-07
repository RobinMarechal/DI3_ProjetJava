package lib.form;

/**
 * Created by Robin on 23/05/2017. <br>
 * Form.Field types of {@link javafx.scene.control.Control}
 * This allow the validator to perform a validation to certains fields
 */
public enum FieldTypes
{
    /** The type is undifined */
    UNDEFINED,

    /** The control is a {@link javafx.scene.control.TextField} */
    TEXTFIELD,

    /** The control is a {@link javafx.scene.control.ComboBox} */
    COMBOBOX,

    /** The control is a {@link javafx.scene.control.CheckBox} */
    CHECKBOX
}
