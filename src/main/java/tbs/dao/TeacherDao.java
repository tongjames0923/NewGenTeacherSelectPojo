package tbs.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.Teacher;
import tbs.pojo.dto.TeacherDetail;
import tbs.utils.redis.RedisConfig;

import java.util.List;

public interface TeacherDao {
    @InsertProvider(type = SqlUpdateImpl.class, method = "insert")
    void saveTeacher(Teacher teacher);

    @Select(TeacherDetail.BASIC_DATA_SQL + "where bu.phone=#{phone}")
    @Cacheable(cacheManager = RedisConfig.ShortTermCache, key = "#phone", value = "teacherInfo", unless = "#result==null")
    TeacherDetail findTeacherByPhone(String phone);

    @UpdateProvider(type = SqlUpdateImpl.class, method = "update")
    @CacheEvict(key = "#teacher.phone", value = "teacherInfo")
    void updateTeacher(Teacher teacher);


    @Select("select * from teacher t join basicuser b on t.phone = b.phone where departmentId=#{department}")
    List<Teacher> listTeacherByDepartment(Integer department);
}
