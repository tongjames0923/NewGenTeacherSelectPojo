package tbs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.pojo.MasterRelation;

import java.util.List;

@Mapper
public interface MasterRelationDao extends BaseMapper<MasterRelation> {

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL limit 1")
    MasterRelation findEmptyByMasterAndConfig(String master, int config);


    @Select("select count(1) from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL")
    Integer countEmpty(String master,int config);

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config}")
    List<MasterRelation> findByMasterAndConfig(String master,int config);

    @Select("select masterPhone from masterrelation where studentPhone is not null group by masterPhone")
    List<String> listMasterByHasStudent();
}
