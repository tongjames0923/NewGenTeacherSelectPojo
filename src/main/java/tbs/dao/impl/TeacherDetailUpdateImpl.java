package tbs.dao.impl;

import tbs.pojo.Teacher;
import tbs.utils.sql.SQL_Tool;

public class TeacherDetailUpdateImpl {
    public String insert(Teacher teacher)
    {
        try {
            return SQL_Tool.insert(teacher);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
