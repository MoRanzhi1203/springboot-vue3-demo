package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.Company;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.CompanyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@Tag(name = "企业管理")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @GetMapping("/list")
    @Operation(summary = "分页查询企业")
    public R<PageInfo<Company>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String industry) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(companyName)) wrapper.like(Company::getCompanyName, companyName);
        if (ObjectUtils.isNotEmpty(industry)) wrapper.like(Company::getIndustry, industry);
        wrapper.orderByDesc(Company::getId);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(companyService.list(wrapper)));
    }

    @PostMapping("/add")
    @Operation(summary = "新增企业")
    public R<Void> add(@RequestBody Company company) {
        companyService.save(company);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑企业")
    public R<Void> update(@RequestBody Company company) {
        companyService.updateById(company);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除企业")
    public R<Void> delete(@RequestParam Long id) {
        companyService.removeById(id);
        return R.success("删除成功");
    }
}
