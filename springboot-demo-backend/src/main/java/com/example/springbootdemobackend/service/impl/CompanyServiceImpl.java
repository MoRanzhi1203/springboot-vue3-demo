package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.Company;
import com.example.springbootdemobackend.mapper.CompanyMapper;
import com.example.springbootdemobackend.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
}
