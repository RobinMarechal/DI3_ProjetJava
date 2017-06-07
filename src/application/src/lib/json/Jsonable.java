package lib.json;

import org.json.simple.JSONObject;

/**
 * Created by Robin on 11/04/2017.<br>
 * The class that implements this interface should provide a way to save it's data into {@link JSONObject}
 */
public interface Jsonable
{
    /**
     * Creates an instance of {@link JSONObject} from the class instance data.
     *
     * @return the json object containing the class instance data.
     */
    JSONObject toJson ();
}
