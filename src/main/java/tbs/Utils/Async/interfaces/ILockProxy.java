package tbs.utils.Async.interfaces;

import java.util.function.Function;

public interface ILockProxy {
    public Object run(Function< ILocker,Object> f,String lockName);
}