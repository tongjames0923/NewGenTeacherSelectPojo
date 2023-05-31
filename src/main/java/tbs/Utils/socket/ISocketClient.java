package tbs.utils.socket;

public interface ISocketClient {
    String key();
    void send(Object data,String service);
}
