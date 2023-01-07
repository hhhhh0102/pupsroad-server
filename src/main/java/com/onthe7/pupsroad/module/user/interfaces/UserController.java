package com.onthe7.pupsroad.module.user.interfaces;

import com.onthe7.pupsroad.common.security.service.SecurityUserFacade;
import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.Response;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.gallery.application.GalleryFacade;
import com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryResourceElement;
import com.onthe7.pupsroad.module.pet.appilcation.PetFacade;
import com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;
import com.onthe7.pupsroad.module.user.domain.dto.AuthDto;
import com.onthe7.pupsroad.module.user.domain.dto.UserDto;
import com.onthe7.pupsroad.module.user.domain.service.UserCommandService;
import com.onthe7.pupsroad.module.user.domain.vo.UserVo.UserInfoVo;
import com.onthe7.pupsroad.module.user.interfaces.UserDto.DeleteUserGalleryResourceRequest;
import com.onthe7.pupsroad.module.walk.application.WalkFacade;
import com.onthe7.pupsroad.module.walk.domain.dto.response.WalkQueryResponse.PetWalkElement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationRequestDto;
import static com.onthe7.pupsroad.common.domain.dto.PaginationDto.PaginationResponseDto;
import static com.onthe7.pupsroad.module.gallery.domain.dto.response.GalleryQueryResponse.GalleryElement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final RequestIdGenerator requestIdGenerator;
    private final SecurityUserFacade securityUserFacade;
    private final GalleryFacade galleryFacade;
    private final WalkFacade walkFacade;
    private final PetFacade petFacade;
    private final UserCommandService userCommandService;

    /**
     * 유저정보 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public Response<UserInfoVo> showUserInfo() {
        String requestId = requestIdGenerator.getRequestId();
        AuthDto.UserCredentialDto userCredential = securityUserFacade.getLoginUserCredential();
        UserInfoVo userInfoVo = userCommandService.showUser(userCredential.getClientId());

        return Response.success(requestId, userInfoVo);
    }

    /**
     * 유저정보 수정
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("")
    public Response<ResultVo> editUserInfo(@RequestBody UserDto.UserInfoDto userInfoDto) {
        String requestId = requestIdGenerator.getRequestId();
        AuthDto.UserCredentialDto userCredential = securityUserFacade.getLoginUserCredential();
        ResultVo resultVo = userCommandService.editUser(userInfoDto, userCredential.getClientId());

        return Response.success(requestId, resultVo);
    }

    /**
     * 회원탈퇴
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("")
    public Response<ResultVo> deleteUser() {
        String requestId = requestIdGenerator.getRequestId();
        AuthDto.UserCredentialDto userCredential = securityUserFacade.getLoginUserCredential();
        ResultVo resultVo = userCommandService.deleteUser(userCredential.getClientId());

        return Response.success(requestId, resultVo);
    }

    /**
     * 유저 - 반려견 정보 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pets")
    public Response<List<PetDetail>> getUserPetDetailList() {
        String requestId = requestIdGenerator.getRequestId();
        List<PetDetail> petDetailList = petFacade.getUserPetDetailList();
        return Response.success(requestId, petDetailList);
    }

    /**
     * 유저 - 반려견 - 산책 정보 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/pets/{petId}/walks")
    public Response<?> getUserWalkHistory(@PathVariable Long petId,
                                          @RequestParam Integer limit,
                                          @RequestParam(required = false) String cursor,
                                          @RequestParam Integer year,
                                          @RequestParam Integer month) {
        String requestId = requestIdGenerator.getRequestId();
        PaginationRequestDto paginationRequest = PaginationRequestDto.from(limit, cursor);
        List<PetWalkElement> petWalkElementList = walkFacade.getPetWalkHistory(paginationRequest, petId, year, month);
        PaginationResponseDto response = PaginationResponseDto
                .from(petWalkElementList, paginationRequest.getLimit());
        return Response.success(requestId, response);
    }

    /**
     * 유저 갤러리 목록 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/galleries")
    public Response<?> getUserGalleryList(
            @RequestParam Integer limit,
            @RequestParam(required = false) String cursor) {
        String requestId = requestIdGenerator.getRequestId();
        PaginationRequestDto paginationRequest = PaginationRequestDto.from(limit, cursor);
        List<GalleryElement> galleryElementList = galleryFacade.getUserGalleryList(paginationRequest);
        PaginationResponseDto response = PaginationResponseDto
                .from(galleryElementList, paginationRequest.getLimit());
        return Response.success(requestId, response);
    }


    /**
     * 유저 기본 갤러리 - 리소스 파일 조회
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/galleries/{galleryId}/resources")
    public Response<?> getUserGalleryResources(@RequestParam Integer limit,
                                               @PathVariable Long galleryId,
                                               @RequestParam(required = false) String cursor) {
        String requestId = requestIdGenerator.getRequestId();
        PaginationRequestDto paginationRequest = PaginationRequestDto.from(limit, cursor);
        List<GalleryResourceElement> resourceElementList = galleryFacade
                .getUserGalleryResourceElementList(paginationRequest, galleryId);
        PaginationResponseDto response = PaginationResponseDto
                .from(resourceElementList, paginationRequest.getLimit());
        return Response.success(requestId, response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/galleries/{galleryId}/resources")
    public Response<ResultVo> deleteGalleryResource(@PathVariable Long galleryId,
                                             @RequestBody DeleteUserGalleryResourceRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        if (!request.getResourceIds().isEmpty()) {
            galleryFacade.deleteGalleryResource(galleryId, request.getResourceIds());
        }
        return Response.success(requestId, ResultVo.success());
    }

}
