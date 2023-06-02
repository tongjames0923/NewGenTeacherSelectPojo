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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class SQL_Tool {


    private static class SqlFieldItem {
        private String field, value;
        boolean isPrimary = false;

        public boolean isPrimary() {
            return isPrimary;
        }

        public void setPrimary(boolean primary) {
            isPrimary = primary;
        }

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
    static SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            if (f.isPrimary())
                fieldItem.setPrimary(true);
            fieldItem.setIndex(f.index());
            fieldItem.setField(StringUtils.isEmpty(f.field()) ? field.getName() : f.field());
            Object v = field.get(qo);
            if (v != null) {
                if(v.getClass().equals(Date.class))
                {
                    Date d=(Date) v;

                    fieldItem.setValue(format.format(d));
                }
                else
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

    public static <Q> String query(Q qo, Page page, Sortable sortable) throws Exception {
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
            if (StringUtils.isEmpty(item.value))
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

        UpdateInnerInfo innerInfo = getUpdateInnerInfo(data);
        StringBuilder builder = new StringBuilder("insert into " + innerInfo.updateable.table() + " ");
        String feilds = "(" + ListToStr(innerInfo.items, ",", fieldItemStringConnector) + ")";
        String values = "(" + ListToStr(innerInfo.items, ",", valueItemStringConnector) + ")";
        builder.append(feilds).append(" values ").append(values);
        return builder.toString();
    }

    private static <Q> UpdateInnerInfo getUpdateInnerInfo(Q data) throws Exception {
        Updateable updateable = data.getClass().getDeclaredAnnotation(Updateable.class);
        if (updateable == null) {
            throw new Exception("not find updateable in class");
        }

        List<SqlFieldItem> items = qoToList(data);
        if (CollectionUtils.isEmpty(items))
            throw new Exception("empty field for " + data.getClass());
        UpdateInnerInfo innerInfo = new UpdateInnerInfo(updateable, items);
        return innerInfo;
    }

    private static class UpdateInnerInfo {
        public final Updateable updateable;
        public final List<SqlFieldItem> items;

        public UpdateInnerInfo(Updateable updateable, List<SqlFieldItem> items) {
            this.updateable = updateable;
            this.items = items;
        }
    }

    public static <Q> String update(Q data) throws Exception {
        Updateable updateable = data.getClass().getDeclaredAnnotation(Updateable.class);
        if (updateable == null) {
            throw new Exception("not find updateable in class");
        }

        List<SqlFieldItem> items = qoToList(data);
        if (CollectionUtils.isEmpty(items))
            throw new Exception("empty field for " + data.getClass());
        StringBuilder builder = new StringBuilder("UPDATE ").append(updateable.table()).append(" SET ");
        final SqlFieldItem[] p = {null};
        builder.append(ListToStr(items, " ", new StringConnector<SqlFieldItem>() {
            @Override
            public String singleItem(SqlFieldItem item, int index, String now) {
                if (item.isPrimary()) {
                    p[0] = item;
                    return "";
                }
                String v = "";
                if (!StringUtils.isEmpty(item.value))
                    v = "`" + item.field + "` = '" + item.value + "'";
                if (!StringUtils.isEmpty(now) && !StringUtils.isEmpty(v))
                    return "," + v;
                return v;
            }

            @Override
            public int connectPosition(SqlFieldItem item, int index, String now) {
                if (StringUtils.isEmpty(item.value) || StringUtils.isEmpty(now))
                    return 0;
                return -index;
            }
        }));
        if (p[0] == null || StringUtils.isEmpty(p[0].value))
            throw new Exception("不存在主键，无法更新");
        builder.append(" WHERE ").append("`").append(p[0].field).append("`='").append(p[0].value).append("'");
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
