package com.donchung.colame.userservice.POJO;

import com.donchung.colame.userservice.utils.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private List<Role> roles;

    @Transient
    public UserResponseDTO toUserResponseDto() {
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }
}
