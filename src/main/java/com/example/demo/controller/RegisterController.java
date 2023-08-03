package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.mapper.PlayListMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController  //Restful风格
@ResponseBody

public class RegisterController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlayListMapper playListMapper;

    @PostMapping("/Register")
    public boolean RegisterUser(@RequestBody User user){
        String[] tempIDs = userMapper.findAllId();
        user.setUserId(String.valueOf(tempIDs[tempIDs.length-1] + 1)); //获取当前最后一位用户ID 往后退一位赋予给新注册用户
        //判断是否已经存在用户名和账号都相同的账户
        if(!userMapper.isExistEqualUser(user.getUserName() , user.getAccount()).isEmpty()){
            System.out.println("有用户");
            return false;
        }

        //满足密码要求
        if(Password_Check(user.getPassword())){
            try{
                if(addUser(user)){
                    return true;
                }
                else {
                    return false;
                }
            }catch (Exception e){
                System.out.println("注册异常!");
                return false;
            }
        }
        System.out.println("注册失败!");
        return false;
    }

    private boolean Password_Check(String password){
        int isEnglish = 0;//记录英文字符出现次数
        if(password.length() < 8) //密码小于8位
            return false;
        for(int i = 0;i<password.length();i++){
            char temp = password.charAt(i);
            if((temp >= 65 && temp <= 90) || (temp >= 97 && temp <= 122)) {
                isEnglish++;
            }
        }
        if(isEnglish < 2){ //英文字符少于两位
            return false;
        }
        //其余情况均为符合密码要求
        return true;
    }

    @Transactional
    private boolean addUser(User user){
        try {
            //添加用户
            userMapper.Register(user);
            //创建默认歌单 默认的歌单号为用户Id
            playListMapper.createPlaylist(user.getUserId(), user.getUserName() + "的歌单", user.getUserId() );
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }
}


