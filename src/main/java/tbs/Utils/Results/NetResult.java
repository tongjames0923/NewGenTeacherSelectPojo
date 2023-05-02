package tbs.utils.Results;

import tbs.utils.AOP.authorize.error.AuthorizationFailureException;

public class NetResult<T> {
    private long cost=0L,code=SUCCESS;
    private T data=null;
    private String message="";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final long SUCCESS=40000L,LIMITED_ACCESS=39999L,Unchecked_Exception=40001L;
    public static interface IAction<T>
    {
        T action() throws NetError;
        default String msg()
        {
            return "invoke success";
        }
    }

    public static class NetError extends Exception
    {
        private long code;

        public NetError(String message, long code) {
            super(message);
            this.code = code;
        }

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            this.code = code;
        }
    }

    public static<T> NetResult<T> makeResult(IAction<T> action)
    {
        if(action==null) {
            return null;
        }
        NetResult<T> result=new NetResult<>();
        long beg=System.currentTimeMillis();
        T data=null;
        try {
            data= action.action();
            result.setMessage(action.msg());
        }
        catch (NetError error)
        {
            result.setCode(error.getCode());
            result.setMessage(error.getMessage());
        }
        result.setData(data);
        long end=System.currentTimeMillis();
        result.setCost(end-beg);
        return result;
    }

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
