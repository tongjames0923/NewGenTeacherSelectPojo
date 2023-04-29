package tbs.Utils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Component
public class BatchUtil {
    private static final int STATUS_SAVED = -1;
    @Resource
    SqlSessionFactory sqlSessionFactory;

    private SqlSession session = null;

    void getSession() {
        if (session == null)
            session = sqlSessionFactory.openSession(ExecutorType.BATCH);
    }

    void commitSession() {
        if (session != null)
            session.commit();
    }

    void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }
    void rollback()
    {
        if(session!=null)
        {
            session.rollback();
        }
    }
    public interface SqlUpdateExecute {

       public void execute();
    }
    public interface SqlSelectExecute
    {
        public <T> T select();
    }

    public  <T> T getMapper(Class<T> m)
    {
        if(session!=null)
            return session.getMapper(m);
        return null;
    }
    public static interface ExecuteCondition {
        boolean willExecute(int needExe);
    }

    List<SqlUpdateExecute> needExxcutes = new LinkedList<>();


    public static class DefaultNumberLimitCondition implements ExecuteCondition
    {
        int n=0;
        public DefaultNumberLimitCondition(int n)
        {
            this.n=n;
        }

        @Override
        public boolean willExecute(int needExe) {
            return needExe>=n;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }
    }
    public static class SQL_EXXECUTE_FAIL_ERROR extends Exception {
        List<Exception> exceptionList=new LinkedList<>();

        public List<Exception> getExceptionList() {
            return exceptionList;
        }

        public void setExceptionList(List<Exception> exceptionList) {
            this.exceptionList = exceptionList;
        }
        @Override
        public void printStackTrace() {
            for(Exception e:exceptionList)
            {
                e.printStackTrace();
            }
        }
    }

    private int execute() throws SQL_EXXECUTE_FAIL_ERROR {
        SQL_EXXECUTE_FAIL_ERROR ex=null;
        try {
            getSession();
            for(SqlUpdateExecute q:needExxcutes)
            {
                try {
                    q.execute();
                }catch (Exception e)
                {
                    if(ex==null)
                        ex=new SQL_EXXECUTE_FAIL_ERROR();
                    ex.getExceptionList().add(e);
                }
            }
            commitSession();
        }catch (Exception e)
        {
            if(ex==null)
                ex=new SQL_EXXECUTE_FAIL_ERROR();
            ex.getExceptionList().add(e);
            rollback();
        }finally {
            close();
        }
        if(ex!=null)
            throw ex;
        int cnt= needExxcutes.size();
        needExxcutes.clear();
        return cnt;
    }
    DefaultNumberLimitCondition defaultNumberLimitCondition=new DefaultNumberLimitCondition(5);
    public int batchUpdate(int maxSIZE, SqlUpdateExecute... executes) throws SQL_EXXECUTE_FAIL_ERROR {
        defaultNumberLimitCondition.setN(maxSIZE);
        return batchUpdate(defaultNumberLimitCondition,executes);
    }
    public int batchUpdate(ExecuteCondition executeCondition, SqlUpdateExecute... executes) throws SQL_EXXECUTE_FAIL_ERROR {
        for (SqlUpdateExecute execute : executes) {
            if (execute != null)
                needExxcutes.add(execute);
        }
        boolean flag=false;
        if(executeCondition!=null)
            flag=executeCondition.willExecute(needExxcutes.size());
        if(!flag)
            return STATUS_SAVED;
        return execute();
    }

    public <T> List<T> select(SqlSelectExecute... selectExecutes)
    {
        List<T> result=new LinkedList<>();


        return result;
    }

}
