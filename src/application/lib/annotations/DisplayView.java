package application.lib.annotations;

import java.lang.annotation.*;

/**
 * Created by Robin on 04/06/2017. <br/>
 * Allow to specify that a method's role is to display a view
 */
@Documented
@Retention (RetentionPolicy.SOURCE)
@Target ({ElementType.METHOD})
public @interface DisplayView
{}
