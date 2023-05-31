package tbs.utils.socket;

import tbs.utils.socket.model.SocketReceiveMessage;


import javax.websocket.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public interface ISocketManager {

    public static final int DUPLICATEKEY=0,SUCCESS=1,EMPTYKEY=2,SEND_FAIL=3;
    static interface MessageEvent
    {
        void consume(SocketReceiveMessage receiveMessage);
    }
    Session getSockets(String key);

    Enumeration<String> all();
    void remove(String key);

    int putSocket(String key, Session socket);

    int sendMessage(Object data,String key,String service);

    void ApplyonMessageEvent(String service,MessageEvent ... events);

    void resetEvent(String service,MessageEvent... events);

    List<MessageEvent> getEvents(String service);
}
