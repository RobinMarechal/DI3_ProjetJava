package lib.json;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

/**
 * Created by Robin on 11/04/2017.<br>
 * The class that implements this interface should provide a way to load data from {@link JSONObject}
 */
public interface JsonLoader
{
    /**
     * Load all data from json files.
     * @param json the json containing the data to load
     */
    void load (@NotNull JSONObject json);
}
