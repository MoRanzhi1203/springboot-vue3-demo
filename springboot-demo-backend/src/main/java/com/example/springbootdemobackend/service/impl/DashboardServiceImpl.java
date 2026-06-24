package com.example.springbootdemobackend.service.impl;

import com.example.springbootdemobackend.entity.EnergySavingGoal;
import com.example.springbootdemobackend.mapper.CarbonEmissionMapper;
import com.example.springbootdemobackend.mapper.EnergySavingGoalMapper;
import com.example.springbootdemobackend.service.CompanyService;
import com.example.springbootdemobackend.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Dashboard 数据概览服务实现
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private CarbonEmissionMapper carbonEmissionMapper;

    @Resource
    private EnergySavingGoalMapper energySavingGoalMapper;

    @Resource
    private CompanyService companyService;

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new LinkedHashMap<>();

        // 1. 累计碳排放总量
        BigDecimal totalEmission = carbonEmissionMapper.sumTotalEmission();
        result.put("totalEmission", totalEmission != null ? totalEmission : BigDecimal.ZERO);

        // 2. 检测企业数量
        long companyCount = companyService.count();
        result.put("companyCount", companyCount);

        // 3. 当前年度月度碳排放（无数据时回退到2024年）
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Map<String, Object>> monthlyData = carbonEmissionMapper.getMonthlyEmissionByYear(currentYear);
        if (monthlyData == null || monthlyData.isEmpty()) {
            monthlyData = carbonEmissionMapper.getMonthlyEmissionByYear(2024);
        }
        // 补齐1-12月空数据
        if (monthlyData == null) monthlyData = new ArrayList<>();
        Map<Integer, Object> monthMap = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) monthMap.put(m, 0);
        for (Map<String, Object> row : monthlyData) {
            Object mObj = row.get("month");
            Object tObj = row.get("total");
            if (mObj != null) {
                int m = Integer.parseInt(mObj.toString());
                monthMap.put(m, tObj != null ? tObj : 0);
            }
        }
        List<Map<String, Object>> fullMonthly = new ArrayList<>();
        for (Map.Entry<Integer, Object> e : monthMap.entrySet()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", e.getKey());
            item.put("total", e.getValue());
            fullMonthly.add(item);
        }
        result.put("monthlyEmission", fullMonthly);

        // 4. 碳排放来源占比
        List<Map<String, Object>> sourceData = carbonEmissionMapper.getEmissionSourceData();
        if (sourceData == null) sourceData = new ArrayList<>();
        // 统一 name/value
        List<Map<String, Object>> normalizedSource = new ArrayList<>();
        for (Map<String, Object> row : sourceData) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", row.getOrDefault("name", row.getOrDefault("source_name", "")));
            item.put("value", row.getOrDefault("value", row.getOrDefault("total", 0)));
            normalizedSource.add(item);
        }
        result.put("sourceData", normalizedSource);

        // 5. 减排完成率（从 energy_saving_goal 表 progress 取平均，无数据返回0）
        double reductionRate = 0;
        try {
            List<EnergySavingGoal> goalList = energySavingGoalMapper.selectList(null);
            if (goalList != null && !goalList.isEmpty()) {
                double sum = 0;
                int count = 0;
                for (EnergySavingGoal g : goalList) {
                    if (g.getProgress() != null) {
                        sum += g.getProgress().doubleValue();
                        count++;
                    }
                }
                if (count > 0) {
                    reductionRate = BigDecimal.valueOf(sum / count)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
            }
        } catch (Exception ignored) {
            // 表不存在时忽略
        }
        result.put("reductionRate", reductionRate);

        return result;
    }
}
