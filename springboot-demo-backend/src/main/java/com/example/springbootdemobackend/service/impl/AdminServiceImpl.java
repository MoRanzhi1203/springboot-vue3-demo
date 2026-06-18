package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.Admin;
import com.example.springbootdemobackend.mapper.AdminMapper;
import com.example.springbootdemobackend.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
