package com.client.ws.projectplus.controller;

import com.client.ws.projectplus.dto.LoginDto;
import com.client.ws.projectplus.dto.TokenDto;
import com.client.ws.projectplus.dto.UserDetailsDto;
import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import com.client.ws.projectplus.service.AuthenticationService;
import com.client.ws.projectplus.service.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class AuthenticationControllerTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    //given_metodo_when_???_then_return???

    @Test
    void given_auth_when_dtoIsOk_then_returnTokenDto() throws Exception {

        LoginDto dto = new LoginDto("usuario@usuario.com","pass");
        TokenDto tokenDto = new TokenDto(TOKEN, "Bearer");

        when(authenticationService.auth(dto)).thenReturn(tokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token",Matchers.is(TOKEN)))
                .andExpect(jsonPath("$.type", Matchers.is("Bearer")));
    }

    @Test
    void given_auth_when_dtoIsMissingValues_then_returnBadRequest() throws Exception {
        LoginDto dto = new LoginDto("","");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",Matchers.is("[password=atributo obrigatório, username=atributo obrigatório]")))
                .andExpect(jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)))
        ;
    }

    @Test
    void given_sendRecoveryCode_when_dtoIsOk_then_returnNoContent() throws Exception {
        UserRecoveryCode dto = new UserRecoveryCode();
        dto.setEmail("usuario@usuario.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void given_sendRecoveryCode_when_emailIsNotSentOrWrong_then_returnBadRequest() throws Exception {
        UserRecoveryCode dto = new UserRecoveryCode();
        dto.setEmail("usuario");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[email=inválido]")))
                .andExpect(jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_dtoIsOk_then_returnNoContent() throws Exception {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setEmail("usuario@usuario.com");
        dto.setPassword("pass");
        dto.setRecoveryCode("808921");

        mockMvc.perform(MockMvcRequestBuilders.patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void given_updatePasswordByRecoveryCode_when_emailIsWrongAndPassIsNull_then_returnBadRequest() throws Exception {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setEmail("usuario");
        dto.setPassword(" ");
        dto.setRecoveryCode("808921");

        mockMvc.perform(MockMvcRequestBuilders.patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[password=atributo inválido, email=inválido]")))
                .andExpect(jsonPath("$.httpStatus", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    @Test
    void given_recoveryCodeIsValid_then_returnOk() throws Exception {

        when(userDetailsService.recoveryCodeIsValid("1234", "usuario@usuario")).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/recovery-code/").param("recoveryCode","1234")
                        .param("email","usuario@usuario"))
                .andExpect(status().isOk());

        verify(userDetailsService, times(1)).recoveryCodeIsValid("1234","usuario@usuario");
    }
}