package com.client.ws.projectplus.service;

import com.client.ws.projectplus.dto.UserDetailsDto;
import com.client.ws.projectplus.exception.BadRequestException;
import com.client.ws.projectplus.exception.NotFoundException;
import com.client.ws.projectplus.integration.MailIntegration;
import com.client.ws.projectplus.model.jpa.UserCredentials;
import com.client.ws.projectplus.model.jpa.UserType;
import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import com.client.ws.projectplus.repository.jpa.UserDetailsRepository;
import com.client.ws.projectplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.projectplus.service.impl.UserDetailsServiceImpl;
import com.client.ws.projectplus.utils.PasswordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
 * Nomeclatura de teste: given [contexto] when [ação] then [resultado esperado]
 * Exemplo: given_metodo_when_cenario_then_retornoEsperado
 * */

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    private static final String USERNAME_ALUNO = "testuser@testuser.com";
    private static final String PASSWORD_ALUNO = "senha123";

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Mock
    private MailIntegration mailIntegration;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void given_loadUserByUsernameAndPass_when_userExistsAndPasswordMatches_then_returnUserCredentials() {
        UserCredentials userCredentials = getUserCredentials();
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        UserCredentials result = userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO);

        assertEquals(userCredentials, result);
        verify(userDetailsRepository, times(1)).findByUsername(any());
    }

    @Test
    void given_loadUserByUsernameAndPass_when_userCredentialsIsEmpty_then_throwNotFoundException() {
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());

        assertEquals("Usuário não encontrado",
                assertThrows(NotFoundException.class,
                        () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO)
                ).getLocalizedMessage());

        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_loadUserByUsernameAndPass_when_passwordIsDifferent_then_throwBadRequestException() {
        UserCredentials userCredentials = getUserCredentials();
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        assertEquals("Usuário ou senha inválido",
                assertThrows(BadRequestException.class,
                        () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, "pass")
                ).getLocalizedMessage());
        verify(userDetailsRepository, times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_sendRecoveryCode_when_userExistsAndRecoveryCodeDoesNotExist_then_saveUserAndSendEmail() {

        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(getUserCredentials()));

        userDetailsService.sendRecoveryCode(USERNAME_ALUNO);

        verify(userRecoveryCodeRepository, times(1)).save(any());
        verify(mailIntegration, times(1)).send(any(), any(), any());
    }

    @Test
    void given_sendRecoveryCode_when_userExistsAndRecoveryCodeExist_then_updateUserAndSendEmail() {

        UserRecoveryCode userRecoveryCode = new UserRecoveryCode(UUID.randomUUID().toString(), USERNAME_ALUNO, "4065", LocalDateTime.now());
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));

        userDetailsService.sendRecoveryCode(USERNAME_ALUNO);

        verify(userRecoveryCodeRepository, times(1)).save(any());
        verify(mailIntegration, times(1)).send(any(), any(), any());
    }

    @Test
    void given_sendRecoveryCode_when_userDoesNotExistAndRecoveryCodeDoesNotExist_then_throwNotFoundException(){
        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());
        try {
            userDetailsService.sendRecoveryCode(USERNAME_ALUNO);
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
            assertEquals("Usuário não encontrado", e.getMessage());
        }
        verify(userRecoveryCodeRepository, times(0)).save(any());
        verify(mailIntegration, times(0)).send(any(), any(), any());
    }

//    @Test
//    void given_recoveryCodeIsValid_when_userExistsAndCodeIsValid_then_returnTrue() {
//        String recoveryCode = "0011";
//
//        UserRecoveryCode userRecoveryCode = new UserRecoveryCode(UUID.randomUUID().toString(), USERNAME_ALUNO, recoveryCode, LocalDateTime.now().minusMinutes(1));
//        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));
//
//        Assertions.assertTrue(userDetailsService.recoveryCodeIsValid(recoveryCode, USERNAME_ALUNO));
//    }
//
//    @Test
//    void given_recoveryCodeIsValid_when_userExistsAndCodeDoesNotValid_then_returnFalse() {
//        String recoveryCode = "0011";
//
//        UserRecoveryCode userRecoveryCode = new UserRecoveryCode(UUID.randomUUID().toString(), USERNAME_ALUNO, recoveryCode, LocalDateTime.now().minusMinutes(1));
//        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));
//
//        Assertions.assertTrue(userDetailsService.recoveryCodeIsValid(recoveryCode, USERNAME_ALUNO));
//    }
//
//    @Test
//    void given_recoveryCodeIsValid_when_userDoesNotExist_then_throwNotFoundException(){
//        String recoveryCode = "0011";
//
//        when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> userDetailsService.recoveryCodeIsValid(recoveryCode, USERNAME_ALUNO));
//    }
//
//    @Test
//    void given_updatePasswordByRecoveryCode_when_recoveryCodeIsValid_then_updatePassworAndSendEmail() {
//        UserDetailsDto userDetailsDto = new UserDetailsDto("testuser@testuser.com", "0011", "newPassword");
//
//        UserCredentials userCredentials = new UserCredentials(1L, userDetailsDto.getEmail(), PasswordUtils.encode("password"), new UserType());
//        when(userDetailsRepository.findByUsername(userDetailsDto.getEmail())).thenReturn(Optional.of(userCredentials));
//        when(userDetailsService.recoveryCodeIsValid(userDetailsDto.getPassword(), userDetailsDto.getEmail())).thenReturn(true);
//
//        userDetailsService.updatePasswordByRecoveryCode(userDetailsDto);
//
//        verify(userDetailsRepository, times(1)).save(any());
//        verify(mailIntegration, times(1)).send(eq(userDetailsDto.getEmail()), eq("Alteração de senha"), contains("senha foi alterada com sucesso"));
//    }

    private UserCredentials getUserCredentials() {
        UserType userType = new UserType(1L, "aluno", "aluno plataforma");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new UserCredentials(1L, USERNAME_ALUNO, encoder.encode(PASSWORD_ALUNO), userType);
    }
}
