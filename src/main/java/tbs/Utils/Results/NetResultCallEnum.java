package tbs.utils.Results;

public enum NetResultCallEnum {

    Refresh("刷新"),Close("关闭");
    private String value;

    NetResultCallEnum(String n)
    {
        this.value=n;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
