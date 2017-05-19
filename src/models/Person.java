package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class Person implements Jsonable, Serializable
{
    private StringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    private StringProperty lastName = new SimpleStringProperty(this, "lastName", "");


    public Person(){}

    public Person (String firstName, String lastName)
    {
        this.firstName.setValue(firstName);
        this.lastName.setValue(lastName);
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
        this.firstName.set(firstName);
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
        this.lastName.set(lastName);
        return this;
    }

    /**
     * Creates a string representing the Person instance
     * @return the string representing the Person instance
     */
    @Override
    public String toString() {
        return firstName.getValueSafe() + " " + lastName.getValueSafe();
    }


    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    @Override
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();

        json.put("firstName", firstName.getValueSafe());
        json.put("lastName", lastName.getValueSafe());

        return json;
    }
}
