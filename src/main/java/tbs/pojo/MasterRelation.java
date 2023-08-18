package tbs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString
@TableName( "masterrelation")
public class MasterRelation  {

    @TableField(value = "masterPhone")
    private String masterPhone;
    @TableField("studentPhone")
    private String studentPhone;

    @TableField("ScoreConfigItemId")
    private int ScoreConfigItemId;
    @TableId("id")
    private int id;

    public MasterRelation(String masterPhone, int scoreConfigItemId) {
        this.masterPhone = masterPhone;
        ScoreConfigItemId = scoreConfigItemId;
    }
}
