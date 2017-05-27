package application.lib.exceptions;

/**
 * Created by Robin on 18/05/2017.
 */
public class ViewException extends Exception
{
    public ViewException ()
    {
        super();
    }

    public ViewException (int errorCode, String errorMessage)
    {
        super(errorCode, errorMessage);
    }

    public ViewException (int errorCode)
    {
        super(errorCode);
    }

    public ViewException (String errorMessage)
    {
        super(errorMessage);
    }
}
