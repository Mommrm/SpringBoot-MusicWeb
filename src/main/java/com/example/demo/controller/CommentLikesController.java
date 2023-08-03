package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CommentLikesController {
    @Autowired
    private CommentMapper commentMapper;

    @PostMapping("/updataCommentLikes")
    public int updataCommentLikes(@RequestBody Comment comment){
        try{
            System.out.println(comment);
            return commentMapper.updateLikes(comment);
        }catch (Exception e){
            System.out.println(e);
            return -1;
        }
    }
}
