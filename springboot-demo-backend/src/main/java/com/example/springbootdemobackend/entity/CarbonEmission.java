package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 碳排放实体类
 */
@Data
@TableName("carbon_emission")
public class CarbonEmission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long companyId;
    private Integer emissionYear;
    private Integer emissionMonth;
    private BigDecimal scope1Emission;
    private BigDecimal scope2Emission;
    private BigDecimal scope3Emission;
    private BigDecimal totalEmission;
    private String dataSource;
    private String verificationStatus;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
