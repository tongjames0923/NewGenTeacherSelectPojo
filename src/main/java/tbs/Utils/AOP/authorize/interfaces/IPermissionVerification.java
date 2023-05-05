package tbs.utils.AOP.authorize.interfaces;

import tbs.utils.AOP.authorize.error.AuthorizationFailureException;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.util.HashMap;
import java.util.Map;

public interface IPermissionVerification {
    public static enum VerificationConclusion
    {
        UnAuthorized("未授权"),Authorized("高级别授权"),EQUAL("匹配授权"),NO_NEED("无需授权");
        private String value;

        public String getValue() {
            return value;
        }

        VerificationConclusion(String value) {
            this.value = value;
        }
    }
    VerificationConclusion accessCheck(BaseRoleModel user, Map<Integer, BaseRoleModel> needRoleMap) throws AuthorizationFailureException;

}
