package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class GetSongCommentController {
    @Autowired
    private CommentMapper commentMapper;

    @PostMapping("/getComments")
    public List<Comment> getComments(String songId){
        try{
            List<Comment> comments = commentMapper.getSongComment(songId);
            System.out.println(comments);
            return comments;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
