package application.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import application.lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017. <br/>
 * This class represents every human person, such as the boss, the employees or the managers.
 */
public abstract class Person implements Jsonable
{
    /** The JSON key containing the person's firstname value */
    protected static final String JSON_KEY_FIRSTNAME = "firstName";
    /** The JSON key containing the person's lastname value */
    protected static final String JSON_KEY_LASTNAME = "lastName";

    /** The person's firstname */
    private StringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    /** The person's lastname */
    private StringProperty lastName = new SimpleStringProperty(this, "lastName", "");

    /**
     * Default constructor <br/>
     * Does nothing.
     */
    public Person ()
    {
    }

    /**
     * 2 parameters constructor
     * @param firstName the person's firstname
     * @param lastName the person's lastname
     */
    public Person (String firstName, String lastName)
    {
        this.firstName.setValue(formatName(firstName));
        this.lastName.setValue(formatName(lastName));
    }

    /**
     * Get the person's firstname
     * @return the person's firstname
     */
    public String getFirstName ()
    {
        return firstName.get();
    }

    /**
     * Get the person's firstname property which can be used for bindinds
     * @return the person's firstname property
     */
    public StringProperty firstNameProperty ()
    {
        return firstName;
    }


    /**
     * Change the person's firstname
     * @param firstName the new firstname
     */
    public Person setFirstName (String firstName)
    {
        this.firstName.set(formatName(firstName));
        return this;
    }

    /**
     * Get the person's lastname
     * @return the person's lastname
     */
    public String getLastName ()
    {
        return lastName.get();
    }

    /**
     * Get the person's lastname property which can be used for bindinds
     * @return the person's lastname property
     */
    public StringProperty lastNameProperty ()
    {
        return lastName;
    }

    /**
     * Change the person's lastname
     * @param lastName the new lastname
     */
    public void setLastName (String lastName)
    {
        this.lastName.set(formatName(lastName));
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

    /**
     * Assure that the names values has the right format
     * @param name the name to format
     * @return the formmated name
     */
    private String formatName (String name)
    {
        String result = "";

        String[] arraySpace = name.trim().split(" ");

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

        result = result.substring(0, result.length()-1);

        return result;
    }
}
