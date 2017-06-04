package application.lib.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 18/05/2017.
 */
public abstract class Exception extends java.lang.Exception
{
    protected Map<String, Object> data;
    protected int errorCode = 0;

    public Exception ()
    {
        this(0, "Exception");
    }

    public Exception (int errorCode, String errorMessage)
    {
        super(errorMessage);
        data = new HashMap<>();
        this.errorCode = errorCode;
    }

    public Exception(int errorCode)
    {
        this(errorCode, "Exception");
    }

    public Exception (String errorMessage)
    {
        this(0, errorMessage);
    }

    public Map<String, Object> getData ()
    {
        return data;
    }

    public Exception set(String key, Object value)
    {
        data.put(key, value);
        return this;
    }

    public Object get(String key)
    {
        return data.get(key);
    }
}