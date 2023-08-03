package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AboutLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@ResponseBody
@RestController

public class LandController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/Land")
    public User Land(@RequestBody User user) throws Exception{

        AboutLogin aboutLogin = new AboutLogin();  //有关登录逻辑的实现
        User userInfo = new User();

        try{
            User i = userMapper.Land(user);  //验证用户
            if(i != null){
                user.setUserId(userMapper.findLandId(user.getAccount(),user.getPassword()));

                String token = aboutLogin.GetToken(user.getUserId());
                userInfo.setUserId(userMapper.Land(user).getUserId());
                userInfo.setUserName(userMapper.Land(user).getUserName());
                userInfo.setAccount(user.getAccount());
                userInfo.setPassword(user.getPassword());
                userInfo.setToken(token);

                System.out.println(userInfo);
                return userInfo;
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

}
