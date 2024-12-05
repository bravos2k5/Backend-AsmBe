package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.dto.*;
import com.bravos2k5.asmbe.model.Blog;
import com.bravos2k5.asmbe.model.User;
import com.bravos2k5.asmbe.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserService userService;

    @Autowired
    public BlogService(BlogRepository blogRepository, UserService userService) {
        this.blogRepository = blogRepository;
        this.userService = userService;
    }

    public Blog findById(UUID id) {
        return blogRepository.findById(id).orElse(null);
    }

    public BlogDetail createNewBlog(CreateBlogRequest blogCreator, String username) {
        Blog blog = new Blog();
        User user = userService.findByUsername(username);
        if(user == null) {
            throw new IllegalArgumentException("Invalid author");
        }
        blog.setUser(user);
        blog.setTitle(blogCreator.title());
        blog.setContent(blogCreator.content());
        blog.setImage(blogCreator.image());
        blog = blogRepository.saveAndFlush(blog);
        BlogAuthor blogAuthor = new BlogAuthor(user.getName(),user.getAvatar());
        return new BlogDetail(blog.getId(),blog.getTitle(),blogAuthor,blog.getCreatedDate(),blog.getImage(),blog.getContent());
    }

    public BlogDetail updateBlog(BlogUpdateRequest blogUpdateRequest, String username) throws IllegalAccessException {
        Blog blog = blogRepository.findById(UUID.fromString(blogUpdateRequest.id())).orElse(null);
        if(blog == null) {
            throw new IllegalArgumentException("Invalid blog");
        }
        if(!blog.getUser().getUsername().equals(username)) {
            throw new IllegalAccessException("Invalid user");
        }
        blog.setTitle(blogUpdateRequest.title());
        blog.setContent(blogUpdateRequest.content());
        blog.setImage(blogUpdateRequest.image());
        blog = blogRepository.saveAndFlush(blog);
        BlogAuthor blogAuthor = new BlogAuthor(blog.getUser().getName(),blog.getUser().getAvatar());
        return new BlogDetail(blog.getId(),blog.getTitle(),blogAuthor,blog.getCreatedDate(),blog.getImage(),blog.getContent());
    }

    public List<BlogDto> getAll() {
        List<Blog> blogList = blogRepository.findAll();
        List<BlogDto> dtoList = new ArrayList<>();
        blogList.forEach(blog -> {
            dtoList.add(new BlogDto(blog.getId(),blog.getTitle(),
                    blog.getContent().substring(0,Math.min(blog.getContent().length(),90)),
                    blog.getImage()));
        });
        return dtoList;
    }

    public BlogDetail getBlogDetailById(UUID id) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(blogOptional.isEmpty()) {
            throw new IllegalArgumentException("Blog is not exist");
        }
        Blog blog = blogOptional.get();
        BlogAuthor author = new BlogAuthor(blog.getUser().getName(),blog.getUser().getAvatar());
        return new BlogDetail(blog.getId(),blog.getTitle(),author,blog.getCreatedDate(),blog.getImage(),blog.getContent());
    }

    public List<BlogDto> getBlogDtoByUser(String username) {
        List<Blog> blogList = blogRepository.findAllByUser_Username(username);
        List<BlogDto> dtoList = new ArrayList<>();
        blogList.forEach(blog -> {
            dtoList.add(new BlogDto(blog.getId(),blog.getTitle(),
                    blog.getContent().substring(0,Math.min(blog.getContent().length(),30)),
                    blog.getImage()));
        });
        return dtoList;
    }

    public void deleteBlog(String id) {
        blogRepository.deleteById(UUID.fromString(id));
    }
}
