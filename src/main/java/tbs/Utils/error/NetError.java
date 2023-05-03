package tbs.utils.error;

public class NetError extends Exception {
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
