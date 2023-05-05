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

    public String makeSql() {
        int from = (page - 1) * count;
        int to = from + count;
        return " LIMIT " + from + "," + to + " ";
    }

}
