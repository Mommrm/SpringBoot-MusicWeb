package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AboutLogin;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetUserInfoController {
    @Autowired
    private UserMapper userMapper;
    private AboutLogin aboutLogin = new AboutLogin();

    private static String secret = "abcdfghiabcdfghiabcdfghiabcdfghi";

    @PostMapping("/getUserInfo")
    public User BytokenGetInfo(@RequestParam("token") String token){
        return decrypt(token);
    }

    //解密用户信息
    private User decrypt(String token){
        return userMapper.findUser(Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).
                getBody().getSubject());
    }
}
