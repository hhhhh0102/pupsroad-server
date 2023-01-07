package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.CreateWalkToResourceCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_walk_to_resource")
@NoArgsConstructor(access = PROTECTED)
public class WalkToResourceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "walk_id")
    private WalkEntity walk;


    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "resource_id")
    private WalkResourceEntity resource;

    public static WalkToResourceEntity from(CreateWalkToResourceCommand command) {
        return WalkToResourceEntity.builder()
                .walk(command.getWalk()).resource(command.getResource())
                .createdBy(command.getUser().getId()).updatedBy(command.getUser().getId())
                .build();
    }
}
