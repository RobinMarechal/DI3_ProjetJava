package lib;

import java.io.File;
import java.io.IOException;

/**
 * Created by Robin on 03/05/2017.<br>
 * Helper class containing helper methods
 */
public class Helper
{

    /**
     * Get the path of the configuration file for the server
     * @return the path of the configuration file for the server
     * @throws IOException
     */
    public static String serverConfigFilePath () throws IOException
    {
        return new File(".").getCanonicalPath() + "/data/config/serverConfig.json";
    }

    /**
     * Get the path of the serialization file of the company
     * @return the path of the serialization file of the company
     * @throws IOException
     */
    public static String serializationFilePath () throws IOException
    {
        return new File(".").getCanonicalPath() + "/data/serialized/company.ser";
    }
}
