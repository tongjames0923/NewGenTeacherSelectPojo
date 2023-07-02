package tbs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import tbs.framework.model.BaseEntities;

import java.util.Date;

@TableName("scoreConfigTemplate")
@Data
public class ScoreConfigTemplate extends BaseEntities {

    @TableId
    private String id;
    @TableField("templateName")
    private String templateName;
    @TableField("departmentId")
    private int departmentId;


}
