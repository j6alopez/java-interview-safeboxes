package com.interview.skeletons.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.interview.skeletons.auth.config.SecurityConfig;
import com.interview.skeletons.config.ApiConfig;
import com.interview.skeletons.dtos.items.ItemDTO;
import com.interview.skeletons.dtos.safes.CreateSafeBoxDTO;
import com.interview.skeletons.dtos.safes.SafeBoxDTO;
import com.interview.skeletons.dtos.safes.UpdateSafeBoxDTO;
import com.interview.skeletons.services.SafeBoxService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.when;

@WebMvcTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@WithMockUser(username = "testUser", password = "testWordPass1!", roles = "USER")
public class SafeBoxControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    SafeBoxService safeBoxService;
    private final JsonMapper mapper = JsonMapper.builder().build();
    private Random randomGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.randomGenerator = new Random();
    }

    @Test
    void create_safe_box_ok() throws Exception {
        final String ID_FIELD = "$.id";
        final String COMPLIANT_PASSWORD = "12345678910Abcd!";

        final CreateSafeBoxDTO createRequest = new CreateSafeBoxDTO(
            RandomStringUtils.randomAlphabetic(5),
            COMPLIANT_PASSWORD
        );

        final SafeBoxDTO payloadResponse = new SafeBoxDTO(
            randomGenerator.nextLong(1, 100),
            createRequest.name(),
            createRequest.password(),
            null
        );

        when(safeBoxService.createSafeBox(createRequest)).thenReturn(payloadResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(ApiConfig.BASE_PATH + "/safeboxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest)))
            .andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath(ID_FIELD).exists(),
                MockMvcResultMatchers.jsonPath(ID_FIELD).value(payloadResponse.id())
            );
    }

    @Test
    void get_safe_box_ok() throws Exception {
        final String ITEMS_FIELD = "$.items";
        final String ID_FIELD = "$..id";
        final String DESCRIPTION_FIELD = "$..description";

        final SafeBoxDTO payloadResponse = new SafeBoxDTO(
            randomGenerator.nextLong(1, 100),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            generateItemsDTO()
        );

        final Long safeBoxId = payloadResponse.id();

        when(safeBoxService.getSafeBox(safeBoxId)).thenReturn(payloadResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(ApiConfig.BASE_PATH + "/safeboxes/{id}", safeBoxId))
            .andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath(ITEMS_FIELD).exists(),
                MockMvcResultMatchers.jsonPath(ID_FIELD).exists(),
                MockMvcResultMatchers.jsonPath(DESCRIPTION_FIELD).exists()
            );
    }

    @Test
    void update_safe_box_ok() throws Exception {

        final List<ItemDTO> itemsDto = generateItemsDTO();

        final UpdateSafeBoxDTO updateSafeBoxDTO = new UpdateSafeBoxDTO(itemsDto);
        final Long safeBoxId = randomGenerator.nextLong(1, 100);

        when(safeBoxService.updateSafeBox(safeBoxId, updateSafeBoxDTO)).thenReturn(updateSafeBoxDTO.items());

        mockMvc.perform(MockMvcRequestBuilders.put(ApiConfig.BASE_PATH + "/safeboxes/{id}", safeBoxId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updateSafeBoxDTO))
        ).andExpectAll(
            MockMvcResultMatchers.status().isNoContent(),
            MockMvcResultMatchers.content().string(StringUtils.EMPTY)
        );
    }

    @Test
    @WithAnonymousUser
    void authorization_not_valid_ko() throws Exception {
        final Long safeBoxId = randomGenerator.nextLong(1, 100);
        final List<ItemDTO> itemsDto = generateItemsDTO();
        final UpdateSafeBoxDTO updateSafeBoxDTO = new UpdateSafeBoxDTO(itemsDto);

        mockMvc.perform(MockMvcRequestBuilders.put(ApiConfig.BASE_PATH + "/safeboxes/{id}", safeBoxId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateSafeBoxDTO))
            )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }


    private List<ItemDTO> generateItemsDTO() {

        return List.of(
            new ItemDTO(RandomStringUtils.randomAlphabetic(5)),
            new ItemDTO(RandomStringUtils.randomAlphabetic(5))
        );

    }

}
