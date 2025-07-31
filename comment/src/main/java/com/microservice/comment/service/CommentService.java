package com.microservice.comment.service;

import com.microservice.comment.config.RestTemplateConfig;
import com.microservice.comment.entity.Comment;
import com.microservice.comment.payload.Post;
import com.microservice.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private RestTemplateConfig restTemplate;
    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment){
        Post post = restTemplate.getRestTemplate()
                .getForObject("http://localhost:8081/api/post/" + comment.getId(), Post.class);   //getId = getPostId
        if (post!=null) {
            Comment savedComment = commentRepository.save(comment);
            return savedComment;
        }else {
            return null;
        }

    }

    public List<Comment> getAllCommentsById(long id) {
        List<Comment> comments = commentRepository.findById(id);
        return comments;
    }
}
