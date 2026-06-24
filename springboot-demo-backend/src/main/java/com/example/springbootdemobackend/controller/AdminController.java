package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.Admin;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.AdminService;
import com.example.springbootdemobackend.utils.JwtUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    @Resource
    private JwtUtils jwtUtils;

    // ==================== 新增 ====================
    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public R<Void> add(@RequestBody Admin admin) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<Admin> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(Admin::getUsername, admin.getUsername());
        long count = adminService.count(checkWrapper);
        if (count > 0) {
            return R.fail("用户名已存在");
        }
        adminService.save(admin);
        return R.success("添加成功");
    }

    // ==================== 分页列表查询 ====================
    @GetMapping("/list")
    @Operation(summary = "分页列表查询")
    public R<PageInfo<Admin>> list(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) String tel,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(defaultValue = "1") Integer pagenum,
                                   @RequestParam(defaultValue = "10") Integer pagesize) {
        LambdaQueryWrapper<Admin> adminWrapper = new LambdaQueryWrapper<>();

        // 姓名模糊查询
        if (ObjectUtils.isNotEmpty(name)) {
            adminWrapper.like(Admin::getName, name);
        }

        // 电话模糊查询
        if (ObjectUtils.isNotEmpty(tel)) {
            adminWrapper.like(Admin::getTel, tel);
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

    // ==================== 登录 ====================
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<Map<String, Object>> login(@RequestBody Admin admin) {
        Admin loginUser = adminService.login(admin.getUsername(), admin.getPassword());
        if (loginUser != null) {
            // 生成 JWT Token
            String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getUsername());
            // 清除密码
            loginUser.setPassword(null);

            // 返回 token + user
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("token", token);
            data.put("user", loginUser);
            return R.data(data);
        }
        return R.fail("用户名或密码错误");
    }

    // ==================== 注册 ====================
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<Void> register(@RequestBody Admin admin) {
        boolean success = adminService.register(admin);
        if (success) {
            return R.success("注册成功");
        }
        return R.fail("用户名已存在");
    }
}
