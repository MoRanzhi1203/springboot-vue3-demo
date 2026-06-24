package com.example.springbootdemobackend.controller;

import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数据概览仪表盘 Controller
 */
@RestController
@RequestMapping("/dashboard")
@Tag(name = "数据概览仪表盘")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/overview")
    @Operation(summary = "获取数据概览仪表盘数据")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = dashboardService.getOverview();
        return R.data(data);
    }
}
