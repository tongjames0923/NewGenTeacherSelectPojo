package tbs.utils.sql;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tbs.utils.sql.annotations.Updateable;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.annotations.Queryable;
import tbs.utils.sql.query.Sortable;
import tbs.utils.sql.annotations.SqlField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SQL_Tool {


    private static class SqlFieldItem {
        private String field, value;
        private int index = 0;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    private static <Q> List<SqlFieldItem> qoToList(Q qo) throws IllegalAccessException {
        Class qClass = qo.getClass();
        Field[] fields = qClass.getDeclaredFields();
        List<SqlFieldItem> items = new ArrayList<>(fields.length);
        for (Field field : fields) {
            SqlField f = field.getAnnotation(SqlField.class);
            if (f == null) {
                continue;
            }
            field.setAccessible(true);
            SqlFieldItem fieldItem = new SqlFieldItem();
            fieldItem.setIndex(f.index());
            fieldItem.setField(StringUtils.isEmpty(f.field()) ? field.getName() : f.field());
            Object v = field.get(qo);
            if (v != null) {
                fieldItem.setValue(v.toString());
            }
            items.add(fieldItem);
        }
        items.sort((item1, item2) -> {
            return item1.index - item2.index;
        });
        return items;
    }

    public String rolesIn(List<Integer> list) {
        StringBuilder builder = new StringBuilder("SELECT r.roleid as roleCode,r.rolename as roleName from role r ");
        builder.append(listOr("r.roleid", list));
        return builder.toString();
    }

    public static <Q> String query( Q qo, Page page, Sortable sortable) throws Exception {
        Queryable queryable = qo.getClass().getDeclaredAnnotation(Queryable.class);
        if (queryable == null) {
            throw new Exception("not find queryable in class");
        }
        StringBuilder select = new StringBuilder(queryable.value() + " ");
        if (qo != null) {
            String find = "where ";
            List<SqlFieldItem> items = qoToList(qo);
            if (!CollectionUtils.isEmpty(items)) {
                SqlFieldItem item = items.get(0);
                String n = item.getField();
                String v = item.getValue();
                find += n + "='" + v + "' ";
                for (int i = 1; i < items.size(); i++) {
                    item = items.get(i);
                    n = item.getField();
                    v = item.getValue();
                    if (!StringUtils.isEmpty(v))
                        find += " and " + n + "='" + v + "' ";
                }
                select.append(find);
            }
        }
        if (sortable != null) {
            String sidx = " order by " + sortable.getField() + " ";
            if (sortable.getDirection() == Sortable.DESC) {
                sidx += "DESC ";
            }
            select.append(sidx);
        }
        if (page != null) {
            select.append(" " + page.makeSql());
        }
        return select.toString();
    }

    private interface StringConnector<T> {
        default String singleItem(T item, int index, String now) {
            return item.toString();
        }

        int connectPosition(T item, int index, String now);
    }

    static <T> String ListToStr(List<T> ls, String connectStr, StringConnector<T> connector) {
        if (connector == null)
            return "";
        if (StringUtils.isEmpty(connectStr))
            connectStr = ",";
        int index = 0;
        String result = "";
        for (T i : ls) {
            int pos = connector.connectPosition(i, index, result);
            if (pos < 0)
                result += connectStr + connector.singleItem(i, index, result);
            else if (pos > 0)
                result += connector.singleItem(i, index, result) + connectStr;
            else
                result += connector.singleItem(i, index, result);
            index++;
        }
        return result;
    }


    private static class SqlFieldConnector implements StringConnector<SqlFieldItem> {

        @Override
        public String singleItem(SqlFieldItem item, int index, String now) {
            return "`" + item.field + "`";
        }

        @Override
        public int connectPosition(SqlFieldItem item, int index, String now) {
            return -index;
        }
    }

    private static class SqlValueConnector implements StringConnector<SqlFieldItem> {

        @Override
        public String singleItem(SqlFieldItem item, int index, String now) {
            if(StringUtils.isEmpty(item.value))
                return "NULL";
            return "'" + item.value + "'";
        }

        @Override
        public int connectPosition(SqlFieldItem item, int index, String now) {
            return -index;
        }
    }

    static StringConnector<SqlFieldItem> fieldItemStringConnector = new SqlFieldConnector(),
            valueItemStringConnector = new SqlValueConnector();

    public static <Q> String insert(Q data) throws Exception {

        Updateable updateable = data.getClass().getDeclaredAnnotation(Updateable.class);
        if (updateable == null) {
            throw new Exception("not find updateable in class");
        }

        List<SqlFieldItem> items = qoToList(data);
        if (CollectionUtils.isEmpty(items))
            throw new Exception("empty field for " + data.getClass());
        StringBuilder builder = new StringBuilder("insert into " + updateable.table() + " ");
        String feilds = "(" + ListToStr(items, ",", fieldItemStringConnector) + ")";
        String values = "(" + ListToStr(items, ",", valueItemStringConnector)+")";
        builder.append(feilds).append(" values ").append(values);
        return builder.toString();
    }


    public static String listOr(String field, List list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder builder = new StringBuilder("where ");
        int index = 0;
        Object item = list.get(index++);
        builder.append(field + "='" + item.toString() + "' ");
        for (; index < list.size(); index++) {
            item = list.get(index);
            builder.append(" or " + field + "='" + item.toString() + "' ");
        }
        return builder.toString();
    }
}
