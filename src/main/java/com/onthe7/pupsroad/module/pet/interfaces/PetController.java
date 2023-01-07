package com.onthe7.pupsroad.module.pet.interfaces;

import com.onthe7.pupsroad.common.util.RequestIdGenerator;
import com.onthe7.pupsroad.common.vo.Response;
import com.onthe7.pupsroad.common.vo.ResultVo;
import com.onthe7.pupsroad.module.pet.appilcation.PetFacade;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.CreatePetProfileRequest;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.CreatePetProfileResponse;
import com.onthe7.pupsroad.module.pet.interfaces.PetDto.UpdatePetProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.onthe7.pupsroad.module.pet.domain.dto.response.PetQueryResponse.PetDetail;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetFacade petFacade;
    private final RequestIdGenerator requestIdGenerator;


    /**
     * 반려견 등록
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public Response<CreatePetProfileResponse> createPetProfile(@RequestBody @Valid CreatePetProfileRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        Long petId = petFacade.createPetProfile(request);
        return Response.success(requestId, CreatePetProfileResponse.success(petId));
    }

    /**
     * 반려견 정보 수정
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{petId}")
    public Response<ResultVo> updatePetProfile(@PathVariable Long petId,
                                        @RequestBody @Valid UpdatePetProfileRequest request) {
        String requestId = requestIdGenerator.getRequestId();
        ResultVo resultVo = petFacade.updatePetProfile(petId, request);
        return Response.success(requestId, resultVo);
    }

    /**
     * 반려견 정보 삭제
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{petId}")
    public Response<ResultVo> deletePetProfile(@PathVariable Long petId) {
        String requestId = requestIdGenerator.getRequestId();
        ResultVo resultVo = petFacade.deletePetProfile(petId);
        return Response.success(requestId, resultVo);
    }

    /**
     * 반려견 정보 조회
     */
    @GetMapping("/{petId}")
    public Response<PetDetail> getPetDetail(@PathVariable Long petId) {
        String requestId = requestIdGenerator.getRequestId();
        PetDetail petDetail = petFacade.getPetDetail(petId);
        return Response.success(requestId, petDetail);
    }
}
