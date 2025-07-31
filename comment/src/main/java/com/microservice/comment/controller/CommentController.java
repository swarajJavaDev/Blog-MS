package com.microservice.comment.controller;

import com.microservice.comment.entity.Comment;
import com.microservice.comment.service.CommentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    //http://localhost:8082/api/comments
    @PostMapping
    @CircuitBreaker(name = "postServiceCircuitBreaker", fallbackMethod = "postFallback")
    @Retry(name = "postServiceRetry")
    public ResponseEntity <Comment> saveComment(@RequestBody Comment comment){
        Comment comment1 = commentService.saveComment(comment);
        return new ResponseEntity<>(comment1, HttpStatus.CREATED);

    }
    //http://localhost:8082/api/comments/id
    @GetMapping("/{id}")

    public List<Comment> getAllCommentsByPostId(@PathVariable long id){           //here id = postId
        List<Comment> comments = commentService.getAllCommentsById(id);
        return comments;

    }


}
