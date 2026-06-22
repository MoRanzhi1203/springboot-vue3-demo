package com.example.springbootdemobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdemobackend.entity.Admin;
import com.example.springbootdemobackend.mapper.AdminMapper;
import com.example.springbootdemobackend.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    /**
     * 登录：根据 username 和 userpwd 查询用户
     */
    @Override
    public Admin login(String username, String userpwd) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username)
               .eq(Admin::getPassword, userpwd);
        return this.getOne(wrapper);
    }

    /**
     * 注册：检查 username 是否已存在，不存在则保存
     */
    @Override
    public boolean register(Admin admin) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, admin.getUsername());
        long count = this.count(wrapper);
        if (count > 0) {
            return false; // 用户名已存在
        }
        return this.save(admin);
    }
}
