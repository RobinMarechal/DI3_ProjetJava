package application.lib.json;

import org.json.simple.JSONObject;

/**
 * Created by Robin on 11/04/2017.<br/>
 * The class that implements this interface should provide a way to load data from {@link JSONObject}
 */
public interface JsonLoader
{
    /**
     * Load all data from json files.
     */
    void load (JSONObject json);
}
