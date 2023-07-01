package tbs.pojo;

import tbs.framework.annotation.SqlField;

public class Role {
    @SqlField
    int roleid;
    @SqlField
    String rolename;

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
