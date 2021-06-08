package com.kwetter.userservice.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        List<String> actual = jwtUtil.extractRoles(token);

        assertEquals("USER", actual.get(0));
    }



}
