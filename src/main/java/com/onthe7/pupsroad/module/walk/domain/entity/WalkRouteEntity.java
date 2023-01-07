package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.CreateWalkRouteCommand;
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
@Table(name = "tbl_walk_route")
@NoArgsConstructor(access = PROTECTED)
public class WalkRouteEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String route;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "walk_id")
    private WalkEntity walk;

    public static WalkRouteEntity from(CreateWalkRouteCommand command) {
        return WalkRouteEntity.builder()
                .route(command.getRoute())
                .walk(command.getWalk())
                .createdBy(command.getUser().getId())
                .updatedBy(command.getUser().getId()).build();
    }
}
