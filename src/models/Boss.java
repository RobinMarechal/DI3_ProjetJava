package models;

import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Boss of the Company. <br/>
 * Singleton
 */
public class Boss extends Person implements Jsonable
{
    /**
     * Instance of Boss class
     */
    private static Boss bossInstance = new Boss();


    /**
     * No parameter constructor <br/>
     * Define Boss' first-name and last-name.
     */
    private Boss()
    {
        setFirstName("Boss");
        setLastName("of the Company");
    }


    /**
     * Retrieve the instance of the singleton class Boss <br/>
     * @return Boss instance
     */
    public static Boss getBoss()
    {
        return bossInstance;
    }


    /**
     * Creates a String representing a Boss instance <br/>
     * Ex: 'Boss : [first-name] [last-name]'
     * @return String
     */
    @Override
    public String toString()
    {
        return "Boss : " + getFirstName() + " " + getLastName();
    }

    public void loadFromDeserialization (Boss instance)
    {
        bossInstance = instance;
    }

    public static void loadFromJson (JSONObject json)
    {
        Boss obj = getBoss();
        obj.setFirstName(json.get(JSON_KEY_FIRSTNAME).toString());
        obj.setLastName(json.get(JSON_KEY_LASTNAME).toString());
    }
}
