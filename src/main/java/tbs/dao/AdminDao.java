package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.Admin;
import tbs.pojo.dto.AdminDetail;
import tbs.utils.redis.RedisConfig;

import java.util.List;

@Mapper
public interface AdminDao {

    @Select(AdminDetail.BASIC_DATA_SQL)
    @Cacheable(value = "ADMIN_KEY",unless = "#result==null",cacheManager = RedisConfig.LongTermCache)
    List<AdminDetail> listSu();


    @InsertProvider(type = SqlUpdateImpl.class,method = "insert")
    void saveAdmin(Admin admin);
}
