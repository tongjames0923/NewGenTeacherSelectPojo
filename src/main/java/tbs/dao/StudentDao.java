package tbs.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.QO.StudentQO;
import tbs.dao.impl.SQL_QueryImpl;
import tbs.pojo.Student;
import tbs.pojo.dto.StudentUserDetail;
import tbs.utils.redis.RedisConfig;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

@Mapper
public interface StudentDao {


    @Insert("insert into student(studentNo, grade, cla, phone) VALUES (#{studentNo},#{grade},#{cla},#{phone})")
    int saveStudent(Student item);

    @Select(StudentUserDetail.BASIC_DATA_SQL + "where bu.phone=#{phone}")
    @Cacheable(cacheManager = RedisConfig.ShortTermCache, key = "#phone", value = "studentInfo")
    StudentUserDetail findStudentByPhone(String phone);


    @SelectProvider(type = SQL_QueryImpl.class, method = "query")
    List<StudentUserDetail> studentDetailQuery(StudentQO qo, Page page, Sortable sortable);

    @Select(StudentUserDetail.BASIC_DATA_SQL + " where bu.departmentId=#{department} limit #{beg},#{end}")
    List<StudentUserDetail> findStudentByDepartment(int department, int beg, int end);

}
