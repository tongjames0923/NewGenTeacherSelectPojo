package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.BasicUser;
import tbs.utils.AOP.authorize.model.BaseRoleModel;
import tbs.utils.redis.RedisConfig;

import java.util.List;

@Mapper
public interface BasicUserDao {
    @Insert("insert into basicuser(uid, name, password, phone, role, departmentId) " +
            "VALUES (#{uid},#{name},#{password},#{phone},#{role},#{departmentId})")
    @CacheEvict(value = "dev_users",key = "#u.departmentId")
    int save(BasicUser u);

    @Select("select * from basicuser where departmentId=#{dep} ")
    @Cacheable(value = "dev_users",key = "#dep",cacheManager = RedisConfig.ShortTermCache,unless = "#result==null")
    List<BasicUser> findByDepartment(int dep);


    @UpdateProvider(type = SqlUpdateImpl.class,method = "update")
    @CacheEvict(value = "dev_users",allEntries = true)
    void updateBaiscUser(BasicUser basicUser);
}
