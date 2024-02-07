package com.donchung.colame.postservice.repositories;

import com.donchung.colame.postservice.POJO.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findByCode(String code);

    List<Tag> findByStatus(boolean status);
}
