package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 节能减排目标实体类
 */
@Data
@TableName("energy_saving_goal")
public class EnergySavingGoal {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long companyId;
    private String goalType;
    private Integer targetYear;
    private Integer baselineYear;
    private BigDecimal baselineValue;
    private BigDecimal targetValue;
    private BigDecimal actualValue;
    private String targetUnit;
    private BigDecimal progress;
    private String status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
