package com.bravos2k5.asmbe.repository;

import com.bravos2k5.asmbe.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {



}
