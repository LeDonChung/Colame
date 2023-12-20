package com.donchung.colame.userservice.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "users")
public class User extends Auditable<String> implements Serializable {
    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Column(columnDefinition = "MEDIUMBLOB")
    private String avatar;

    private String cover;

    private Boolean status;
}
