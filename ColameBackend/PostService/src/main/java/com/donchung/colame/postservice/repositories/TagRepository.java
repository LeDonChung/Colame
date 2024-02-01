package com.donchung.colame.postservice.repositories;

import com.donchung.colame.postservice.POJO.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}
