package com.example.springbootdemobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springbootdemobackend.entity.CarbonTrade;
import com.example.springbootdemobackend.response.R;
import com.example.springbootdemobackend.service.CarbonTradeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carbonTrade")
@Tag(name = "碳交易管理")
public class CarbonTradeController {

    @Resource
    private CarbonTradeService carbonTradeService;

    @GetMapping("/list")
    @Operation(summary = "分页查询碳交易")
    public R<PageInfo<CarbonTrade>> list(
            @RequestParam(defaultValue = "1") Integer pagenum,
            @RequestParam(defaultValue = "10") Integer pagesize,
            @RequestParam(required = false) String tradeNo,
            @RequestParam(required = false) String tradeType) {
        LambdaQueryWrapper<CarbonTrade> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(tradeNo)) wrapper.like(CarbonTrade::getTradeNo, tradeNo);
        if (ObjectUtils.isNotEmpty(tradeType)) wrapper.eq(CarbonTrade::getTradeType, tradeType);
        wrapper.orderByDesc(CarbonTrade::getId);
        PageHelper.startPage(pagenum, pagesize);
        return R.data(new PageInfo<>(carbonTradeService.list(wrapper)));
    }

    @PostMapping("/add")
    @Operation(summary = "新增碳交易记录")
    public R<Void> add(@RequestBody CarbonTrade record) {
        carbonTradeService.save(record);
        return R.success("新增成功");
    }

    @PostMapping("/update")
    @Operation(summary = "编辑碳交易记录")
    public R<Void> update(@RequestBody CarbonTrade record) {
        carbonTradeService.updateById(record);
        return R.success("修改成功");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除碳交易记录")
    public R<Void> delete(@RequestParam Long id) {
        carbonTradeService.removeById(id);
        return R.success("删除成功");
    }
}
