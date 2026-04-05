package com.hirehub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirehub.dto.request.CreateShortlistRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ShortlistControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper om;
    @Autowired private UserRepository userRepo;
    private String ownerToken, seafarerToken;

    private String registerAndGetToken(String email, UserRole role, String name, String company) throws Exception {
        RegisterRequest r = new RegisterRequest();
        r.setEmail(email); r.setPassword("password123"); r.setRole(role);
        r.setFullName(name); r.setCompanyName(company);
        MvcResult res = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(r))).andReturn();
        return om.readTree(res.getResponse().getContentAsString()).get("accessToken").asText();
    }

    @BeforeEach void setup() throws Exception {
        userRepo.deleteAll();
        ownerToken = registerAndGetToken("owner@test.com", UserRole.SHIPOWNER, "Owner", "Test Ship");
        seafarerToken = registerAndGetToken("sailor@test.com", UserRole.SEAFARER, "Sailor", null);
    }

    @Test @DisplayName("Shipowner creates shortlist - 201")
    void createShortlist() throws Exception {
        CreateShortlistRequest req = new CreateShortlistRequest();
        req.setTitle("3rd Officer — Tanker"); req.setVesselContext("MT Pacific Voyager");
        mockMvc.perform(post("/shortlists").header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(req)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("3rd Officer — Tanker"));
    }

    @Test @DisplayName("Seafarer cannot create shortlist - 403")
    void seafarerForbidden() throws Exception {
        CreateShortlistRequest req = new CreateShortlistRequest();
        req.setTitle("Should Fail");
        mockMvc.perform(post("/shortlists").header("Authorization", "Bearer " + seafarerToken)
                .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test @DisplayName("GET /shortlists returns list")
    void getShortlists() throws Exception {
        CreateShortlistRequest req = new CreateShortlistRequest();
        req.setTitle("Test"); 
        mockMvc.perform(post("/shortlists").header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(req)));
        mockMvc.perform(get("/shortlists").header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content").isArray());
    }
}
