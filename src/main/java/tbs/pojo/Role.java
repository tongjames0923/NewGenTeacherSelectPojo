package tbs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("role")
public class Role {
    @TableId("roleid")
    Integer roleid;
    @TableField("rolename")
    String rolename;

}
