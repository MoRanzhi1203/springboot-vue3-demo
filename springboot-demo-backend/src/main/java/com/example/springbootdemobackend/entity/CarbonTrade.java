package com.example.springbootdemobackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 碳交易实体类
 */
@Data
@TableName("carbon_trade")
public class CarbonTrade {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String tradeNo;
    private Long sellerCompanyId;
    private Long buyerCompanyId;
    private String tradeType;
    private BigDecimal tradeQuantity;
    private BigDecimal tradePrice;
    private BigDecimal totalAmount;
    private LocalDate tradeDate;
    private String tradePlatform;
    private String status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
