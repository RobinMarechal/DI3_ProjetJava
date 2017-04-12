package models;

import lib.json.JsonSaver;
import lib.json.Jsonable;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 27/03/2017. <br/>
 * Represents the Boss of the Company. <br/>
 * Singleton
 */
public class Boss extends Person implements Jsonable, JsonSaver
{
    /**
     * Instance of Boss class
     */
    private static Boss bossInstance = new Boss();


    /**
     * No parameter constructor <br/>
     * Define Boss' first-name and last-name.
     */
    public Boss()
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

    /**
     * Save the data of a class instance into a json file
     */
    @Override
    public void save ()
    {
        String path = "data\\files";
        String filename = "boss.json";
        saveToFile(path, filename, toJson());
    }
}
