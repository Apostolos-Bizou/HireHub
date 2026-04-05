package com.hirehub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirehub.dto.request.LoginRequest;
import com.hirehub.dto.request.RegisterRequest;
import com.hirehub.domain.enums.UserRole;
import com.hirehub.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void cleanup() { userRepository.deleteAll(); }

    @Test
    @DisplayName("Register seafarer returns 201 + tokens")
    void registerSeafarer() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("juan@test.com"); req.setPassword("password123");
        req.setRole(UserRole.SEAFARER); req.setFullName("Juan Dela Cruz");

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value("SEAFARER"));
    }

    @Test
    @DisplayName("Duplicate email returns 400")
    void registerDuplicate() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("dupe@test.com"); req.setPassword("password123");
        req.setRole(UserRole.SEAFARER); req.setFullName("Dupe");

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isCreated());
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login with valid credentials returns tokens")
    void loginSuccess() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("login@test.com"); reg.setPassword("password123");
        reg.setRole(UserRole.SEAFARER); reg.setFullName("Login");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        LoginRequest login = new LoginRequest();
        login.setEmail("login@test.com"); login.setPassword("password123");
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Wrong password returns 401")
    void loginWrongPassword() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("wrong@test.com"); reg.setPassword("password123");
        reg.setRole(UserRole.SEAFARER); reg.setFullName("Wrong");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        LoginRequest login = new LoginRequest();
        login.setEmail("wrong@test.com"); login.setPassword("badpass");
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Invalid email format returns 400")
    void registerInvalidEmail() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("not-email"); req.setPassword("password123");
        req.setRole(UserRole.SEAFARER); req.setFullName("Bad");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Shipowner registration with company")
    void registerShipowner() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("nikos@aegean.gr"); req.setPassword("password123");
        req.setRole(UserRole.SHIPOWNER); req.setFullName("Nikos K.");
        req.setCompanyName("Aegean Maritime");
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.user.role").value("SHIPOWNER"));
    }
}
