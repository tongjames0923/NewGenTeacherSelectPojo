package tbs.utils.sql.query;

import java.lang.annotation.*;

/**
 * @author abstergo
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Queryable {
    String value();
}
