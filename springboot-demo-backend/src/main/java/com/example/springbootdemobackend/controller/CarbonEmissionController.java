package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.CarbonEmission;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.CarbonEmissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carbonEmission")
@Tag(name = "碳排放管理")
public class CarbonEmissionController {

    @Resource
    private CarbonEmissionService carbonEmissionService;

    @GetMapping("/list")
    @Operation(summary = "分页查询碳排放")
    public R<PageInfo<CarbonEmission>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Integer emissionYear,
            @RequestParam(required = false) Integer emissionMonth) {
        LambdaQueryWrapper<CarbonEmission> wrapper = new LambdaQueryWrapper<>();
        if (companyId != null) wrapper.eq(CarbonEmission::getCompanyId, companyId);
        if (emissionYear != null) wrapper.eq(CarbonEmission::getEmissionYear, emissionYear);
        if (emissionMonth != null) wrapper.eq(CarbonEmission::getEmissionMonth, emissionMonth);
        wrapper.orderByDesc(CarbonEmission::getId);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(carbonEmissionService.list(wrapper)));
    }

    @PostMapping("/add")
    @Operation(summary = "新增碳排放记录")
    public R<Void> add(@RequestBody CarbonEmission record) {
        carbonEmissionService.save(record);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑碳排放记录")
    public R<Void> update(@RequestBody CarbonEmission record) {
        carbonEmissionService.updateById(record);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除碳排放记录")
    public R<Void> delete(@RequestParam Long id) {
        carbonEmissionService.removeById(id);
        return R.success("删除成功");
    }
}
