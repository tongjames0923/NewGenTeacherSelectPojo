package tbs.utils.AOP.authorize.annotations;

import tbs.utils.AOP.authorize.impls.DefaultAccessImpl;
import tbs.utils.AOP.authorize.interfaces.IAccess;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.lang.annotation.*;

/**
 * @author abstergo
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessRequire {
     String requireField() default "X-TOKEN";
     int[] manual() default {};
}
