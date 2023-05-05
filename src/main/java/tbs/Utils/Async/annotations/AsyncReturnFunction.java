package tbs.utils.Async.annotations;

import java.lang.annotation.*;

/**
 * @author abstergo
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncReturnFunction {
}
