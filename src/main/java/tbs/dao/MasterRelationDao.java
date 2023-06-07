package tbs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tbs.pojo.MasterRelation;
import tbs.pojo.dto.MasterRelationVO;
import tbs.utils.Async.annotations.LockIt;

import java.util.List;

@Mapper
public interface MasterRelationDao extends BaseMapper<MasterRelation> {

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL limit 1")
    MasterRelation findEmptyByMasterAndConfig(String master, int config);



    @Select("select * from masterrelation where studentPhone=#{student} limit 1;")
    MasterRelation findByStudent(String student);


    @Select("select count(1) from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config} and studentPhone is NULL")
    Integer countEmpty(String master,int config);

    @Select("select * from masterrelation where masterPhone=#{master} and scoreConfigItemId=#{config}")
    List<MasterRelation> findByMasterAndConfig(String master,int config);

    @Select("SELECT mr.masterPhone AS `master`,( " +
            "SELECT COUNT(1) FROM masterrelation mr1 WHERE mr1.masterPhone=mr.masterPhone AND mr1.studentPhone IS NULL and mr1.scoreConfigItemId=#{configItem}) AS `left`,( " +
            "SELECT COUNT(1) FROM masterrelation mr1 WHERE mr1.masterPhone=mr.masterPhone AND mr1.studentPhone IS NOT NULL and mr1.scoreConfigItemId=#{configItem}) AS `had` FROM masterrelation mr GROUP BY mr.masterPhone")
    List<MasterRelationVO> listMasterByHasStudent(int configItem);


    @Update("UPDATE masterrelation m1 " +
            "JOIN (SELECT id FROM masterrelation WHERE studentPhone is NULL and masterPhone=#{master} and scoreConfigItemId=#{config} LIMIT 1) m2 " +
            "ON m1.id = m2.id " +
            "SET m1.studentPhone = #{student};")
    int selectMaster(String student,String master,int config);

    @Update("UPDATE masterrelation mr set studentPhone=null where mr.masterPhone=#{master} and mr.studentPhone=#{student}")
    int unselectMaster(String student,String master);


    @Select("select scoreConfigItemId from masterrelation group by scoreConfigItemId")
    List<Integer> allTemplateItemIds();
}
