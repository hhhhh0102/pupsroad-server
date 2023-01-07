package com.onthe7.pupsroad.module.pet.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand.CreatePetProfileCommand;
import com.onthe7.pupsroad.module.pet.domain.enums.PetGender;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.onthe7.pupsroad.module.pet.domain.dto.request.PetCommand.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_pet")
@NoArgsConstructor(access = PROTECTED)
public class PetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String breed;

    private LocalDateTime birthdate;

    private Double weight;

    @Enumerated(STRING)
    private PetGender gender;

    @Enumerated(STRING)
    private YesNo neutralized;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ToString.Exclude
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "profile_resource_id")
    private PetResourceEntity petResource;

    public static PetEntity from(CreatePetProfileCommand command) {
        return PetEntity.builder()
                .user(command.getUser())
                .name(command.getName()).breed(command.getBreed())
                .birthdate(command.getBirthdate()).weight(command.getWeight())
                .gender(command.getGender()).neutralized(command.getNeutralized())
                .petResource(command.getPetResource())
                .createdBy(command.getUser().getId())
                .updatedBy(command.getUser().getId())
                .build();
    }

    public void updatePetProfile(UpdatePetProfileCommand command) {
        this.name = command.getName();
        this.breed = command.getBreed();
        this.birthdate = command.getBirthdate();
        this.weight = command.getWeight();
        this.gender = command.getGender();
        this.neutralized = command.getNeutralized();
        this.petResource = command.getPetResource();
        this.updatedBy = command.getUser().getId();
    }

    public void deletePetProfile(Long userId) {
        this.deleted = YesNo.Y;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
