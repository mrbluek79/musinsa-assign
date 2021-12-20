package com.musinsa.assign.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.musinsa.assign.enums.PointTypeEnum;
import com.musinsa.assign.repository.entity.PointBalanceEntity;
import com.musinsa.assign.repository.entity.PointEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PointDto {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PointHistoryResponse {
        private Long point;
        private String type;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime expiredDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime createdDt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
        private LocalDateTime updatedDt;

        public static PointHistoryResponse of(PointEntity e) {
            return PointHistoryResponse.builder()
                    .point(e.getPoint())
                    .type(PointTypeEnum.of(e.getType()).getDescription())
                    .expiredDt(e.getExpiredDt())
                    .createdDt(e.getCreatedDt())
                    .updatedDt(e.getUpdatedDt())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PointHistoriesResponse {
        private List<PointHistoryResponse> list;

        public static PointHistoriesResponse of(List<PointEntity> entities) {
            if (entities == null) entities = Collections.emptyList();

            return PointHistoriesResponse.builder()
                    .list(entities.stream()
                            .map(PointHistoryResponse::of)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PointBalanceResponse {
        private Long point;

        public static PointBalanceResponse of(PointBalanceEntity e) {
            if (e == null) return null;

            return PointBalanceResponse.builder()
                    .point(e.getPoint())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SuccessResponse {
        private Boolean success;
    }
}
