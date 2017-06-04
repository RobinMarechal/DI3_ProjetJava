package application.lib.exceptions;

/**
 * Created by Robin on 18/05/2017.
 */
public class ModelException extends Exception
{
    public ModelException ()
    {
        super();
    }

    public ModelException (int errorCode)
    {
        super(errorCode);
    }

    public ModelException (int errorCode, String errorMessage)
    {
        super(errorCode, errorMessage);
    }

    public ModelException (String errorMessage)
    {
        super(errorMessage);
    }

    public void print ()
    {
        System.out.println("Exception has been thrown with code " + errorCode);
    }
}