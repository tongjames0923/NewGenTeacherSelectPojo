package tbs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import tbs.dao.impl.SqlUpdateImpl;
import tbs.pojo.ScoreConfigTemplate;
import tbs.pojo.ScoreConfigTemplateItem;
import tbs.utils.sql.SQL_Tool;

import java.util.List;

@Mapper
public interface ScoreConfigDao {


    @UpdateProvider(type = SqlUpdateImpl.class,method = "insert")
    void insertTemplate(ScoreConfigTemplate template);
    @UpdateProvider(type = SqlUpdateImpl.class,method = "insert")
    void insertTemplateItem(ScoreConfigTemplateItem item);

    List<ScoreConfigTemplate> listTemplateByDepartment(int department);

    @Select("select * from scoreconfigtemplate where templateName=#{templateName} and departmentId=#{department} limit 1;")
    ScoreConfigTemplate findOneByDepartmentAndName(int department,String templateName);

}
