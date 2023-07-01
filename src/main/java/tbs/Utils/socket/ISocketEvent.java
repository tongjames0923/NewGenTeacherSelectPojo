package tbs.utils.socket;

public interface ISocketEvent {
    void accept(ISocketClient client) throws Exception;
    void onClose(ISocketClient socketClient);
    void onError(Throwable e);
}
