package tbs.pojo.dto;

public class AdminDetail extends BasicUserDetail {
    private String adminToken;
    public static final String BASIC_DATA_SQL="SELECT bu.`name` AS `name`,bu.phone AS `phone`,bu.departmentId AS `departmentId`,r.roleid AS `role`,r.rolename AS `roleName`,d.departname AS `department`,st.adminToken as adminToken FROM basicuser bu JOIN admin st ON st.phone=bu.phone JOIN role r ON r.roleid=bu.role JOIN department d ON bu.departmentId=d.id ";

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }
}
