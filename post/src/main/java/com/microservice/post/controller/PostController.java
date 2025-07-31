package com.microservice.post.controller;

import com.microservice.post.entity.Post;
import com.microservice.post.payload.PostDto;
import com.microservice.post.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    //http://localhost:8081/api/post
    @PostMapping
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        Post newPost = postService.savePost(post);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    //http://localhost:8081/api/post/1

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPostById(@PathVariable("id") long id){
        Post postById = postService.findPostById(id);
        return new ResponseEntity<>(postById, HttpStatus.OK);
    }

    //http://localhost:8081/api/post/1/comments

    @GetMapping("/{id}/comments")
    @CircuitBreaker(name = "commentServiceCircuitBreaker", fallbackMethod = "commentFallback")
    @Retry(name = "commentServiceRetry")
    public ResponseEntity<PostDto> getPostWithComments(@PathVariable long id){
        PostDto postDto = postService.getPostWithComments(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);

    }

    public ResponseEntity<PostDto> commentFallback(long id, Exception ex){
        System.out.println("Fallback is executed service is down: "+ex);
        ex.printStackTrace();

        PostDto dto = new PostDto();
        dto.setTitle("Service is down");
        dto.setDescription("Service is down");
        dto.setContent("Service is down");
        return  new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);

    }

}
