package com.onthe7.pupsroad.module.walk.interfaces;

import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.Response;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.walk.application.WalkFacade;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkDetail;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.WalkLocationInfo;
import com.onthe7.pupsroad.module.walk.interfaces.WalkDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/walks")
public class WalkController {

    private final RequestIdGenerator requestIdGenerator;
    private final WalkFacade walkFacade;

    /**
     * 산책 시작
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Response<CreateWalkResponse> startWalk(
            @RequestBody @Valid StartWalkRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        Long walkId = walkFacade.startWalk(request);
        return Response.success(requestId, CreateWalkResponse.success(walkId));
    }

    /**
     * 산책 경로 추가
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{walkId}/route")
    public Response<ResultVo> createWalkRoute(@PathVariable Long walkId,
                                       @RequestBody @Valid CreateWalkRouteRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        walkFacade.createRoute(walkId, request);
        return Response.success(requestId, ResultVo.success());
    }

    /**
     * 산책 중 이벤트 추가
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{walkId}/events")
    public Response<ResultVo> createWalkEvents(@PathVariable Long walkId,
                                        @RequestBody @Valid WalkDto.CreateWalkEventRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        walkFacade.createEvent(walkId, request);
        return Response.success(requestId, ResultVo.success());

    }

    /**
     * 산책 상세 기록 저장
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{walkId}/record")
    public Response<ResultVo> createWalkRecord(@PathVariable Long walkId,
                                        @RequestBody @Valid CreateWalkRecordRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        walkFacade.createWalkDetail(walkId, request);
        return Response.success(requestId, ResultVo.success());
    }

    /**
     * 산책 중 파일 저장
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{walkId}/resources")
    public Response<ResultVo> createWalkResource(@PathVariable Long walkId,
                                                 @RequestBody CreateWalkResourceRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        walkFacade.createWalkResource(walkId, request);
        return Response.success(requestId, ResultVo.success());
    }

    /**
     * 산책 상세 정보 조회
     */
    @GetMapping("/{walkId}")
    public Response<WalkDetail> getWalkDetailById(@PathVariable Long walkId) {

        String requestId = requestIdGenerator.getRequestId();
        WalkDetail walkDetail = walkFacade.getWalkDetailById(walkId);
        return Response.success(requestId, walkDetail);
    }

    @PutMapping("/{walkId}")
    public Response<ResultVo> updateWalkDetail(@PathVariable Long walkId,
                                               @RequestBody UpdateWalkDetail request) {
        String requestId = requestIdGenerator.getRequestId();
        walkFacade.updateWalkDetail(walkId, request);
        return Response.success(requestId, ResultVo.success());
    }

    /**
     * 위치 기반 산책 정보 조회
     */
    @GetMapping("/location")
    public Response<?> getPetWalkElementByLocationInfo(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam Double distance,
            @RequestParam Long offset,
            @RequestParam Long limit) {
        String requestId = requestIdGenerator.getRequestId();

        List<WalkLocationInfo> petWalkElementList = walkFacade
                .getWalkElementListByLocationInfo(offset, limit, lat, lon, distance);

        return Response.success(requestId, petWalkElementList);
    }
}
