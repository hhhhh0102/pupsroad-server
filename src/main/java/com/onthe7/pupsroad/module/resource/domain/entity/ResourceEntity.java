package com.onthe7.pupsroad.module.resource.domain.entity;

import com.onthe7.pupsroad.common.domain.entity.BaseEntity;
import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static com.onthe7.pupsroad.module.resource.domain.dto.request.ResourceCommand.CreateResourceCommand;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_resource")
@NoArgsConstructor(access = PROTECTED)
public class ResourceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private ResourceType type;

    private String url;

    private String originalFilename;

    public static ResourceEntity from(CreateResourceCommand command) {
        return ResourceEntity.builder()
                .type(command.getType())
                .url(command.getUrl())
                .originalFilename(command.getOriginalFilename())
                .createdBy(command.getUser().getId())
                .updatedBy(command.getUser().getId())
                .build();
    }
}
