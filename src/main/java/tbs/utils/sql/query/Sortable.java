package tbs.utils.sql.query;

public class Sortable {
    private int direction = ASC;
    private String field;

    private String dir;

    public String getDir() {
        return dir;
    }

    public Sortable(int direction, String field) {
        setDirection(direction);
        this.field = field;
    }
    public Sortable(String dir, String field) {
        this.setDir(dir);
        this.field = field;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public static final int DESC = 1, ASC = 0;
    private static final String[] sidxs={"asc","desc"};

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        this.dir=sidxs[direction];
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
