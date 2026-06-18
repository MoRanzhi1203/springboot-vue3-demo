package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.Admin;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    // ==================== 新增 ====================
    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public R<Void> add(@RequestBody Admin admin) {
        adminService.save(admin);
        return R.success();
    }

    // ==================== 分页列表查询 ====================
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    public R<PageInfo<Admin>> list(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(defaultValue = "1") Integer pagenum,
                                   @RequestParam(defaultValue = "10") Integer pagesize) {
        LambdaQueryWrapper<Admin> adminWrapper = new LambdaQueryWrapper<>();

        // 姓名模糊查询
        if (ObjectUtils.isNotEmpty(name)) {
            adminWrapper.like(Admin::getName, name);
        }

        // 账号模糊查询
        if (ObjectUtils.isNotEmpty(username)) {
            adminWrapper.like(Admin::getUsername, username);
        }

        // 按 ID 倒序
        adminWrapper.orderByDesc(Admin::getId);

        // 开启分页
        PageHelper.startPage(pagenum, pagesize);

        // 执行查询
        List<Admin> adminList = adminService.list(adminWrapper);

        // 封装分页信息
        PageInfo<Admin> pageInfo = new PageInfo<>(adminList);

        return R.data(pageInfo);
    }

    // ==================== 编辑 ====================
    @PutMapping("/update")
    @Operation(summary = "编辑用户")
    public R<Void> update(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return R.success();
    }

    // ==================== 删除 ====================
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户")
    public R<Void> delete(@PathVariable Long id) {
        adminService.removeById(id);
        return R.success();
    }

    // ==================== 批量删除 ====================
    @DeleteMapping("/batchDelete")
    @Operation(summary = "批量删除用户")
    public R<Void> batchDelete(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        adminService.removeByIds(idList);
        return R.success();
    }
}
