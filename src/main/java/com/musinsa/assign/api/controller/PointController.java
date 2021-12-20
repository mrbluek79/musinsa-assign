package com.musinsa.assign.api.controller;

import com.musinsa.assign.api.controller.dto.PointDto;
import com.musinsa.assign.repository.entity.PointEntity;
import com.musinsa.assign.service.PointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Api(tags="포인트")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PointController {
    private final PointService service;

    @ApiOperation("포인트 합계 조회")
    @GetMapping("/point-balance")
    public ResponseEntity<PointDto.PointBalanceResponse> balance(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(PointDto.PointBalanceResponse.of(service.getPointBalance(userId)));
    }

    @ApiOperation("포인트 적립/사용 내역 조회")
    @GetMapping("/point")
    public ResponseEntity<Page<PointEntity>> pointHistory(@RequestParam(value = "userId") Long userId,
                                                          @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        return ResponseEntity.ok(service.getUserPointHistory(userId, pageNo));
    }

    @ApiOperation("포인트 적립")
    @PostMapping("/point")
    public ResponseEntity<PointDto.SuccessResponse> savePoint(@RequestParam(value = "userId") Long userId,
                                                                   @Min(0) @RequestParam(value = "point") Long point,
                                                                   @Min(1) @RequestParam(value = "transactionNo") Long transactionNo) throws Exception {
        return ResponseEntity.ok(service.savePoint(userId, point, transactionNo));
    }

    @ApiOperation("포인트 사용")
    @PatchMapping("/point")
    public ResponseEntity<PointDto.SuccessResponse> usePoint(@RequestParam(value = "userId") Long userId,
                                                                  @Min(0) @RequestParam(value = "point") Long point,
                                                                  @Min(1) @RequestParam(value = "transactionNo") Long transactionNo) throws Exception {
        return ResponseEntity.ok(service.usePoint(userId, point, transactionNo));
    }

    @ApiOperation("포인트 취소")
    @DeleteMapping("/point")
    public ResponseEntity<PointDto.SuccessResponse> cancelPoint(@RequestParam(value = "userId") Long userId,
                                                                     @Min(1) @RequestParam(value = "transactionNo") Long transactionNo) throws Exception {
        return ResponseEntity.ok(service.cancelPoint(transactionNo, userId));
    }
}
