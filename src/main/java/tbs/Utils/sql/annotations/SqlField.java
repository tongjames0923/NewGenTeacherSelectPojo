package tbs.utils.sql.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlField {


    public static final int SORT_NOT = 0, SORT_DESC = -1, SORT_ASC = 1;

    boolean isPrimary() default false;

    String field() default "";

    int index() default 0;

    int sortable() default SORT_NOT;

}
