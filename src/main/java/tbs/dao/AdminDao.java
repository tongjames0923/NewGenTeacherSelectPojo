package tbs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import tbs.pojo.dto.AdminDetail;
import tbs.utils.redis.RedisConfig;

import java.util.List;

@Mapper
public interface AdminDao {

    @Select(AdminDetail.BASIC_DATA_SQL)
    @Cacheable(value = "ADMIN_KEY",unless = "#result==null",cacheManager = RedisConfig.LongTermCache)
    List<AdminDetail> listSu();
}
