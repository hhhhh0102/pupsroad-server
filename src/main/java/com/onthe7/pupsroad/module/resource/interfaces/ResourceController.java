package com.onthe7.pupsroad.module.resource.interfaces;

import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.Response;
import com.onthe7.pupsroad.module.resource.application.ResourceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.onthe7.pupsroad.module.resource.interfaces.ResourceDto.CreateResourceResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/resources")
public class ResourceController {

    private final ResourceFacade resourceFacade;
    private final RequestIdGenerator requestIdGenerator;

    @PostMapping
    @ResponseBody
    public Response<CreateResourceResponse> createResource(
            @RequestPart(value = "files") MultipartFile file) {
        String requestId = requestIdGenerator.getRequestId();
        Long resourceId = resourceFacade.createResource(file);
        return Response.success(requestId, CreateResourceResponse.success(resourceId));
    }
}
