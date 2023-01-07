package com.onthe7.pupsroad.module.resource.domain.service;

import com.onthe7.pupsroad.module.resource.domain.dto.request.ResourceCommand.CreateResourceCommand;
import com.onthe7.pupsroad.module.resource.domain.entity.ResourceEntity;
import com.onthe7.pupsroad.module.resource.infrastructure.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceCommandService {

    private final ResourceRepository resourceRepository;

    public ResourceEntity createResource(CreateResourceCommand command) {
        ResourceEntity resource = ResourceEntity.from(command);
        resourceRepository.save(resource);
        return resource;
    }
}
