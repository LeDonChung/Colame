package com.donchung.colame.identityservice.repositories;

import com.donchung.colame.identityservice.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
