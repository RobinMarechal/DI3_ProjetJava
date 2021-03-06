package lib.json;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Robin on 10/04/2017.<br>
 * The class that implements this interface should provide a way to save it's {@link JSONObject} data into a file
 */
public interface JsonSaver
{
    /**
     * Save the data of a class instance into a json file
     */
    void save ();


    /**
     * Save the data of a class instance into a json file
     *
     * @param path the file directory
     * @param filename the filename (including extension)
     * @param json the json instance to write in the file
     */
    default void saveJsonToFile (@NotNull String path, @NotNull String filename, @NotNull JSONObject json)
    {
        File f = new File(path);

        if(!f.exists())
        {
            f.mkdirs();
        }

        try(FileWriter file = new FileWriter(path + "\\" + filename))
        {
            file.write(json.toJSONString());
            file.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
