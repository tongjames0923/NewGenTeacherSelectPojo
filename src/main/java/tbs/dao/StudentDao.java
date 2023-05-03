package tbs.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.StudentDetailQueryImpl;
import tbs.pojo.BasicUser;
import tbs.pojo.Student;
import tbs.pojo.dto.StudentDetail;
import tbs.utils.redis.RedisConfig;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

@Mapper
public interface StudentDao {


    @Insert("insert into student(studentNo, grade, cla, phone) VALUES (#{studentNo},#{grade},#{cla},#{phone})")
    int saveStudent(Student item);

    @Select(StudentDetail.BASIC_DATA_SQL + "where bu.phone=#{phone}")
    @Cacheable(cacheManager = RedisConfig.ShortTermCache, key = "#phone", value = "studentInfo")
    StudentDetail findStudentByPhone(String phone);


    @SelectProvider(type = StudentDetailQueryImpl.class, method = "query")
    List<StudentDetail> studentDetailQuery(StudentQO qo, Page page, Sortable sortable);

    @Select(StudentDetail.BASIC_DATA_SQL + " where bu.departmentId=#{department} limit #{beg},#{end}")
    List<StudentDetail> findStudentByDepartment(int department, int beg, int end);

}
