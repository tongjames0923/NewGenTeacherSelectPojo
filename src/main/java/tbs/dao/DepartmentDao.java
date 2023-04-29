package tbs.dao;

import org.apache.ibatis.annotations.Select;
import tbs.pojo.Department;

import java.util.List;

public interface DepartmentDao {

    @Select("select * from department where parentId=#{id}")
    List<Department> findAllByParent(int id);

    @Select("select * from department where id=#{id}")
    Department getById(int id);

}
