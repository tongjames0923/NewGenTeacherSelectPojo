package tbs.utils.sql;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Queryable;
import tbs.utils.sql.query.Sortable;
import tbs.utils.sql.query.SqlField;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            fieldItem.setField(f.field());
            Object v = field.get(qo);
            if (v != null) {
                fieldItem.setValue(v.toString());
                items.add(fieldItem);
            }
        }
        items.sort((item1, item2) -> {
            return item1.index - item2.index;
        });
        return items;
    }

    public String rolesIn(List<Integer> list) {
        StringBuilder builder = new StringBuilder("SELECT r.roleid as roleCode,r.rolename as roleName from role r where ");
        builder.append(listOr("r.roleid", list));
        return builder.toString();
    }

    public static <T, Q> String query(Class<T> tClass, Q qo, Page page, Sortable sortable) throws Exception {
        Queryable queryable = tClass.getDeclaredAnnotation(Queryable.class);
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

    public static String listOr(String field, List list) {
        if (CollectionUtils.isEmpty(list)) {
            return "()";
        }
        StringBuilder builder = new StringBuilder();
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
