package tbs.utils.Async.interfaces;

public interface ILockProxy {
    interface FunctionWithThrows<P,R>
    {
        R apply(P param) throws Throwable;
    }
    public Object run(FunctionWithThrows<ILocker,Object> function, String lockName) throws Throwable;

    public ILocker getLocker();
    public void setLocker(ILocker locker);
}
