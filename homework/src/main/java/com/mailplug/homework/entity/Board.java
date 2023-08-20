package com.mailplug.homework.entity;

import lombok.*;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_at")
    private String createAt;


    @Column(name = "update_at")
    private String updateAt;

    @Column(name = "username")
    private String username;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0")
    private int viewCount;

    @Column(name = "category")
    private String category;

}
