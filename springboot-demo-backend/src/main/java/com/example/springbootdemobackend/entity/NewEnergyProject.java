package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 新能源项目实体类
 */
@Data
@TableName("new_energy_project")
public class NewEnergyProject {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String projectName;
    private String projectCode;
    private Long companyId;
    private String projectType;
    private BigDecimal capacity;
    private BigDecimal annualGeneration;
    private BigDecimal investmentAmount;
    private LocalDate startDate;
    private LocalDate completionDate;
    private String location;
    private String status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
