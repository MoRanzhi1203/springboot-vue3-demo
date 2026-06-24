package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.NewEnergyProject;
import com.example.springbootdemobackend.mapper.NewEnergyProjectMapper;
import com.example.springbootdemobackend.service.NewEnergyProjectService;
import org.springframework.stereotype.Service;

@Service
public class NewEnergyProjectServiceImpl extends ServiceImpl<NewEnergyProjectMapper, NewEnergyProject> implements NewEnergyProjectService {
}
