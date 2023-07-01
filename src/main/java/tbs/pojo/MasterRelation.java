package tbs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tbs.utils.sql.annotations.SqlField;
import tbs.utils.sql.annotations.Updateable;

@Data
@NoArgsConstructor
@ToString
@Updateable(table = "masterrelation")
@TableName("masterrelation")
public class MasterRelation {

    @TableField(value = "masterPhone")
    @SqlField
    private String masterPhone;
    @TableField("studentPhone")
    private String studentPhone;

    @SqlField
    @TableField("ScoreConfigItemId")
    private int ScoreConfigItemId;
    @TableId("id")
    private int id;

    public MasterRelation(String masterPhone, int scoreConfigItemId) {
        this.masterPhone = masterPhone;
        ScoreConfigItemId = scoreConfigItemId;
    }
}
