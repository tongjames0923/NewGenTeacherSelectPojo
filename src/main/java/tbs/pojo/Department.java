package tbs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@TableName("department")
@Data
@ToString
public class Department implements Serializable {

    private static final long serialVersionUID = 8736348091873326341L;
    @TableId
    private Long id;
    @TableField("parentId")
    private Long parentId;
    @TableField("departname")
    private String departname;

}
