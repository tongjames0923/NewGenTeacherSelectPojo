package tbs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.pojo.MasterRelation;
import tbs.pojo.dto.MasterRelationVO;

import java.util.List;

@Mapper
public interface MasterRelationDao extends BaseMapper<MasterRelation> {

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL limit 1")
    MasterRelation findEmptyByMasterAndConfig(String master, int config);


    @Select("select count(1) from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL")
    Integer countEmpty(String master,int config);

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config}")
    List<MasterRelation> findByMasterAndConfig(String master,int config);

    @Select("SELECT mr.masterPhone AS `master`,( " +
            "SELECT COUNT(1) FROM masterrelation mr1 WHERE mr1.masterPhone=mr.masterPhone AND mr1.studentPhone IS NULL and mr1.scoreConfigItemId=#{configItem}) AS `left`,( " +
            "SELECT COUNT(1) FROM masterrelation mr1 WHERE mr1.masterPhone=mr.masterPhone AND mr1.studentPhone IS NOT NULL and mr1.scoreConfigItemId=#{configItem}) AS `had` FROM masterrelation mr GROUP BY mr.masterPhone")
    List<MasterRelationVO> listMasterByHasStudent(int configItem);


    @Select("select scoreConfigItemId from masterrelation group by scoreConfigItemId")
    List<Integer> allTemplateItemIds();
}
