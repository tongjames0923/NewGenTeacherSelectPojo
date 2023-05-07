package tbs.utils.AOP.authorize.model;

import lombok.ToString;
import tbs.utils.AOP.authorize.interfaces.IPermissionVerification;
import tbs.utils.Results.NetResult;

import java.util.Date;
@ToString
public class SystemExecutionData {
    private String invokeToken;
    private Date requestBeginTime;
    private Date requestEndTime;
    BaseRoleModel invokeRole;
    NetResult.MethodType methodType;
    IPermissionVerification.VerificationConclusion authType;

    public String getInvokeToken() {
        return invokeToken;
    }

    public void setInvokeToken(String invokeToken) {
        this.invokeToken = invokeToken;
    }

    public Date getRequestBeginTime() {
        return requestBeginTime;
    }

    public void setRequestBeginTime(Date requestBeginTime) {
        this.requestBeginTime = requestBeginTime;
    }

    public Date getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(Date requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public BaseRoleModel getInvokeRole() {
        return invokeRole;
    }

    public void setInvokeRole(BaseRoleModel invokeRole) {
        this.invokeRole = invokeRole;
    }

    public NetResult.MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(NetResult.MethodType methodType) {
        this.methodType = methodType;
    }

    public IPermissionVerification.VerificationConclusion getAuthType() {
        return authType;
    }

    public void setAuthType(IPermissionVerification.VerificationConclusion authType) {
        this.authType = authType;
    }
}
