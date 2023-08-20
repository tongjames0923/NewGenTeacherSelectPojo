package tbs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@TableName("scoreConfigTemplate")
@Data
public class ScoreConfigTemplate {

    @TableId
    private String id;
    @TableField("templateName")
    private String templateName;
    @TableField("departmentId")
    private Long departmentId;
    @TableField("createUser")
    private String createUser;

    @TableField("createTime")
    private Date createTime;


}
