package com.example.springbootdemobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootdemobackend.entity.Admin;

/**
 * 用户服务接口
 */
public interface AdminService extends IService<Admin> {

    /**
     * 用户登录
     * @param username 账号
     * @param userpwd  密码
     * @return 登录成功返回用户对象，失败返回 null
     */
    Admin login(String username, String userpwd);

    /**
     * 用户注册
     * @param admin 用户信息
     * @return true=注册成功，false=用户名已存在
     */
    boolean register(Admin admin);
}
