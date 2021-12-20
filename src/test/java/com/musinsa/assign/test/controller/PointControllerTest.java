package com.musinsa.assign.test.controller;

import com.musinsa.assign.api.controller.PointController;
import com.musinsa.assign.api.controller.dto.PointDto;
import com.musinsa.assign.repository.entity.PointBalanceEntity;
import com.musinsa.assign.repository.entity.PointEntity;
import com.musinsa.assign.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PointControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private PointController pointController;

    @MockBean
    private PointService pointService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pointController)
                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("포인트 적립")
    @Test
    void savePoint() throws Exception {
        given(pointService.savePoint(any(), any(), any())).willReturn(PointDto.SuccessResponse.builder().success(true).build());

        mockMvc.perform(post("/api/point")
                .param("userId", "100")
                .param("point", "10000")
                .param("transactionNo", "10001")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("포인트 사용")
    @Test
    void usePoint() throws Exception {
        given(pointService.usePoint(any(), any(), any())).willReturn(PointDto.SuccessResponse.builder().success(true).build());

        mockMvc.perform(patch("/api/point")
                .param("userId", "200")
                .param("point", "5000")
                .param("transactionNo", "20001")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("포인트 사용취소")
    @Test
    void useCancelPoint() throws Exception {
        given(pointService.cancelPoint(any(), any())).willReturn(PointDto.SuccessResponse.builder().success(true).build());

        mockMvc.perform(delete("/api/point")
                .param("userId", "300")
                .param("transactionNo", "30001")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("포인트 조회")
    @Test
    void viewPointBalance() throws Exception {
        given(pointService.getPointBalance(any())).willReturn(PointBalanceEntity.builder().build());

        mockMvc.perform(get("/api/point-balance")
                .param("userId", "400")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("포인트 적립/사용 이력 조회")
    @Test
    void viewPointHistory() throws Exception {
        //given(pointService.getUserPointHistory(any(), any())).willReturn(this.pointHistoryResponse());

        mockMvc.perform(get("/api/point")
                .param("userId", "400")
                .param("pageNo", "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    private Page<PointEntity> pointHistoryResponse() {
        return new PageImpl<>(Arrays.asList(PointEntity.builder()
                .userId(400L)
                .point(10000L)
                .remainPoint(10000L)
                .transactionNo(10000L)
                .id(100L)
                .type(1)
                .useYn(1)
                .createdDt(LocalDateTime.now())
                .build()));
    }
}
