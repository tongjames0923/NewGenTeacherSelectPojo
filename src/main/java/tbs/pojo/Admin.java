package tbs.pojo;

import tbs.framework.annotation.SqlField;
import tbs.framework.sql.model.BasicModel;

import javax.persistence.Table;

@Table(name = "admin")
public class Admin extends BasicModel {
    @SqlField
    private String adminToken;
    @SqlField(isPrimary = true)
    String phone;

    public String getAdminToken() {
        return adminToken;
    }

    public void setAdminToken(String adminToken) {
        this.adminToken = adminToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
