package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.Permission;
import com.example.springbootdemobackend.entity.RolePermission;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.PermissionService;
import com.example.springbootdemobackend.service.RolePermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RolePermissionService rolePermissionService;

    @GetMapping("/list")
    @Operation(summary = "分页查询权限")
    public R<PageInfo<Permission>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) String permissionCode,
            @RequestParam(required = false) String module) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(permissionName)) wrapper.like(Permission::getPermissionName, permissionName);
        if (ObjectUtils.isNotEmpty(permissionCode)) wrapper.like(Permission::getPermissionCode, permissionCode);
        if (ObjectUtils.isNotEmpty(module)) wrapper.eq(Permission::getModule, module);
        wrapper.orderByAsc(Permission::getSortOrder);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(permissionService.list(wrapper)));
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有权限")
    public R<List<Permission>> all() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getStatus, 1).orderByAsc(Permission::getSortOrder);
        return R.data(permissionService.list(wrapper));
    }

    @PostMapping("/add")
    @Operation(summary = "新增权限")
    public R<Void> add(@RequestBody Permission permission) {
        LambdaQueryWrapper<Permission> check = new LambdaQueryWrapper<>();
        check.eq(Permission::getPermissionCode, permission.getPermissionCode());
        if (permissionService.count(check) > 0) return R.fail("权限编码已存在");
        permissionService.save(permission);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑权限")
    public R<Void> update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除权限")
    public R<Void> delete(@RequestParam Long id) {
        permissionService.removeById(id);
        return R.success("删除成功");
    }

    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除权限")
    public R<Void> batchDelete(@RequestBody List<Long> ids) {
        permissionService.removeByIds(ids);
        return R.success("批量删除成功");
    }

    @GetMapping("/rolePermissions")
    @Operation(summary = "查询角色已有权限ID列表")
    public R<List<Long>> rolePermissions(@RequestParam Long roleId) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<Long> ids = rolePermissionService.list(wrapper).stream()
                .map(RolePermission::getPermissionId).collect(Collectors.toList());
        return R.data(ids);
    }

    @PostMapping("/assignRolePermissions")
    @Operation(summary = "分配角色权限")
    public R<Void> assignRolePermissions(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("roleId").toString());
        @SuppressWarnings("unchecked")
        List<Integer> permissionIdsRaw = (List<Integer>) params.get("permissionIds");
        // 先删除角色原有权限
        LambdaQueryWrapper<RolePermission> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionService.remove(delWrapper);
        // 再批量插入新权限
        if (permissionIdsRaw != null && !permissionIdsRaw.isEmpty()) {
            List<RolePermission> list = permissionIdsRaw.stream().map(pid -> {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(pid.longValue());
                return rp;
            }).collect(Collectors.toList());
            rolePermissionService.saveBatch(list);
        }
        return R.success("分配权限成功");
    }
}
