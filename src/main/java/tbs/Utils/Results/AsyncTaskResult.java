package tbs.utils.Results;

import lombok.extern.slf4j.Slf4j;
import tbs.utils.Async.interfaces.IThreadLocker;
import tbs.utils.Async.interfaces.IThreadSign;

import java.util.List;

@Slf4j
public class AsyncTaskResult {
    private IThreadSign sign;
    private IThreadLocker locker;

    private long STATUS=DOING;
    private Exception exception;

    public IThreadLocker getLocker() {
        return locker;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public static final long DOING=1,DONE=2,ERROR=3;

    public long getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(long STATUS) {
        this.STATUS = STATUS;
        if(changeEvent!=null) {
            try {
                changeEvent.changed(this,IDataChanged.STATUS_CHANGED);
            }catch (Exception e)
            {
                log.error("调用异步状态监听异常",e);
            }
        }
    }

    public static interface IDataChanged
    {
        public static final long STATUS_CHANGED=-1,DATA_CHANGED=-2;
        void changed(AsyncTaskResult self, long whatChanged);
    }

    private IDataChanged changeEvent=null;

    public void setChangeEvent(IDataChanged changeEvent) {
        this.changeEvent = changeEvent;
    }

    public AsyncTaskResult(IThreadLocker locker, IThreadSign sign)
    {
        this.sign=sign;
        this.locker=locker;
    }
    public IThreadSign getSign()
    {
        return sign;
    }
    public <T> T getAsyncData(Class<? extends T> clas)
    {
        return locker.getObject(sign,clas);
    }
    public <T> List< T> getAsyncListData(Class<T> clas)
    {
        return locker.getList(sign,clas);
    }
    public boolean putAsyncData(Object data)
    {
        if(locker.isLock(sign))
        {
            locker.putObject(sign,data);
            if(changeEvent!=null) {
                try {
                    changeEvent.changed(this,IDataChanged.DATA_CHANGED);
                }catch (Exception e)
                {
                    log.error("调用异步状态监听异常",e);
                }
            }
            return true;
        }
        return false;
    }
}