package tbs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import tbs.pojo.ScoreConfigTemplateItem;
import tbs.pojo.StudentLevel;
import tbs.utils.redis.RedisConfig;

@Mapper
public interface StudentLevelDao extends BaseMapper<StudentLevel> {

    @Select("select item.* from studentLevel sl join scoreconfigtemplateitem item on item.id=sl.levelId where studentPhone=#{student} ")
    ScoreConfigTemplateItem findByPhone(String student);

}
