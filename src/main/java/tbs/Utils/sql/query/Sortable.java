package tbs.utils.sql.query;

public class Sortable {
    private int direction = ASC;
    private String field;

    public static final int DESC = -1, ASC = 1;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
