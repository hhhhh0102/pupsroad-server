package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.CreateWalkCommand;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.FINISHED;
import static com.onthe7.pupsroad.module.walk.domain.enums.WalkStatus.IN_PROGRESS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_walk")
@NoArgsConstructor(access = PROTECTED)
public class WalkEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Double startLongitude;

    private Double startLatitude;

    @Enumerated(STRING)
    private WalkStatus status;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = LAZY, mappedBy = "walk")
    private List<WalkToResourceEntity> walkToResourceList;

    @OneToOne(fetch = LAZY, mappedBy = "walk")
    private WalkDetailEntity walkDetail;

    public static WalkEntity from(CreateWalkCommand command) {
        return WalkEntity.builder()
                .user(command.getUser()).status(IN_PROGRESS)
                .startLongitude(command.getStartLongitude())
                .startLatitude(command.getStartLatitude())
                .createdBy(command.getUser().getId())
                .updatedBy(command.getUser().getId()).build();
    }

    public void finishWalk(Long userId) {
        this.status = FINISHED;
        this.updatedBy = userId;
    }
}
