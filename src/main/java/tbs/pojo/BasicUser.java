package tbs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import tbs.framework.model.BaseEntities;

@TableName( "basicuser")
@ToString
@Data
public class BasicUser extends BaseEntities {

    @TableId("uid")
    private String uid;
    @TableField("name")
    private String name;
    @TableField("password")
    private String password;
    @TableField("phone")
    private String phone;
    @TableField("role")
    private Integer role;
    @TableField("departmentId")
    private Integer departmentId;

}
