package application.lib.json;

import org.json.simple.JSONObject;

/**
 * Created by Robin on 11/04/2017.
 */
public interface Jsonable
{
    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    public JSONObject toJson ();
}
