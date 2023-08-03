package com.example.demo.controller;

import com.example.demo.entity.Song;
import com.example.demo.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class GetMyDataController {

    @Autowired
    private SongMapper songMapper;

    @PostMapping("/getMySongData")
    public List<Song> GetMyData(@RequestParam("playlistId") String playlistId) throws Exception{
        return songMapper.GetMySongData(playlistId);
    }

}
