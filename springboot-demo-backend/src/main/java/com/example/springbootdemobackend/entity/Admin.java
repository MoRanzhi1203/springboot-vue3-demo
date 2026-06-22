package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
@TableName("admin")
@Schema(description = "用户信息")
public class Admin {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "账号")
    private String username;

    // 数据库字段为 userpwd，JSON 序列化也使用 userpwd
    @TableField("userpwd")
    @JsonProperty("userpwd")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "电话")
    private String tel;

    @Schema(description = "头像地址")
    private String headurl;

    @TableLogic
    @Schema(description = "逻辑删除(0=未删除,1=已删除)", hidden = true)
    private Integer deleted;
}
