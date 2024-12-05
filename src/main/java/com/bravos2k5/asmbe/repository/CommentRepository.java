package com.bravos2k5.asmbe.repository;

import com.bravos2k5.asmbe.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByBlog_Id(UUID blogId);
}