package com.bravos2k5.asmbe.controller;

import com.bravos2k5.asmbe.dto.CommentDto;
import com.bravos2k5.asmbe.dto.CommentRequest;
import com.bravos2k5.asmbe.service.CommentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/public/comments/{blogId}")
    public List<CommentDto> getCommentListByBlog(@PathVariable String blogId) {
        return commentService.getAllCommentByBlog(UUID.fromString(blogId));
    }

    @PostMapping("/api/private/comment")
    public CommentDto createNewComment(@RequestBody CommentRequest commentRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return commentService.createNewComment(commentRequest,username);
    }

}
