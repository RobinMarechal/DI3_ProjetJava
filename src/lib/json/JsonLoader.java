package lib.json;

import org.json.simple.JSONObject;

/**
 * Created by Robin on 11/04/2017.
 */
public interface JsonLoader
{
    /**
     * Load all data from json files.
     */
    public void load (JSONObject json);
}
