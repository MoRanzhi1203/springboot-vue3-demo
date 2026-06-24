package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.CarbonTrade;
import com.example.springbootdemobackend.mapper.CarbonTradeMapper;
import com.example.springbootdemobackend.service.CarbonTradeService;
import org.springframework.stereotype.Service;

@Service
public class CarbonTradeServiceImpl extends ServiceImpl<CarbonTradeMapper, CarbonTrade> implements CarbonTradeService {
}
