package tbs.utils.Results;

import tbs.utils.AOP.controller.IAction;
import tbs.utils.error.NetError;

public class NetResult<T> {
    private long cost=0L,code=SUCCESS;
    private T data=null;
    private String message="";

    String invokeToken="";

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

    public static final long SUCCESS=40000L,LIMITED_ACCESS=39999L,Unchecked_Exception=40001L;

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
