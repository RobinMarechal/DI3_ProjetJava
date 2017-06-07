package lib.annotations;

import java.lang.annotation.*;

/**
 * Created by Robin on 04/06/2017. <br>
 * Used to specify what fields a form should contain fields key
 */
@Documented
@Retention (RetentionPolicy.SOURCE)
@Target ({
        ElementType.METHOD,
        ElementType.PARAMETER
})
public @interface FormContains
{

    /**
     * The fields name key
     * @return an array containing all the field's name
     */
    String[] fields ();
}
