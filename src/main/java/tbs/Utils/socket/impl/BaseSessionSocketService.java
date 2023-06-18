package tbs.utils.socket.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import tbs.utils.socket.ISocketClient;
import tbs.utils.socket.ISocketManager;
import tbs.utils.socket.ISocketWorker;
import tbs.utils.socket.model.SocketReceiveMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class BaseSessionSocketService {
    public ISocketWorker WORKER = null;

    private static ConcurrentHashMap<String, ISocketWorker> workers = new ConcurrentHashMap<>();

    public abstract String serviceName();

    private void init() {
        if (!workers.containsKey(serviceName())) {
            WORKER = SpringUtil.getBean(serviceName());
            workers.put(serviceName(), WORKER);
        } else {
            WORKER = workers.get(serviceName());
        }
    }

    public static void send(String service, Object data) {
        if (workers.containsKey(service)) {
            workers.get(service).sendMessage(data);
            log.debug("send to {} with :{}", service, JSON.toJSONString(data));
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("key") String key) {
        init();
        try {
            ISocketClient client = new DefaultSocketClient(key, session);
            WORKER.accept(client);
        } catch (Exception e) {
            log.error("验证失败," + e.getMessage(), e);
            try {
                session.close();
            } catch (Exception ex) {

            }
        }
    }

    @OnClose
    public void onClose(@PathParam("key") String key) {
        init();
        WORKER.onClose(new DefaultSocketClient(key, null));
    }


    @OnError
    public void error(Throwable exception) {
        init();
        WORKER.onError(exception);
    }

    @OnMessage
    public void onMessage(String message) {
        init();
        try {
            SocketReceiveMessage receiveMessage = JSON.parseObject(message, SocketReceiveMessage.class);
            if (!WORKER.messageEventFiltering(receiveMessage))
                for (ISocketManager.MessageEvent event : WORKER.messageEvents()) {
                    event.consume(receiveMessage);
                }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
