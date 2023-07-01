package tbs.utils.socket.impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import tbs.utils.socket.ISocketClient;
import tbs.utils.socket.model.SocketSendMessage;

import javax.websocket.Session;
import java.util.Date;

@Slf4j
public class DefaultSocketClient implements ISocketClient {

    private String k;
    private Session session;

    public DefaultSocketClient(String key, Session s) {
        this.k = key;
        this.session = s;
    }

    @Override
    public String key() {
        return k;
    }

    @Override
    public void send(Object data, String service) {
        try {
            SocketSendMessage sendMessage = new SocketSendMessage();
            sendMessage.setSendTime(new Date());
            sendMessage.setKey(this.k);
            sendMessage.setService(service);
            sendMessage.setData(data);
            session.getBasicRemote().sendText(JSON.toJSONString(sendMessage));
        } catch (Exception e) {
            log.error("发送消息错误", e);
        }
    }
}
