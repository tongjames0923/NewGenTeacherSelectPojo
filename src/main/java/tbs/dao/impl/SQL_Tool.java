package tbs.dao.impl;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class SQL_Tool {

    public String rolesIn(List<Integer> list) {
        StringBuilder builder = new StringBuilder("SELECT r.roleid as roleCode,r.rolename as roleName from role r where ");
        builder.append(listOr("r.roleid",list));
        return builder.toString();
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
