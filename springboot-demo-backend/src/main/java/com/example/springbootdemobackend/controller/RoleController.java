package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.Admin;
import com.example.springbootdemobackend.entity.Role;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.AdminService;
import com.example.springbootdemobackend.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    @Resource
    private AdminService adminService;

    @GetMapping("/list")
    @Operation(summary = "分页查询角色")
    public R<PageInfo<Role>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(roleName)) wrapper.like(Role::getRoleName, roleName);
        if (ObjectUtils.isNotEmpty(roleCode)) wrapper.like(Role::getRoleCode, roleCode);
        wrapper.orderByAsc(Role::getSortOrder);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(roleService.list(wrapper)));
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有角色")
    public R<List<Role>> all() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1).orderByAsc(Role::getSortOrder);
        return R.data(roleService.list(wrapper));
    }

    @PostMapping("/add")
    @Operation(summary = "新增角色")
    public R<Void> add(@RequestBody Role role) {
        LambdaQueryWrapper<Role> check = new LambdaQueryWrapper<>();
        check.eq(Role::getRoleCode, role.getRoleCode());
        if (roleService.count(check) > 0) return R.fail("角色编码已存在");
        roleService.save(role);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑角色")
    public R<Void> update(@RequestBody Role role) {
        roleService.updateById(role);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色")
    public R<Void> delete(@RequestParam Long id) {
        roleService.removeById(id);
        return R.success("删除成功");
    }

    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除角色")
    public R<Void> batchDelete(@RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return R.success("批量删除成功");
    }

    @GetMapping("/userRole")
    @Operation(summary = "查询用户角色ID")
    public R<Long> userRole(@RequestParam Long userId) {
        Admin admin = adminService.getById(userId);
        return R.data(admin != null ? admin.getRoleId() : null);
    }

    @PostMapping("/assignUserRole")
    @Operation(summary = "分配用户角色")
    public R<Void> assignUserRole(@RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long roleId = params.get("roleId");
        Admin admin = adminService.getById(userId);
        if (admin == null) return R.fail("用户不存在");
        admin.setRoleId(roleId);
        adminService.updateById(admin);
        return R.success("分配角色成功");
    }
}
