package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_resource")
@NoArgsConstructor(access = PROTECTED)
public class WalkResourceEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private ResourceType type;

    private String url;

    private String originalFilename;

    private LocalDateTime createdAt;
}
