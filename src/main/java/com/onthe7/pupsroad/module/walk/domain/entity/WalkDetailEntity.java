package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.CreateWalkDetailCommand;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.UpdateWalkDetailCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_walk_detail")
@NoArgsConstructor(access = PROTECTED)
public class WalkDetailEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer totalTime;

    private Double totalDistance;

    private String memo;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "walk_id")
    private WalkEntity walk;

    public static WalkDetailEntity from(CreateWalkDetailCommand command) {
        return WalkDetailEntity.builder()
                .walk(command.getWalk())
                .startTime(command.getStartTime())
                .endTime(command.getEndTime())
                .totalTime(command.getTotalTime())
                .totalDistance(command.getTotalDistance())
                .createdBy(command.getUser().getId())
                .updatedBy(command.getUser().getId())
                .build();
    }

    public void update(UpdateWalkDetailCommand command) {
        this.memo = command.getMemo();
        this.updatedBy = command.getUser().getId();
    }
}
