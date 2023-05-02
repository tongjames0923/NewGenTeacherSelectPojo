package tbs.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import tbs.pojo.BasicUser;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

@Mapper
public interface BasicUserDao {
    @Insert("insert into basicuser(uid, name, password, phone, role, departmentId) " +
            "VALUES (#{uid},#{name},#{password},#{phone},#{role},#{departmentId})")
    int save(BasicUser u);
}
