package com.ss.heartlinkapi.search.entity;

import com.ss.heartlinkapi.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "search")
public class SearchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer searchId;
    @JoinColumn
    @ManyToOne
    private UserEntity user_id;
    private String type;
    private String keyword;
    private LocalDateTime createAt;
}
