package tbs.utils.sql.query;

public class Page {

    /**
     * 当前页
     */
    private int page;
    /**
     * 一页数量
     */
    int count;

    int offset;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Page(int page, int count) {
        this.page = page<=0?0:page;
        this.offset=page<=0?0:(page - 1) * count;
        this.count = count;
    }

    public String makeSql() {
        return " LIMIT " + offset + "," + count + " ";
    }

}
