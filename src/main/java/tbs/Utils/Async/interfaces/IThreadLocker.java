package tbs.Utils.Async.interfaces;

import java.util.List;

public interface IThreadLocker {
    boolean isLock(IThreadSign sign);

    void lock(IThreadSign sign);

    void unlock(IThreadSign sign);

    <T> void putObject(IThreadSign sign,T obj);

    public <T> T getObject(IThreadSign sign,Class<? extends T> clas) ;

    public <T> List<? extends T> getList(IThreadSign sign, Class<? extends T> clas) ;
}
