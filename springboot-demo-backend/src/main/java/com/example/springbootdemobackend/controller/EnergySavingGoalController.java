package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springbootdemobackend.entity.EnergySavingGoal;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.EnergySavingGoalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/energySavingGoal")
@Tag(name = "节能减排目标管理")
public class EnergySavingGoalController {

    @Resource
    private EnergySavingGoalService energySavingGoalService;

    @GetMapping("/list")
    @Operation(summary = "分页查询节能减排目标")
    public R<PageInfo<EnergySavingGoal>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String goalType,
            @RequestParam(required = false) Integer targetYear) {
        LambdaQueryWrapper<EnergySavingGoal> wrapper = new LambdaQueryWrapper<>();
        if (companyId != null) wrapper.eq(EnergySavingGoal::getCompanyId, companyId);
        if (goalType != null && !goalType.isEmpty()) wrapper.eq(EnergySavingGoal::getGoalType, goalType);
        if (targetYear != null) wrapper.eq(EnergySavingGoal::getTargetYear, targetYear);
        wrapper.orderByDesc(EnergySavingGoal::getId);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(energySavingGoalService.list(wrapper)));
    }

    @PostMapping("/add")
    @Operation(summary = "新增节能减排目标")
    public R<Void> add(@RequestBody EnergySavingGoal record) {
        energySavingGoalService.save(record);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑节能减排目标")
    public R<Void> update(@RequestBody EnergySavingGoal record) {
        energySavingGoalService.updateById(record);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除节能减排目标")
    public R<Void> delete(@RequestParam Long id) {
        energySavingGoalService.removeById(id);
        return R.success("删除成功");
    }
}
