package lib.util;

/**
 * Created by Robin on 12/05/2017.<br>
 * This interface allows to pass a method to a method.
 */
public interface Closure
{
    /**
     * The method that will be runned later
     */
    void call();
}
