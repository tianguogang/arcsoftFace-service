package com.tgg.arcsoftfaceservice.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 田国刚
 * @since 2023-04-16
 */
@Data
@TableName("fact_info")
public class FactInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 面部信息
     */
    @TableField("face_data")
    private String faceData;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("remark")
    private String remark;


}
