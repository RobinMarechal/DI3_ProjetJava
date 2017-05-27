package application.lib.exceptions;

/**
 * Created by Robin on 18/05/2017.
 */
public class ControllerException extends Exception
{
    public ControllerException ()
    {
        super();
    }

    public ControllerException (int errorCode, String errorMessage)
    {
        super(errorCode, errorMessage);
    }

    public ControllerException (int errorCode)
    {
        super(errorCode);
    }

    public ControllerException (String errorMessage)
    {
        super(errorMessage);
    }
}
