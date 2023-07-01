package tbs.pojo;

import tbs.utils.sql.annotations.SqlField;
import tbs.utils.sql.annotations.Updateable;

@Updateable(table = "scoreConfigTemplateItem")
public class ScoreConfigTemplateItem {
    @SqlField
    private int id;
    @SqlField
    private Integer sortCode;
    @SqlField
    private Integer percent;
    @SqlField
    private String templateId;

    @SqlField
    private String configName;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSortCode() {
        return sortCode;
    }

    public void setSortCode(Integer sortCode) {
        this.sortCode = sortCode;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
