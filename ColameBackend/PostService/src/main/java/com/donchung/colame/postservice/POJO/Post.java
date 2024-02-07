package com.donchung.colame.postservice.POJO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends Auditable<String> {
    @Id
    @Column(name = "post_id")
    private String id;

    @Column(name = "post_code", nullable = false, unique = true)
    private String postCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "viewer", columnDefinition = "DEFAULT 0")
    private Integer viewer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    private Collection<Tag> tags;

    @Column(name = "status", columnDefinition = "DEFAULT TRUE")
    private boolean status;

    @Column(name = "username")
    private String username;
}
