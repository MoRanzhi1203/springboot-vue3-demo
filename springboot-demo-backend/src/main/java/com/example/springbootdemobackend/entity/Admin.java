package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("admin")
public class Admin {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    @TableField("userpwd")
    @JsonProperty("userpwd")
    private String password;
    private String name;
    private String sex;
    private String tel;
    private String headurl;
    @TableLogic
    private Integer deleted;
}
