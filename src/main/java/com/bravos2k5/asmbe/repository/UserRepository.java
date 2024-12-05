package com.bravos2k5.asmbe.repository;

import com.bravos2k5.asmbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("select (count(u) > 0) from User u where upper(u.username) like upper(?1)")
    boolean existsByUsernameLikeIgnoreCase(String username);
}
