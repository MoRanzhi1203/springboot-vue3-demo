package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.Role;
import com.example.springbootdemobackend.mapper.RoleMapper;
import com.example.springbootdemobackend.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
