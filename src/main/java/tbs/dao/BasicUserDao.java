package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.pojo.BasicUser;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.util.List;

@Mapper
public interface BasicUserDao {
    @Insert("insert into basicuser(uid, name, password, phone, role, departmentId) " +
            "VALUES (#{uid},#{name},#{password},#{phone},#{role},#{departmentId})")
    int save(BasicUser u);

    @Select("select * from basicuser where departmentId=#{dep} ")
    List<BasicUser> findByDepartment(int dep);
}
