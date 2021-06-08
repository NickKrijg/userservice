package com.kwetter.userservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuaWNrIiwiUm9sZXMiOlsiVVNFUiJdLCJleHAiOjE2MjU3NzE5NDEsImlhdCI6MTYyMzE3OTk0MSwianRpIjoiMTEifQ.Y6N7jN7MzBFnEhcC-qzirfiaK6-DGRHPMAxEna2KC5Y";

    @Test
    void extractUsername() {
        JwtUtil jwtUtil = new JwtUtil();

        String actual = jwtUtil.extractUsername(token);

        assertEquals("nick", actual);
    }

    @Test
    void extractId() {
        JwtUtil jwtUtil = new JwtUtil();

        Long actual = jwtUtil.extractId(token);

        assertEquals(11L, actual);
    }

    @Test
    void extractRoles() {
        JwtUtil jwtUtil = new JwtUtil();

        ArrayList<String> actual = jwtUtil.extractRoles(token);

        assertEquals("USER", actual.get(0));
    }



}
