package tbs.utils.Async.interfaces;

public interface ILocker {
    boolean isLock(IThreadSign sign);

    void lock(IThreadSign sign);

    void unlock(IThreadSign sign);
}
