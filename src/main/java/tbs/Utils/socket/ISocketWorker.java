package tbs.utils.socket;

import tbs.utils.socket.model.SocketReceiveMessage;

import java.util.List;

public interface ISocketWorker extends ISocketEvent {

    void sendMessage(Object data);
    String serviceName();
    List<ISocketManager.MessageEvent> messageEvents();

    boolean messageEventFiltering(SocketReceiveMessage message);
}
