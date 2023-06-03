package tbs.dao.impl;

import tbs.utils.sql.SQL_Tool;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

public class SQL_QueryImpl {
    public static final String QUERY="query";
    public String query(Object qo) throws Exception {
        return SQL_Tool.query(qo, null);
    }
    public String queryWithPage(Object qo, Page page) throws Exception {
        return SQL_Tool.query(qo, page);
    }
}
