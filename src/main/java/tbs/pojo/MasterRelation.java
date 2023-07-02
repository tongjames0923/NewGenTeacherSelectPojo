package tbs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tbs.framework.model.BaseEntities;

@Data
@NoArgsConstructor
@ToString
@TableName( "masterrelation")
public class MasterRelation extends BaseEntities {

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
