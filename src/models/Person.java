package models;

import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017.
 */
public abstract class Person implements Jsonable
{

    /**
     * The first-name of the Person
     */
    private String firstName;

    /**
     * The last-name of the Person
     */
    private String lastName;

    /**
     * Basic constructor
     */
    public Person(){}


    /**
     * 2 parameters constructor
     * @param firstName the first-name of the person
     * @param lastName the last-name of the person
     */
    public Person(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Retrieve the first-name of the person
     * @return the first-name
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * Retrieve the last-name of the person
     * @return the last-name
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * Modifies the first-name of the person
     * @param firstName the new first-name
     * @return this
     */
    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


    /**
     * Modifies the last-name of the person
     * @param lastName the new last-name
     * @return this
     */
    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Creates a string representing the Person instance
     * @return the string representing the Person instance
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
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

        json.put("firstName", firstName);
        json.put("lastName", lastName);

        return json;
    }


}
