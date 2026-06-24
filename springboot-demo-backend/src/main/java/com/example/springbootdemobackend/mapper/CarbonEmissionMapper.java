package com.example.springbootdemobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootdemobackend.entity.CarbonEmission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 碳排放 Mapper
 */
public interface CarbonEmissionMapper extends BaseMapper<CarbonEmission> {

    /**
     * 查询累计碳排放总量
     */
    @Select("SELECT COALESCE(SUM(total_emission), 0) FROM carbon_emission")
    BigDecimal sumTotalEmission();

    /**
     * 查询指定年份月度碳排放
     */
    @Select("SELECT emission_month AS month, " +
            "COALESCE(SUM(total_emission), 0) AS total " +
            "FROM carbon_emission " +
            "WHERE emission_year = #{year} " +
            "GROUP BY emission_month " +
            "ORDER BY emission_month")
    List<Map<String, Object>> getMonthlyEmissionByYear(@Param("year") Integer year);

    /**
     * 查询碳排放来源占比（工业生产/交通运输/建筑施工/其他）
     */
    @Select("SELECT '工业生产' AS name, COALESCE(SUM(scope1_emission) * 0.5, 0) AS value FROM carbon_emission " +
            "UNION ALL " +
            "SELECT '交通运输' AS name, COALESCE(SUM(scope2_emission), 0) AS value FROM carbon_emission " +
            "UNION ALL " +
            "SELECT '建筑施工' AS name, COALESCE(SUM(scope3_emission) * 0.6667, 0) AS value FROM carbon_emission " +
            "UNION ALL " +
            "SELECT '其他' AS name, COALESCE(SUM(scope3_emission) * 0.3333, 0) AS value FROM carbon_emission")
    List<Map<String, Object>> getEmissionSourceData();
}
