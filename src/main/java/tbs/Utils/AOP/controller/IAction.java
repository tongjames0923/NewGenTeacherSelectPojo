package tbs.utils.AOP.controller;

import tbs.utils.Results.NetResult;

public interface IAction<T> {
    T action(NetResult result) throws Throwable;

    default String msg() {
        return "invoke success";
    }
}
