package tbs.utils.Results;

import tbs.utils.AOP.authorize.interfaces.IPermissionVerification;
import tbs.utils.AOP.controller.IAction;
import tbs.utils.error.NetError;

public class NetResult<T> {
    public static enum MethodType {
        Immediately("即时响应", 1), AsynchronousDelay("异步延迟", 2);
        private String str;
        private int value;

        MethodType(String str, int value) {
            this.str = str;
            this.value = value;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }


    public static class AsyncDelayResult {
        public static final String RUNNING="运行中",DONE="完成";
        private String invokePath;

        private String status=RUNNING;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public AsyncDelayResult(String invokePath) {
            this.invokePath = invokePath;
        }

        public String getInvokePath() {
            return invokePath;
        }

        public void setInvokePath(String invokePath) {
            this.invokePath = invokePath;
        }

        Object data;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    private MethodType methodType = MethodType.Immediately;

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    private long cost = 0L, code = SUCCESS;
    private T data = null;
    private String message = "";

    private String invokeToken = "匿名用户";

    private IPermissionVerification.VerificationConclusion authType = IPermissionVerification.VerificationConclusion.NO_NEED;

    public IPermissionVerification.VerificationConclusion getAuthType() {
        return authType;
    }

    public void setAuthType(IPermissionVerification.VerificationConclusion authType) {
        this.authType = authType;
    }

    public String getInvokeToken() {
        return invokeToken;
    }

    public void setInvokeToken(String invokeToken) {
        this.invokeToken = invokeToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final long SUCCESS = 40000L, LIMITED_ACCESS = 39999L, Unchecked_Exception = 40001L;

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
