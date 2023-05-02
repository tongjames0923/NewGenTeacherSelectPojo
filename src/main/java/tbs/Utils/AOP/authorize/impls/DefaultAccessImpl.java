package tbs.utils.AOP.authorize.impls;

import com.alibaba.fastjson.JSON;
import tbs.utils.AOP.authorize.interfaces.IAccess;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultAccessImpl implements IAccess {

    ConcurrentMap<String, Object> tokenMap = new ConcurrentHashMap<>();

    @Override
    public BaseRoleModel readRole(String tokenStr) {
        BaseRoleModel baseRoleModel = new BaseRoleModel();
        baseRoleModel.setRoleCode(-1);
        baseRoleModel.setRoleName("常绿");
        return baseRoleModel;
    }

    @Override
    public List<BaseRoleModel> granded(Method method) {
        BaseRoleModel baseRoleModel = new BaseRoleModel();
        baseRoleModel.setRoleCode(-1);
        baseRoleModel.setRoleName("常绿");
        return Arrays.asList(baseRoleModel);
    }

    @Override
    public List<BaseRoleModel> grandedManual(int[] manuals) {
        return null;
    }

    @Override
    public void put(String token, BaseRoleModel detail) {
        tokenMap.put(token, detail);
    }


    @Override
    public void deleteToken(String token) {
        if (tokenMap.containsKey(token)) {
            tokenMap.remove(token);
        }
    }
}
