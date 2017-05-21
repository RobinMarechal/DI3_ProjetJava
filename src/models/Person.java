package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class Person implements Jsonable
{
    protected static final String JSON_KEY_FIRSTNAME = "firstName";
    protected static final String JSON_KEY_LASTNAME = "lastName";

    private StringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    private StringProperty lastName = new SimpleStringProperty(this, "lastName", "");


    public Person ()
    {
    }

    public Person (String firstName, String lastName)
    {
        this.firstName.setValue(formatName(firstName));
        this.lastName.setValue(formatName(lastName));
    }

    public String getFirstName ()
    {
        return firstName.get();
    }

    public StringProperty firstNameProperty ()
    {
        return firstName;
    }

    public Person setFirstName (String firstName)
    {
        this.firstName.set(formatName(firstName));
        return this;
    }

    public String getLastName ()
    {
        return lastName.get();
    }

    public StringProperty lastNameProperty ()
    {
        return lastName;
    }

    public Person setLastName (String lastName)
    {
        this.lastName.set(formatName(lastName));
        return this;
    }

    /**
     * Creates a string representing the Person instance
     *
     * @return the string representing the Person instance
     */
    @Override
    public String toString ()
    {
        return firstName.getValueSafe() + " " + lastName.getValueSafe();
    }


    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson ()
    {
        JSONObject json = new JSONObject();

        json.put(JSON_KEY_FIRSTNAME, firstName.getValueSafe());
        json.put(JSON_KEY_LASTNAME, lastName.getValueSafe());

        return json;
    }

    private String formatName (String str)
    {
        String result = "";

        String[] arraySpace = str.trim().split(" ");

        for (String s : arraySpace)
        {
            String[] arrayDash = s.split("-");
            for (String part : arrayDash)
            {
                if (part.length() > 1)
                {
                    result += part.substring(0, 1).toUpperCase() + part.substring(1);
                }
                else
                {
                    result += part.toUpperCase();
                }
            }
            result += " ";
        }

        return result;
    }
}
