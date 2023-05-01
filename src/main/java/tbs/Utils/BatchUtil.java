package tbs.utils;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author abstergo
 */
@Component
@Scope("prototype")
public class BatchUtil {
    private static final int STATUS_SAVED = -1;
    @Resource
    SqlSessionFactory sqlSessionFactory;

    private SqlSession session = null;

    void getSession() {
        if (session == null) {
            session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        }
    }

    void commitSession() {
        if (session != null) {
            session.commit();
        }
    }

    void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    void rollback() {
        if (session != null) {
            session.rollback();
        }
    }

    public interface SqlUpdateExecute {

        /**
         * 使用本工具的getMapper方法获取mapper并执行 insert,udpate,delete操作
         */
        public void execute();
    }

    public interface SqlSelectExecute {
        /**
         * 使用本工具的getMapper方法获取mapper并执行select操作
         *
         * @param <T>
         * @return
         */
        public <T> List<T> select();
    }

    public <T> T getMapper(Class<T> m) {
        if (session != null) {
            return session.getMapper(m);
        }
        return null;
    }

    public static interface ExecuteCondition {
        /**
         * 判断是否哦需要执行
         *
         * @param needExe 当前待执行的数量
         * @return true，立即执行，false 暂存
         */
        boolean willExecute(int needExe);
    }

    Queue<SqlUpdateExecute> needExxcutes = new ConcurrentLinkedQueue<SqlUpdateExecute>();


    public static class DefaultNumberLimitCondition implements ExecuteCondition {
        int n = 0;

        public DefaultNumberLimitCondition(int n) {
            this.n = n;
        }

        @Override
        public boolean willExecute(int needExe) {
            return needExe >= n;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }
    }

    public static class SqlExecuteListException extends Exception {
        List<Exception> exceptionList = new LinkedList<>();

        public List<Exception> getExceptionList() {
            return exceptionList;
        }

        public void setExceptionList(List<Exception> exceptionList) {
            this.exceptionList = exceptionList;
        }

        @Override
        public void printStackTrace() {
            for (Exception e : exceptionList) {
                e.printStackTrace();
            }
        }
    }

    private int execute() throws SqlExecuteListException {
        SqlExecuteListException ex = null;
        try {
            getSession();
            for (SqlUpdateExecute q : needExxcutes) {
                try {
                    q.execute();
                } catch (Exception e) {
                    if (ex == null) {
                        ex = new SqlExecuteListException();
                    }
                    ex.getExceptionList().add(e);
                }
            }
            commitSession();
        } catch (Exception e) {
            if (ex == null) {
                ex = new SqlExecuteListException();
            }
            ex.getExceptionList().add(e);
            rollback();
        } finally {
            close();
        }
        if (ex != null) {
            throw ex;
        }
        int cnt = needExxcutes.size();
        needExxcutes.clear();
        return cnt;
    }

    DefaultNumberLimitCondition defaultNumberLimitCondition = new DefaultNumberLimitCondition(5);

    public int batchUpdate(int maxSize, SqlUpdateExecute... executes) throws SqlExecuteListException {
        defaultNumberLimitCondition.setN(maxSize);
        return batchUpdate(defaultNumberLimitCondition, executes);
    }

    public int flush() throws SqlExecuteListException {
        return execute();
    }

    public int batchUpdate(ExecuteCondition executeCondition, SqlUpdateExecute... executes) throws SqlExecuteListException {
        for (SqlUpdateExecute execute : executes) {
            if (execute != null) {
                needExxcutes.add(execute);
            }
        }
        boolean flag = false;
        if (executeCondition != null) {
            flag = executeCondition.willExecute(needExxcutes.size());
        }
        if (!flag) {
            return STATUS_SAVED;
        }
        return execute();
    }

    public <T> List<T> select(SqlSelectExecute... selectExecutes) {
        List<T> result = new LinkedList<>();

        return result;
    }

}
