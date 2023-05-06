package tbs.dao.impl;

import tbs.utils.sql.SQL_Tool;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

public class SQL_QueryImpl {
    public String query(Object qo, Page page, Sortable sortable) throws Exception {
        return SQL_Tool.query(qo, page, sortable);
    }
}
