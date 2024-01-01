package com.donchung.colame.identityservice.POJO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @Column(name = "role_id")
    private String id;

    private String name;

    private String code;
}
