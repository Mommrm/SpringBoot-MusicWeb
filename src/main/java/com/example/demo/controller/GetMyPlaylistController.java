package com.example.demo.controller;

import com.example.demo.entity.PlayList;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class GetMyPlaylistController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/getMyPlaylist")
    public PlayList[] getMyPlaylist(@RequestParam("userId") String userId){
        try{
            return userMapper.getMyPlaylist(userId);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

}
