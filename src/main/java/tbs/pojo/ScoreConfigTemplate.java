package tbs.pojo;

import tbs.framework.annotation.SqlField;

import java.util.Date;

public class ScoreConfigTemplate {

    @SqlField
    private String id;
    @SqlField
    private String templateName;
    @SqlField
    private String createUser;
    @SqlField
    private Date createTime;
    @SqlField
    private int departmentId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
