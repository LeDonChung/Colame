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
public class Post extends Auditable<String>{
    @Id
    @Column(name = "post_id")
    private String id;

    private String title;

    private String summary;

    private String content;

    private Integer viewer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
    private Collection<Tag> tags;
}
