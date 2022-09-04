package com.musinsa.assign.api.controller.dto;

import com.musinsa.assign.repository.entity.InventoryEntity;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryDto {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InventoryListResponse {
        private List<Inventory> inventories;

        public static InventoryListResponse of(List<InventoryEntity> inventoryEntities) {
            if (inventoryEntities.isEmpty())
                return InventoryListResponse.builder().inventories(Collections.emptyList()).build();

            return InventoryListResponse.builder()
                    .inventories(inventoryEntities.stream()
                            .map(inventory -> Inventory.builder()
                                    .productName(inventory.getProductName())
                                    .optionName(inventory.getOptionName())
                                    .count(inventory.getCount())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class InventoryResponse {
        private Inventory inventory;

        public static InventoryResponse of(InventoryEntity e) {
            if (e == null) return null;

            return InventoryResponse.builder()
                    .inventory(Inventory.builder()
                            .productName(e.getProductName())
                            .optionName(e.getOptionName())
                            .count(e.getCount())
                            .build())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Inventory {
        private String productName;
        private String optionName;
        private Long count;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveRequest {
        @NotEmpty private String productName;
        @NotEmpty private String optionName;
        @Min(1) private Long count;
    }

    @Getter
    public static class SuccessResponse {
        private Boolean success;

        public SuccessResponse(Boolean success) {
            this.success = success;
        }
    }
}
