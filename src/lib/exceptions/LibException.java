package lib.exceptions;

/**
 * Created by Robin on 18/05/2017.
 */
public class LibException extends Exception
{
    public LibException ()
    {
        super();
    }

    public LibException (int errorCode, String errorMessage)
    {
        super(errorCode, errorMessage);
    }

    public LibException (int errorCode)
    {
        super(errorCode);
    }

    public LibException (String errorMessage)
    {
        super(errorMessage);
    }
}
