package tbs.utils.sql.query;

public class Page {
    private int page, pageNum, count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
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
