package com.onthe7.pupsroad.module.resource.domain.dto.request;

import com.onthe7.pupsroad.common.enums.ErrorCode;
import com.onthe7.pupsroad.common.exception.ResourceException;
import com.onthe7.pupsroad.common.exception.ResourceException.UnsupportedResourceTypeException;
import com.onthe7.pupsroad.common.exception.UIException;
import com.onthe7.pupsroad.common.util.ResourceUtil;
import com.onthe7.pupsroad.module.resource.domain.enums.ResourceType;
import com.onthe7.pupsroad.module.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

public class ResourceCommand {

    @Getter
    @Builder(access = PRIVATE)
    public static class CreateResourceCommand {
        private final UserEntity user;
        private final ResourceType type;
        private final String url;
        private final String originalFilename;

        public static CreateResourceCommand toCommand(UserEntity user, MultipartFile file, String url) {
            ResourceType resourceType = ResourceUtil.of(file);
            if (Objects.isNull(resourceType)) {
                throw new UnsupportedResourceTypeException();
            }
            return CreateResourceCommand.builder()
                    .user(user).type(resourceType).url(url)
                    .originalFilename(file.getOriginalFilename())
                    .build();
        }
    }

}
