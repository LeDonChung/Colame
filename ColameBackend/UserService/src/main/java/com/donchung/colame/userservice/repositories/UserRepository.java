package com.donchung.colame.userservice.repositories;

import com.donchung.colame.userservice.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
