package com.bravos2k5.asmbe.repository;

import com.bravos2k5.asmbe.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog, UUID> {

    List<Blog> findAllByUser_Username(String userUsername);
}