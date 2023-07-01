package tbs.utils.sql.impl;

import tbs.framework.sql.query.Page;
import tbs.framework.sql.query.Sortable;

public class SQL_QueryImpl {

    private static String sortableStr(Sortable sortable, boolean leftSign) {
        StringBuilder sb = new StringBuilder();
        if (leftSign)
            sb.append(",");
        sb.append("'");
        sb.append(sortable.getField());
        sb.append("' ");
        sb.append(sortable.getDir());
        return sb.toString();
    }

    public static String PageingQuery(Page page, Sortable... sortable) {
        StringBuilder sb = new StringBuilder();
        if (sortable != null && sortable.length > 0) {
            sb.append(" ORDER BY ");
            sb.append(sortableStr( sortable[0],false));
            for (int i=1;i<sortable.length;i++)
            {
                sb.append(sortableStr(sortable[i],true));
            }
        }
        if(page!=null)
        {
            sb.append(" "+page.makeSql());
        }
        return sb.toString();
    }

}
