package application.lib.exceptions;

/**
 * Created by Robin on 18/05/2017.<br/>
 * Custom exception class to handle model class {@link application.models.Employee} specific problems
 */
public class ModelException extends Exception
{
    /** Default constructor */
    public ModelException ()
    {
        super();
    }

    /**
     * Error code constructor
     *
     * @param errorCode the error code
     */
    public ModelException (int errorCode)
    {
        super(errorCode);
    }

    /**
     * 2 parameters constructors
     *
     * @param errorCode    the error code
     * @param errorMessage the error message
     */
    public ModelException (int errorCode, String errorMessage)
    {
        super(errorCode, errorMessage);
    }

    /**
     * Error message constructor
     *
     * @param errorMessage the error message
     */
    public ModelException (String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Print the exception message to the console with error code and error message
     */
    public void print ()
    {
        System.err.println("Exception has been thrown with code " + errorCode + " and message: \n\t-> " + getMessage());
    }
}