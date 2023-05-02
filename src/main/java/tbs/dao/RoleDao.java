package tbs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import tbs.dao.impl.SQL_Tool;
import tbs.utils.AOP.authorize.model.BaseRoleModel;

import java.util.List;

@Mapper
public interface RoleDao {
    @Select("SELECT r.roleid as roleCode,r.rolename as roleName FROM basicuser bu JOIN role r ON r.roleid=bu.role WHERE bu.`phone`=#{phone} AND bu.`password`=#{password};")
    BaseRoleModel loginRole(String phone,String password);

    @SelectProvider(type = SQL_Tool.class,method = "rolesIn")
    List<BaseRoleModel> roleInList(List<Integer> ids);
}
