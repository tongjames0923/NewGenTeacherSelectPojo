package tbs.dao;

import org.apache.ibatis.annotations.Select;
import tbs.pojo.Department;

import java.util.List;

public interface DepartmentDao {

    @Select("select * from department where parentId=#{id}")
    List<Department> findAllByParent(int id);

    @Select("select p2.* from department p1 join department p2 on p2.id=p1.parentId where p1.id=#{id}")
    Department getParent(int id);

}
