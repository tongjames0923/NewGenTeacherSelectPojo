package tbs.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@TableName("department")
@Data
@ToString
public class Department {
    @TableId
    private Long id;
    @TableField("parentId")
    private Long parentId;
    @TableField("departname")
    private String departname;

}
