package tbs.utils.sql.impl;

import org.springframework.util.CollectionUtils;
import tbs.utils.sql.SQL_Tool;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

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
