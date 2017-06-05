package application.lib.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 18/05/2017.<br/>
 * Custom Exception class allowing to pass precise information with an exception
 */
public abstract class Exception extends java.lang.Exception
{
    /** A map containing {@link java.lang.Object} information associated to a {@link java.lang.String} key */
    protected Map<String, Object> data;

    /** The error code */
    protected int errorCode = 0;

    /** Default constructor */
    public Exception ()
    {
        this(0, "Exception");
    }

    /**
     * 2 arguments constructor
     *
     * @param errorCode    the error code of the exception
     * @param errorMessage the error message
     */
    public Exception (int errorCode, String errorMessage)
    {
        super(errorMessage);
        data = new HashMap<>();
        this.errorCode = errorCode;
    }

    /**
     * Error code constructeur
     *
     * @param errorCode the error code
     */
    public Exception (int errorCode)
    {
        this(errorCode, "Exception");
    }

    /**
     * Error message contructor
     *
     * @param errorMessage the error message
     */
    public Exception (String errorMessage)
    {
        this(0, errorMessage);
    }

    /**
     * Get the data map
     *
     * @return the data map
     */
    public Map<String, Object> getData ()
    {
        return data;
    }

    /**
     * Add an precise information to the exception
     *
     * @param key   The key
     * @param value The value of the information
     * @return this
     */
    public Exception set (String key, Object value)
    {
        data.put(key, value);
        return this;
    }

    /**
     * Get a information of the exception by a key
     *
     * @param key the key
     * @return the information object
     */
    public Object get (String key)
    {
        return data.get(key);
    }


}