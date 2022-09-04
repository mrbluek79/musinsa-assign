package com.musinsa.assign.api.controller;

import com.musinsa.assign.api.controller.dto.InventoryDto;
import com.musinsa.assign.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "재고관리")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService service;

    @ApiOperation("재고 조회 by ProductName")
    @GetMapping("/products/{productName}")
    public ResponseEntity<InventoryDto.InventoryListResponse> viewList(@PathVariable(value = "productName") String productName) {
        return ResponseEntity.ok(InventoryDto.InventoryListResponse.of(service.getInventories(productName)));
    }

    @ApiOperation("재고 조회 by ProductName, OptionName")
    @GetMapping("/products/{productName}/options/{optionName}")
    public ResponseEntity<InventoryDto.InventoryResponse> viewOne(@PathVariable(value = "productName") String productName,
                                                                  @PathVariable(value = "optionName") String optionName) {
        InventoryDto.InventoryResponse response = InventoryDto.InventoryResponse.of(service.getInventory(productName, optionName));
        if (response == null) {
            response = InventoryDto.InventoryResponse.builder()
                    .inventory(InventoryDto.Inventory.builder()
                            .productName(productName)
                            .optionName(optionName)
                            .count(0L)
                            .build())
                    .build();
        }

        return ResponseEntity.ok(response);
    }

    @ApiOperation("재고 증가")
    @PatchMapping("/increase")
    public ResponseEntity<InventoryDto.SuccessResponse> increase(@Valid @RequestBody InventoryDto.SaveRequest request) {
        return ResponseEntity.ok(new InventoryDto.SuccessResponse(service.increase(request)));
    }

    @ApiOperation("재고 차감")
    @PatchMapping("/subtract")
    public ResponseEntity<InventoryDto.SuccessResponse> subtract(@Valid @RequestBody InventoryDto.SaveRequest request) throws Exception {
        return ResponseEntity.ok(new InventoryDto.SuccessResponse(service.subtract(request)));
    }
}
