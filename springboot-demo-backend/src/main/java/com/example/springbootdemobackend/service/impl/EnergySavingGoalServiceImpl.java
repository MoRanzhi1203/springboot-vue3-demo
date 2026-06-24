package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.EnergySavingGoal;
import com.example.springbootdemobackend.mapper.EnergySavingGoalMapper;
import com.example.springbootdemobackend.service.EnergySavingGoalService;
import org.springframework.stereotype.Service;

@Service
public class EnergySavingGoalServiceImpl extends ServiceImpl<EnergySavingGoalMapper, EnergySavingGoal> implements EnergySavingGoalService {
}
