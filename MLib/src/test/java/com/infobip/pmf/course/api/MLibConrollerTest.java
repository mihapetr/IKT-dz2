package com.infobip.pmf.course.api;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/init-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MLibConrollerTest {
    @LocalServerPort
    private int port;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
        /*try {
            // Execute the init-test-data.sql script using JdbcTemplate
            jdbcTemplate.execute("classpath:init-test-data.sql");
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
            // Handle exceptions
        }*/
    }

    @Test
    void test1() throws JSONException{
        var response = restClient.get()
                .uri("/libraries?groupId=org.springframework")
                .retrieve()
                .toEntity(String.class);

        // then
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                [
                    {
                      "id": 1,
                      "groupId": "org.springframework",
                      "artifactId": "spring-core",
                      "versions": [1, 2],
                      "name": "Spring Core",
                      "description": "Spring Core Framework"
                    }
                ]""", response.getBody(), true);
    }

    /* ovaj test ne radi jer treba testirati Exception
    @Test
    void shouldBe400() {
        var response1 = restClient.get()
                .uri("/libraries?page=-1")
                .retrieve()
                .toEntity(String.class);

        var response2 = restClient.get()
                .uri("/libraries?size=0")
                .retrieve()
                .toEntity(String.class);

        then(response1.getStatusCode()).isEqualTo(400);
        then(response2.getStatusCode()).isEqualTo(400);
    }*/

    /*@Test
    void shouldGetAllUsers() throws JSONException {
        // when
        var response = restClient.get()
                .uri("libraries/users")
                .retrieve()
                .toEntity(String.class);

        // then
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                [
                    {
                        "id": 1,
                        "username": "Otac",
                        "passHash": "la9psd71atbpgeg7fvvx"
                    },
                    {
                        "id": 2,
                        "username": "Sin",
                        "passHash": "ox9w79g2jwctzww2hcyb"
                    },
                    {
                        "id": 3,
                        "username": "Duh",
                        "passHash": "othyqhps18srg7fdj0p9"
                    }
                ]""", response.getBody(), true);
    }*/


}
