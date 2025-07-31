package com.microservice.post.service;

import com.microservice.post.config.RestTemplateConfig;
import com.microservice.post.entity.Post;
import com.microservice.post.exception.ResourceNotFound;
import com.microservice.post.payload.PostDto;
import com.microservice.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RestTemplateConfig restTemplate;

    public Post savePost(Post post){
       // post.setId(generateUniqueLongId());
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    public Post findPostById (long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with id:- " + id));
        return post;
    }

    public PostDto getPostWithComments(long id) {
        Post post = postRepository.findById(id).get();
        ArrayList comments = restTemplate.getRestTemplate()
                .getForObject("http://localhost:8082/api/comments/" + id, ArrayList.class);
        PostDto postDto = new PostDto();
        postDto.setId(postDto.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        postDto.setComments(comments);
        return postDto;


    }
}
