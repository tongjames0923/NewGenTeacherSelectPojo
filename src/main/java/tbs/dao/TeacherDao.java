package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.TeacherDetailUpdateImpl;
import tbs.pojo.Teacher;
import tbs.pojo.dto.StudentUserDetail;
import tbs.pojo.dto.TeacherDetail;
import tbs.utils.redis.RedisConfig;

public interface TeacherDao {
    @InsertProvider(type = TeacherDetailUpdateImpl.class,method = "insert")
    void saveTeacher(Teacher teacher);

    @Select(TeacherDetail.BASIC_DATA_SQL + "where bu.phone=#{phone}")
    @Cacheable(cacheManager = RedisConfig.ShortTermCache, key = "#phone", value = "teacherInfo")
    TeacherDetail findTeacherByPhone(String phone);
}
