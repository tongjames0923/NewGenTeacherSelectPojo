package tbs.utils.socket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import tbs.framework.config.BeanConfig;
import tbs.framework.interfaces.IAccess;
import tbs.framework.model.BaseRoleModel;
import tbs.utils.socket.ISocketClient;
import tbs.utils.socket.ISocketManager;
import tbs.utils.socket.ISocketWorker;
import tbs.utils.socket.model.SocketReceiveMessage;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Scope("prototype")
@Component
public abstract class BasicSocketWorker implements ISocketWorker {

    private IAccess access = BeanConfig.getInstance().getAccessElement();

    public String SERVICE_NAME = "";
    @Resource
    private ISocketManager socketManager = null;

    private boolean isInit = false;
    private Set<String> keys = new LinkedHashSet<>();

    protected ISocketManager getSocketManager() {
        return socketManager;
    }

    public BasicSocketWorker(String name) {
        SERVICE_NAME = name;
    }
    public BasicSocketWorker() {

    }

    private void init() {
        if (!isInit) {
            List<ISocketManager.MessageEvent> ls = messageEvents();
            ISocketManager.MessageEvent[] mes = new ISocketManager.MessageEvent[ls.size()];
            for (int i = 0; i < ls.size(); i++) {
                mes[i] = ls.get(i);
            }
            socketManager.ApplyonMessageEvent(SERVICE_NAME, mes);
            keys.clear();
            isInit = true;
        }
    }

    public abstract void customAccept(ISocketClient client, BaseRoleModel roleModel) throws Exception;

    @Override
    public void accept(ISocketClient client) throws Exception {
        init();
        BaseRoleModel baseRoleModel = access.readRole(client.key());
        if (baseRoleModel == null) {
            throw new Exception("不存在用户权限");
        }
        if (socketManager.getSockets(client.key()) != null)
            throw new Exception("重复连接");
        customAccept(client, baseRoleModel);
        socketManager.putSocket(client);
        keys.add(client.key());
        log.debug("connected for {}.key :{}", serviceName(), client.key());
    }

    @Override
    public void onClose(ISocketClient client) {
        keys.remove(client.key());
        socketManager.remove(client.key());
        log.debug("disconnected for {}.key :{}", serviceName(), client.key());
    }

    @Override
    public void onError(Throwable e) {
        log.error("SOCKET ERROR ", e);
    }

    @Override
    public boolean messageEventFiltering(SocketReceiveMessage message) {
        return !SERVICE_NAME.equals(message.getService());
    }

    @Override
    public String serviceName() {
        return SERVICE_NAME;
    }
}
