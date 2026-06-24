package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业实体类
 */
@Data
@TableName("company")
public class Company {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String companyName;
    private String companyCode;
    private String industry;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private BigDecimal registeredCapital;
    private LocalDate establishDate;
    private BigDecimal emissionQuota;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
