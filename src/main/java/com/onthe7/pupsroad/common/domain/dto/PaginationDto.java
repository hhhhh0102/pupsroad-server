package com.onthe7.pupsroad.common.domain.dto;

import com.onthe7.pupsroad.common.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.onthe7.pupsroad.common.util.PaginationUtil.*;

public class PaginationDto {
    @Getter
    @NoArgsConstructor
    public static class PaginationRequestDto {

        private String cursor;
        private Integer limit;

        public static PaginationRequestDto from(Integer limit, String cursor) {
            PaginationRequestDto paginationDto = new PaginationRequestDto();
            paginationDto.limit = Objects.nonNull(limit) ?
                    limit > DEFAULT_LIMIT ? DEFAULT_LIMIT + 1 : limit + 1:
                    DEFAULT_LIMIT + 1;
            paginationDto.cursor = Objects.nonNull(cursor) ? decryptCursor(cursor) : DEFAULT_CURSOR;
            return paginationDto;
        }
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class PaginationResponseDto {
        private final PaginationInfo paginationInfo;
        private final List<? extends Cursor> elements;

        public static PaginationResponseDto from(List<? extends Cursor> elements, Integer limit) {

            return PaginationResponseDto.builder()
                    .paginationInfo(getPaginationInfo(elements, limit))
                    .elements(elements)
                    .build();
        }

        @Getter
        @Builder(access = AccessLevel.PRIVATE)
        public static class PaginationInfo {
            private final Integer size;
            private final boolean hasNext;
            private final String nextCursor;
        }

        public static PaginationInfo getPaginationInfo(List<? extends Cursor> elements, Integer limit) {
            boolean hasNext = hasNext(elements, limit);
            if (hasNext) elements.remove(elements.size() - 1);
            String nextCursor = PaginationUtil.getNextCursor(hasNext, elements);

            return PaginationInfo.builder()
                    .size(elements.size())
                    .hasNext(hasNext)
                    .nextCursor(Objects.nonNull(nextCursor) ? encryptCursor(nextCursor) : null)
                    .build();
        }
    }
}
