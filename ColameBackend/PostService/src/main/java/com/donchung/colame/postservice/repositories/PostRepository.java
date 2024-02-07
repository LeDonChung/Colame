package com.donchung.colame.postservice.repositories;

import com.donchung.colame.postservice.POJO.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByStatus(boolean status, Pageable pageable);
    int countPostByStatusIs(boolean status);
    Optional<Post> findByPostCode(String postCode);
}
