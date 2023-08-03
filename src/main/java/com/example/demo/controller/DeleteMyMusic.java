package com.example.demo.controller;


import com.example.demo.mapper.PlayListMapper;
import com.example.demo.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DeleteMyMusic {

    @Autowired
    private PlayListMapper playListMapper;
    @Autowired
    private SongMapper songMapper;

    @PostMapping("/deleteMyMusic")
    public boolean deleteMyMusic(@RequestParam("songId") String songId, @RequestParam("playlistId") String playlistId ){
        if(playListMapper.deleteSong_playlist(playlistId,songId) && songMapper.deleteSong_song(songId,playlistId) ){
            return true;
        }
        else{
            return false;
        }
    }
}
