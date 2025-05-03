package com.rrtyui.filestorage.controller;

import com.rrtyui.filestorage.dto.MyUserRequestDto;
import com.rrtyui.filestorage.entity.MyUser;
import com.rrtyui.filestorage.mapper.MyUserMapper;
import com.rrtyui.filestorage.minio.service.impl.MinioFileStorageService;
import com.rrtyui.filestorage.repository.UserRepository;
import com.rrtyui.filestorage.service.RegisterService;
import com.rrtyui.filestorage.util.Mapper;
import com.rrtyui.filestorage.util.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserMapper userMapper;

    @Autowired
    private RegisterService registerService;

    @MockitoBean
    private MinioFileStorageService minioFileStorageService;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
    @Container
    static final GenericContainer<?> redis = new GenericContainer<>("redis:6.2.6")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgres::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgres::getPassword);
        dynamicPropertyRegistry.add("spring.jpa.generate-ddl", () -> true);
        dynamicPropertyRegistry.add("spring.redis.host", redis::getHost);
        dynamicPropertyRegistry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @BeforeEach
    void setUp() {
        // 👇 Настраиваем мок, чтобы метод ничего не делал и не падал
        doNothing().when(minioFileStorageService).createRootFolderByUserId(anyLong());
    }

    @Test
    @Transactional
    void registration_ShouldReturnIs2xxCreated_WhenCredentialsGood() throws Exception {
        UserResponse userResponse = new UserResponse("test");
        MyUserRequestDto user = new MyUserRequestDto("test", "test");
        String userJson = Mapper.toJson(user);
        String expected = Mapper.toJson(userResponse);

        MvcResult actual = mvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated()) // Status 201
                .andReturn();

        assertThat(actual.getResponse().getContentAsString()).isEqualTo(expected);
    }

    @Test
    @Transactional
    void registration_ShouldReturnIs4xxBadRequest_WhenCredentialsNotValid() throws Exception {
        MyUserRequestDto user = new MyUserRequestDto("t", "test");
        String userJson = Mapper.toJson(user);

        mvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest()); // Status 400
    }

    @Test
    @Transactional
    void registration_ShouldReturnIs4xxConflict_WhenUsernameAlreadyTaken() throws Exception {
        MyUserRequestDto dto = new MyUserRequestDto("test", "test");
        String userJson = Mapper.toJson(dto);
        userRepository.saveAndFlush(userMapper.toMyUser(dto));

        mvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict()); // Status 409
    }

    @Test
    @Transactional
    void auth_ShouldReturnIs2xxOk_WhenCredentialsGood() throws Exception {
        MyUserRequestDto dto = new MyUserRequestDto("test", "test");
        String userJson = Mapper.toJson(dto);
        registerService.register(dto);

        mvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        Optional<MyUser> shouldBeAdded = userRepository.findByUsername("test");
        assertTrue(shouldBeAdded.isPresent());
    }

    @Test
    @Transactional
    void auth_ShouldReturnIs4xxsUnauthorized_WhenUserDoesntExist() throws Exception {
        MyUserRequestDto savedUser = new MyUserRequestDto("test", "test");
        MyUserRequestDto notExistedUser = new MyUserRequestDto("notexist", "notexist");
        String wrongUser = Mapper.toJson(notExistedUser);
        registerService.register(savedUser);

        mvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongUser))
                .andExpect(status().isUnauthorized());
    }
}