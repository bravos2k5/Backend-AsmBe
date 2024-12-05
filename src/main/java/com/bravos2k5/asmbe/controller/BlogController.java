package com.bravos2k5.asmbe.controller;

import com.bravos2k5.asmbe.dto.BlogDetail;
import com.bravos2k5.asmbe.dto.BlogDto;
import com.bravos2k5.asmbe.dto.BlogUpdateRequest;
import com.bravos2k5.asmbe.dto.CreateBlogRequest;
import com.bravos2k5.asmbe.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/api/public/blogs")
    public ResponseEntity<List<BlogDto>> getAllBlog() {
        return new ResponseEntity<>(blogService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/api/public/blog/{blogId}")
    public ResponseEntity<BlogDetail> getBlogDetail(@PathVariable String blogId) {
        return new ResponseEntity<>(blogService.getBlogDetailById(UUID.fromString(blogId)),HttpStatus.OK);
    }

    @GetMapping("/api/private/myblogs")
    public ResponseEntity<List<BlogDto>> getBlogsByUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(blogService.getBlogDtoByUser(username),HttpStatus.OK);
    }

    @PostMapping("/api/private/create-blog")
    public ResponseEntity<BlogDetail> createNewBlog(@RequestBody CreateBlogRequest blogCreator) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(blogService.createNewBlog(blogCreator,username),HttpStatus.OK);
    }

    @PutMapping("/api/private/update-blog")
    public ResponseEntity<BlogDetail> updateBlog(@RequestBody BlogUpdateRequest blogUpdateRequest) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new ResponseEntity<>(blogService.updateBlog(blogUpdateRequest,username),HttpStatus.OK);
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/api/private/delete-blog/{id}")
    public ResponseEntity<Boolean> deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
