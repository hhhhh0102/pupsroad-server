package com.onthe7.pupsroad.module.user.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.common.enums.UserRole;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserSignUpDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.onthe7.pupsroad.common.enums.UserRole.ROLE_USER;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@SuperBuilder
@Table(name = "tbl_user")
@NoArgsConstructor(access = PROTECTED)
public class UserEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String clientId;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(STRING)
    private UserRole role;

    @PrePersist
    protected void onPersist() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static UserEntity createUser(UserSignUpDto userSignUpDto) {
        return UserEntity.builder()
                .clientId(RandomStringUtils.randomAlphanumeric(30))
                .nickname(userSignUpDto.getNickname()).role(ROLE_USER)
                .build();
    }

    public void deleteUser(Long userId) {
        this.deleted = YesNo.Y;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
