package com.microservice.post.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private long commentId;
    private String name;
    private String email;
    private String body;
    private String id;      //postId
}
