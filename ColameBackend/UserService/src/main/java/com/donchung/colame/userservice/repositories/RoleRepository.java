package com.donchung.colame.userservice.repositories;

import com.donchung.colame.userservice.POJO.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByCode(String code);
}
