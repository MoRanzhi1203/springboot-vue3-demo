package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.CarbonEmission;
import com.example.springbootdemobackend.mapper.CarbonEmissionMapper;
import com.example.springbootdemobackend.service.CarbonEmissionService;
import org.springframework.stereotype.Service;

@Service
public class CarbonEmissionServiceImpl extends ServiceImpl<CarbonEmissionMapper, CarbonEmission> implements CarbonEmissionService {
}
