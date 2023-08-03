package com.example.demo.controller;


import com.example.demo.mapper.PlayListMapper;
import com.example.demo.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class deletePlaylistController {
    @Autowired
    private PlayListMapper playlistMapper;
    @Autowired
    private SongMapper songMapper;

    @PostMapping("/deletePlaylist")
    public int deletePlaylist(@RequestParam("playlistId") String playlistId , @RequestParam("userId") String userId){
        //删除默认歌单时候会返回-1错误
        if(userId.equals(playlistId)){
            return -1;
        }else{
            //先删除所删除歌单中的所有歌曲
            songMapper.deleteAllSong_playlist(playlistId);
            return playlistMapper.deleteUser_playlist(playlistId , userId);
        }
    }
}
