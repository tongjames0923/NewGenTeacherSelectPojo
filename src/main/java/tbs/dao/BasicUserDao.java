package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.BasicUser;
import tbs.utils.AOP.authorize.model.BaseRoleModel;
import tbs.utils.redis.RedisConfig;

import java.util.List;

@Mapper
public interface BasicUserDao {
    @Insert("insert into basicuser(uid, name, password, phone, role, departmentId) " +
            "VALUES (#{uid},#{name},#{password},#{phone},#{role},#{departmentId})")
    @Caching(put = {
            @CachePut(value = "base_user",key = "#u.phone",unless = "#u==null",cacheManager = RedisConfig.ShortTermCache)
    },evict = {
            @CacheEvict(value = "dev_users",key = "#u.departmentId")
    })
    int save(BasicUser u);

    @Select("select * from basicuser where departmentId=#{dep} ")
    @Cacheable(value = "dev_users",key = "#dep",cacheManager = RedisConfig.ShortTermCache,unless = "#result==null")
    List<BasicUser> findByDepartment(int dep);


    @Select("select * from basicuser where phone=#{phone} ")
    @Cacheable(value = "base_user",key = "#phone",unless = "#result==null",cacheManager = RedisConfig.ShortTermCache)
    BasicUser findOneByPhone(String phone);


    @UpdateProvider(type = SqlUpdateImpl.class,method = "update")
    @Caching(evict = {
            @CacheEvict(value = "dev_users",allEntries = true)},
    put = {
            @CachePut(value = "base_user",key = "#basicUser.phone",unless = "#basicUser==null||#u",cacheManager = RedisConfig.ShortTermCache)
    })
    void updateBaiscUser(BasicUser basicUser);
}
