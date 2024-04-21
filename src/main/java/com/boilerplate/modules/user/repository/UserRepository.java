package com.boilerplate.modules.user.repository;

import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.boilerplate.modules.base.BaseRepository;
import com.boilerplate.modules.user.domain.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String username);
}
