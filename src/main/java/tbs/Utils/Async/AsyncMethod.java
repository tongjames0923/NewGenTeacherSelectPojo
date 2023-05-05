package tbs.utils.Async;

import tbs.utils.AOP.controller.IAction;
import tbs.utils.Results.NetResult;
import tbs.utils.error.NetError;

public interface AsyncMethod {
    String resultKey();

    NetResult process(NetResult result, ThreadUtil threadUtil, IAction action);

    boolean post(String key, Object o);

    Object get(String key) throws Exception;
}
