package application.lib.exceptions.codes;

/**
 * Created by Robin on 18/05/2017.<br/>
 * Exception codes related to {@link application.models.Employee} model class
 */
public final class EmployeeCodes
{
    /** The employee has no check at a specific date */
    public static final int NO_CHECK_THIS_DATE = 1;

    /** The employee has no check-in at a specific date */
    public static final int NO_CHECK_IN_THIS_DATE = 2;

    /** The employee has no check-out at a specific date */
    public static final int NO_CHECK_OUT_THIS_DATE = 3;
}