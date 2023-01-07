package com.onthe7.pupsroad.module.walk.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.pet.domain.entity.PetEntity;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.CreateWalkToPetCommand;
import com.onthe7.pupsroad.module.walk.domain.dto.request.WalkCommand.UpdateWalkToPetRecordsCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_walk_to_pet")
@NoArgsConstructor(access = PROTECTED)
public class WalkToPetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Double calorie;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "walk_id")
    private WalkEntity walk;


    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    public static List<WalkToPetEntity> from(CreateWalkToPetCommand command) {
        WalkEntity walk = command.getWalk();
        Long userId = command.getUser().getId();
        return command.getPetList()
                .stream().map(pet -> WalkToPetEntity.builder()
                        .walk(walk).createdBy(userId).updatedBy(userId)
                        .calorie(0D).pet(pet).build())
                .collect(Collectors.toList());
    }

    public void updateRecord(UpdateWalkToPetRecordsCommand command) {
        this.calorie = command.getCalorie();
    }
}
