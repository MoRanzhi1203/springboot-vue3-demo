package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.NewEnergyProject;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.NewEnergyProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/newEnergyProject")
@Tag(name = "新能源项目管理")
public class NewEnergyProjectController {

    @Resource
    private NewEnergyProjectService newEnergyProjectService;

    @GetMapping("/list")
    @Operation(summary = "分页查询新能源项目")
    public R<PageInfo<NewEnergyProject>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<NewEnergyProject> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(projectName)) wrapper.like(NewEnergyProject::getProjectName, projectName);
        if (ObjectUtils.isNotEmpty(projectType)) wrapper.eq(NewEnergyProject::getProjectType, projectType);
        if (ObjectUtils.isNotEmpty(status)) wrapper.eq(NewEnergyProject::getStatus, status);
        wrapper.orderByDesc(NewEnergyProject::getId);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(newEnergyProjectService.list(wrapper)));
    }

    @PostMapping("/add")
    @Operation(summary = "新增新能源项目")
    public R<Void> add(@RequestBody NewEnergyProject record) {
        newEnergyProjectService.save(record);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑新能源项目")
    public R<Void> update(@RequestBody NewEnergyProject record) {
        newEnergyProjectService.updateById(record);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除新能源项目")
    public R<Void> delete(@RequestParam Long id) {
        newEnergyProjectService.removeById(id);
        return R.success("删除成功");
    }
}
