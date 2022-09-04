package com.musinsa.assign.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.assign.api.controller.InventoryController;
import com.musinsa.assign.api.controller.dto.InventoryDto;
import com.musinsa.assign.repository.entity.InventoryEntity;
import com.musinsa.assign.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InventoryControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private InventoryController inventoryController;

    @MockBean
    private InventoryService inventoryService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController)
                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("재고 조회 - ProductName")
    @Test
    void viewList() throws Exception {
        given(inventoryService.getInventories("prd-a")).willReturn(new ArrayList<>());

        mockMvc.perform(get("/api/inventories/products/prd-a").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("재고 조회 - ProductName, OptionName")
    @Test
    void viewOne() throws Exception {
        given(inventoryService.getInventory("prd-a", "opt-aa")).willReturn(InventoryEntity.builder().build());

        mockMvc.perform(get("/api/inventories/products/prd-a/options/opt-aa").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("재고 증가")
    @Test
    void increase() throws Exception {
        given(inventoryService.increase(any())).willReturn(true);

        InventoryDto.SaveRequest request = InventoryDto.SaveRequest.builder()
                .productName("prd-a")
                .optionName("opt-aa")
                .count(2L)
                .build();

        String bodyParam = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(patch("/api/inventories/increase")
                        .content(bodyParam)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("재고 차감")
    @Test
    void subtract() throws Exception {
        given(inventoryService.subtract(any())).willReturn(true);

        InventoryDto.SaveRequest request = InventoryDto.SaveRequest.builder()
                .productName("prd-a")
                .optionName("opt-aa")
                .count(2L)
                .build();

        String bodyParam = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(patch("/api/inventories/subtract")
                        .content(bodyParam)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
    }
}
