package tbs.utils.AOP.controller;

import tbs.utils.Results.NetResult;

public interface ApiProxy {


    NetResult method( IAction  action, NetResult result);

}
