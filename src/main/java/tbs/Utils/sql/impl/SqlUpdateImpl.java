package tbs.utils.sql.impl;

import lombok.extern.slf4j.Slf4j;
import tbs.utils.sql.SQL_Tool;

import java.util.List;

@Slf4j
public class SqlUpdateImpl {

    public static final String INSERT="insert",UPDATE="update";

    public  String insert(Object obj) {
        try {
            return SQL_Tool.insert(obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
    public String update(Object obj)
    {
        try {
            return SQL_Tool.update(obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
