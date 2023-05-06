package tbs.utils.AOP.controller;

import lombok.extern.slf4j.Slf4j;
import tbs.utils.Results.NetResult;
public interface ApiProxy {


    NetResult method( IAction  action, NetResult result) throws Throwable;

}
