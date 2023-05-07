package tbs.utils.sql.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlField {

    boolean isPrimary()default false;
    String field() default "";
    int index()default 0;
}
