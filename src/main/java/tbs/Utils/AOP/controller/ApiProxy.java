package tbs.utils.AOP.controller;

import tbs.utils.AOP.authorize.model.BaseRoleModel;
import tbs.utils.Results.NetResult;
import tbs.utils.error.NetError;

public interface ApiProxy {


    NetResult method(IAction  action,NetResult result);

}
