package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.dto.CommentDto;
import com.bravos2k5.asmbe.dto.CommentRequest;
import com.bravos2k5.asmbe.model.Blog;
import com.bravos2k5.asmbe.model.Comment;
import com.bravos2k5.asmbe.model.User;
import com.bravos2k5.asmbe.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BlogService blogService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, BlogService blogService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.blogService = blogService;
    }

    public List<CommentDto> getAllCommentByBlog(UUID blogId) {
        List<Comment> commentList = commentRepository.findAllByBlog_Id(blogId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentList.forEach(comment -> {
            commentDtoList.add(new CommentDto(comment.getUser().getName(),comment.getCreatedDate(),
                    comment.getUser().getAvatar(),comment.getContent()));
        });
        return commentDtoList;
    }

    public CommentDto createNewComment(CommentRequest commentRequest, String username) {
        User user = userService.findByUsername(username);
        Blog blog = blogService.findById(commentRequest.blogId());
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(commentRequest.content());
        comment.setBlog(blog);
        comment = commentRepository.saveAndFlush(comment);
        return new CommentDto(comment.getUser().getName(),comment.getCreatedDate(),comment.getUser().getAvatar(),comment.getContent());
    }

}
