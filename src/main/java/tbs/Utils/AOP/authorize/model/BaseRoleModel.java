package tbs.utils.AOP.authorize.model;

public class BaseRoleModel {
    int roleCode;
    String roleName;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "BaseRoleModel{" +
                "roleCode=" + roleCode +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
