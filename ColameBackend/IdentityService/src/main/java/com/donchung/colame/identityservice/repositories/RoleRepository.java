package com.donchung.colame.identityservice.repositories;

import com.donchung.colame.identityservice.POJO.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByCode(String code);
}
