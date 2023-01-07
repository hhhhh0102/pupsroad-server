package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand;
import com.onthe7.pupsroad.module.walk.domain.enums.WalkEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_walk_pet_event")
@NoArgsConstructor(access = PROTECTED)
public class WalkPetEventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private WalkEventType type;

    LocalDateTime eventTime;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "walk_to_pet_id")
    private WalkToPetEntity walkToPet;

    public static WalkPetEventEntity from(WalkCommand.CreateWalkPetEventCommand command) {
        return WalkPetEventEntity.builder()
                .walkToPet(command.getWalkToPet())
                .type(command.getType()).eventTime(command.getEventTime())
                .build();
    }

}
