package com.example.demo.controller;


import com.example.demo.mapper.PlayListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin
public class AddPlaylistController {
    @Autowired
    private PlayListMapper playListMapper;


    @PostMapping("/addPlaylist")
    public boolean createMyPlaylist(@RequestParam("playlistId") String playlistId , @RequestParam("playlistName") String playlistName
            ,@RequestParam("userId") String userId ){
        try{//playlistId需要经过处理
            playlistId = getMyPlaylistId(playlistId);
            int insert_row = playListMapper.createPlaylist(playlistId,playlistName,userId);
            if(insert_row >= 1){
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //Id是用户id＋当前日期Id
    private String getMyPlaylistId(String playlistId){
        //获取当前时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        //转换为String类型
        String originalString = String.valueOf(currentDateTime);
        // 将原始字符串解析为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(originalString, DateTimeFormatter.ISO_DATE_TIME);
        // 使用自定义格式化模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmm");
        // 格式化日期时间对象为处理后的字符串
        String processedString = dateTime.format(formatter);
        //拼接后返回
        return playlistId + processedString;
    }
}
