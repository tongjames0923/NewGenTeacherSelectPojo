package tbs.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import tbs.framework.model.BaseEntities;

import java.util.Date;

@Data
@TableName("role")
public class Role extends BaseEntities {
    @TableId("roleid")
    Integer roleid;
    @TableField("rolename")
    String rolename;

}
