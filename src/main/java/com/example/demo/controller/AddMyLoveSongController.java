package com.example.demo.controller;


import com.example.demo.entity.PlayList;
import com.example.demo.entity.Song;
import com.example.demo.entity.songUser;
import com.example.demo.mapper.PlayListMapper;
import com.example.demo.mapper.SongMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@ResponseBody
public class AddMyLoveSongController {

    @Autowired
    private SongMapper songMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PlayListMapper playListMapper;
    /*
    * 歌曲添加到对应用户的歌单，选择歌单添加功能还不完善
    * 前端应该把用户选择的歌单传给后端
    * 后端要根据选择的歌单Id进行添加歌曲
    *
    * */
    @PostMapping("/insertMusic")
    public int searchSong(@RequestBody songUser song) throws Exception{
        System.out.println(song);
        try{
            String PlaylistName = userMapper.getPlaylistName(song.getPlaylistID()); //通过歌单Id查询歌单名字
            PlayList selectPlaylist = new PlayList(song.getPlaylistID(),song.getSongId(),PlaylistName);//被选择插入的歌单 ，临时变量
            Song songAdd = new Song(); //创建添加的歌曲变量

            songAdd.setSongName(song.getSongName());
            songAdd.setSongId(song.getSongId());
            songAdd.setSongUrl(song.getSongUrl());
            songAdd.setSongSinger(song.getSongSinger());
            songAdd.setPlaylistID(song.getPlaylistID());

            int check = addSong(songAdd,selectPlaylist);
            System.out.println("check: " + check);
            if(check > 0){
                return 1;
            }else{
                return 0;//未更新数据库
            }
        }catch (Exception e) {
            System.out.println("异常: " + e);
            return 0;
        }
    }

    private int addSong(Song songAdd , PlayList selectPlaylist){
        int addSong_check = songMapper.insert(songAdd);
        int addPlaylist_check = playListMapper.insertSong(selectPlaylist); //在歌单里面添加歌曲
        return addPlaylist_check + addSong_check;
    }
}
