package tbs.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.QO.StudentQO;
import tbs.dao.impl.SQL_QueryImpl;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.Student;
import tbs.pojo.dto.StudentUserDetail;
import tbs.utils.redis.RedisConfig;
import tbs.utils.sql.query.Page;
import tbs.utils.sql.query.Sortable;

import java.util.List;

@Mapper
public interface StudentDao {

    @InsertProvider(type = SqlUpdateImpl.class,method = SqlUpdateImpl.INSERT)
    void saveStudent(Student item);

    @Select(StudentUserDetail.BASIC_DATA_SQL + "where bu.phone=#{phone}")
    @Cacheable(cacheManager = RedisConfig.ShortTermCache, key = "#phone", value = "studentInfo")
    StudentUserDetail findStudentByPhone(String phone);

    @UpdateProvider(type = SqlUpdateImpl.class, method = "update")
    @CacheEvict(key = "#student.phone", value = "studentInfo")
    void updateStudent(Student student);



    @Select(StudentUserDetail.BASIC_DATA_SQL + " where bu.departmentId=#{department} limit #{beg},#{end}")
    List<StudentUserDetail> findStudentByDepartment(int department, int beg, int end);

}
