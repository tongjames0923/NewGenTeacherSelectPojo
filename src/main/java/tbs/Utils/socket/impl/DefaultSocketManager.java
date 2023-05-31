package tbs.utils.socket.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tbs.utils.socket.ISocketManager;
import tbs.utils.socket.model.SocketSendMessage;


import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DefaultSocketManager implements ISocketManager {
    ConcurrentHashMap<String, Session> socketPool = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<MessageEvent>> eventsMap = new ConcurrentHashMap<>();


    @Override
    public Session getSockets(String key) {
        if (socketPool.containsKey(key))
            return socketPool.get(key);
        return null;
    }

    @Override
    public Enumeration<String> all() {
        return socketPool.keys();
    }

    @Override
    public void remove(String key) {
        socketPool.remove(key);
    }

    @Override
    public int putSocket(String key, Session socket) {
        if (socketPool.containsKey(key)) {
            log.debug("SOCKET 重复KEY " + key);
            return DUPLICATEKEY;
        }

        socketPool.put(key, socket);
        return SUCCESS;
    }

    @Override
    public int sendMessage(Object data, String key, String service) {
        if (!socketPool.containsKey(key)) {
            log.debug("not exsit key for " + key);
            return EMPTYKEY;
        }
        try {
            Session socket = socketPool.get(key);
            SocketSendMessage sendMessage = new SocketSendMessage();
            sendMessage.setSendTime(new Date());
            sendMessage.setKey(key);
            sendMessage.setData(data);
            socket.getBasicRemote().sendText(JSON.toJSONString(sendMessage));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            socketPool.remove(key);
            return SEND_FAIL;
        }
        return SUCCESS;
    }

    @Override
    public void ApplyonMessageEvent(String service, MessageEvent... events) {
        if (eventsMap.containsKey(service)) {
            List ls = eventsMap.getOrDefault(service, new LinkedList<>());
            for (MessageEvent me : events) {
                ls.add(me);
            }
            eventsMap.put(service, ls);
        } else {
            resetEvent(service, events);
        }
    }

    @Override
    public void resetEvent(String service, MessageEvent... events) {
        List<MessageEvent> msgs = new LinkedList<>();
        for (MessageEvent m : events) {
            msgs.add(m);
        }
        eventsMap.put(service, msgs);
    }

    @Override
    public List<MessageEvent> getEvents(String service) {
        if (eventsMap.containsKey(service))
            return eventsMap.get(service);
        return new ArrayList<>();
    }
}
