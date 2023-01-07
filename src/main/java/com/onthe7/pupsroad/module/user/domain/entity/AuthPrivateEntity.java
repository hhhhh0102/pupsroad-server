package com.onthe7.pupsroad.module.user.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.common.enums.AuthProviderType;
import com.onthe7.pupsroad.common.enums.YesNo;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto.UserSignUpDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@SuperBuilder
@Table(name = "tbl_auth_private")
@NoArgsConstructor(access = PROTECTED)
public class AuthPrivateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String phone;

    @Enumerated(STRING)
    private AuthProviderType provider;

    private String principalId;


    @Enumerated(STRING)
    private YesNo verified;

    @ToString.Exclude
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static AuthPrivateEntity createOAuthPrivateEntity(UserSignUpDto userSignUpDto, UserEntity user) {
        return AuthPrivateEntity.builder()
                .principalId(userSignUpDto.getPrincipalId())
                .email(userSignUpDto.getEmail())
                .phone(userSignUpDto.getPhone())
                .provider(userSignUpDto.getProvider()).verified(YesNo.Y)
                .user(user).build();
    }

    public void deleteOAuthPrivateEntity(Long userId) {
        this.deleted = YesNo.Y;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
