package lib.util.validator;

import javafx.scene.control.TextInputControl;

/**
 * Created by Robin on 21/05/2017.
 */
public class Validator
{
    private TextInputControl input;
    private String regexp;

    public Validator (TextInputControl input, String regexp)
    {
        this();
        this.input = input;
        this.regexp = regexp;
    }

    public Validator (TextInputControl input)
    {
        this();
        this.input = input;
    }

    public Validator (String regexp)
    {
        this();
        this.regexp = regexp;
    }

    public Validator ()
    {
    }

    public TextInputControl getInput ()
    {
        return input;
    }

    public void setInput (TextInputControl input)
    {
        this.input = input;
    }

    public String getRegexp ()
    {
        return regexp;
    }

    public void setRegexp (String regexp)
    {
        this.regexp = regexp;
    }

    public boolean validate()
    {
        String value = input.getText().trim();
        return value.matches(regexp);
    }

    public static boolean make(TextInputControl input, String regexp)
    {
        return new Validator(input, regexp).validate();
    }
}
