package tbs.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tbs.dao.impl.SQL_QueryImpl;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.ScoreConfigTemplate;
import tbs.pojo.ScoreConfigTemplateItem;
import tbs.utils.redis.RedisConfig;
import tbs.utils.sql.SQL_Tool;

import java.util.List;

@Mapper
public interface ScoreConfigDao {


    @UpdateProvider(type = SqlUpdateImpl.class, method = "insert")
    void insertTemplate(ScoreConfigTemplate template);

    @UpdateProvider(type = SqlUpdateImpl.class, method = "insert")
    void insertTemplateItem(ScoreConfigTemplateItem item);

    @Select("select * from scoreconfigtemplate where departmentId=#{department};")
    List<ScoreConfigTemplate> listTemplateByDepartment(int department);

    @Select("select * from scoreconfigtemplate where templateName=#{templateName} and departmentId=#{department} limit 1;")
    ScoreConfigTemplate findOneByDepartmentAndName(int department, String templateName);


    @Select("select * from scoreconfigtemplate where id=#{id}")
    @Cacheable(value = "scoreTemplate",key = "#id",unless = "#result==null" ,cacheManager = RedisConfig.ShortTermCache)
    ScoreConfigTemplate findById(String id);

    @Delete("delete from scoreconfigtemplate where id=#{id}")
    @CacheEvict(value = "scoreTemplate",key = "#id")
    int deleteTemplate(String id);


    @UpdateProvider(type = SqlUpdateImpl.class,method = SqlUpdateImpl.UPDATE)
    @CacheEvict(value = "scoreTemplate",key = "#item.id")
    void updateTemplate(ScoreConfigTemplate item);

    @Delete("delete from scoreconfigtemplateitem where templateId=#{templateId}")
    int deleteTemplateItems(String templateId);

    @Select("select * from scoreconfigtemplateitem where templateId=#{templateId} order by sortCode asc")
    List<ScoreConfigTemplateItem> listTemplateItems(String templateId);

}
