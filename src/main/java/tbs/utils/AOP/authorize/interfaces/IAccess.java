package tbs.utils.AOP.authorize.interfaces;

import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author abstergo
 */
public interface IAccess {

    BaseRoleModel readRole(String tokenStr);

    List<BaseRoleModel> granded(Method method);

    List<BaseRoleModel> grandedManual(int[] manuals);

    void put(String token,BaseRoleModel detail)throws Exception;



    void deleteToken(String token);

}
