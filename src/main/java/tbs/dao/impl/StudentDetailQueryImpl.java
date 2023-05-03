package tbs.dao.impl;

import tbs.dao.QO.StudentQO;
import tbs.utils.sql.SQL_Tool;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

public class StudentDetailQueryImpl {
    public String query(StudentQO qo, Page page, Sortable sortable) throws Exception {
        return SQL_Tool.query(StudentQO.class, qo, page, sortable);
    }
}
