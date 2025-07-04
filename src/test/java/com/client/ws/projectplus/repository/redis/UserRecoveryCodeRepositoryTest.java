package com.client.ws.projectplus.repository.redis;

import com.client.ws.projectplus.model.redis.UserRecoveryCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataRedis
@AutoConfigureTestDatabase
@WebMvcTest(UserRecoveryCodeRepository.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRecoveryCodeRepositoryTest {

    @Autowired
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @BeforeAll
    void loadSubscriptions() {
        List<UserRecoveryCode> userRecoveryCodeList = new ArrayList<>();

        UserRecoveryCode userRecoveryCode1 = new UserRecoveryCode();
        userRecoveryCode1.setEmail("usuario1@usuario.com");
        userRecoveryCode1.setCode("1234");
        userRecoveryCodeList.add(userRecoveryCode1);

        UserRecoveryCode userRecoveryCode2 = new UserRecoveryCode();
        userRecoveryCode2.setEmail("usuario2@usuario.com");
        userRecoveryCode2.setCode("2345");
        userRecoveryCodeList.add(userRecoveryCode2);

        UserRecoveryCode userRecoveryCode3 = new UserRecoveryCode();
        userRecoveryCode3.setEmail("usuario3@usuario.com");
        userRecoveryCode3.setCode("3456");
        userRecoveryCodeList.add(userRecoveryCode3);

        userRecoveryCodeRepository.saveAll(userRecoveryCodeList);
    }

    @AfterAll
    void dropDataBase() {
        userRecoveryCodeRepository.deleteAll();
    }

    @Test
    void given_findByEmail_when_getByEmail_then_returnCorrectUserRecoveryCode() {
        assertEquals("1234", userRecoveryCodeRepository.findByEmail("usuario1@usuario.com").get().getCode());
        assertEquals("2345", userRecoveryCodeRepository.findByEmail("usuario2@usuario.com").get().getCode());
        assertEquals("3456", userRecoveryCodeRepository.findByEmail("usuario3@usuario.com").get().getCode());
    }
}