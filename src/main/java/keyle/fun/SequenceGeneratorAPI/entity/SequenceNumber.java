package keyle.fun.SequenceGeneratorAPI.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


@Data
@TableName("sequence_number")
public class SequenceNumber {
    // id
    @TableId(type = IdType.AUTO)
    private Long id;
    // 序列号
    @TableField(value = "sequence_no")
    private String sequenceNo;
    // 创建时间
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    // 更新时间
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
