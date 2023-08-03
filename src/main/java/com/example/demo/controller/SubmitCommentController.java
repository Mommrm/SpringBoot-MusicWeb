package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SubmitCommentController {
    @Autowired
    private CommentMapper commentMapper;

    @PostMapping("/submitComment")
    public int submitComment(@RequestBody Comment comment){
        System.out.println(comment.getComment());
        comment.setComment_date();
        comment.setCommentLikes(0);
        comment.setUserName(comment.getUserName());
        try {
            return commentMapper.insertComment(comment);
        }catch (Exception e){
            System.out.println(e);
            return -1; //-1为系统异常，提示给用户是服务器问题
        }
    }
}
