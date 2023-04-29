package tbs.Utils.Async;

public interface IThreadLocker {
    boolean isLock(IThreadSign sign);

    void lock(IThreadSign sign);

    void unlock(IThreadSign sign);
}
