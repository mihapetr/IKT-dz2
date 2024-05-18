package com.infobip.pmf.course.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    }

    //@Test
    void test1() throws JSONException{
        var response = restClient.get()
                .uri("/libraries?groupId=org.springframework")
                .retrieve()
                .toEntity(String.class);

        // then
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONAssert.assertEquals("""
                {
                  "results": [
                    {
                      "id": 1,
                      "groupId": "org.springframework",
                      "artifactId": "spring-core",
                      "versions": [1, 2],
                      "name": "Spring Core",
                      "description": "Spring Core Framework"
                    }
                  ],
                  "page": 0,
                  "size": 20,
                  "totalPages": 1,
                  "totalResults": 1
                }
                """, response.getBody(), true);
    }

    @Test
    void universal() {

        String url1 = "/libraries?groupId=org.springframework";
        String url2 = "/libraries";
        String url3 = "/libraries?artifactId=art1";
        String url4 = "/libraries?groupId=org.springframework&artifactId=art1&page=1&size=2";

        var r1 = restClient.get()
                .uri(url1)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        var r2 = restClient.get()
                .uri(url2)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        try {
            pp(url1, r1);
            pp(url2, r2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void pp(String url, String json) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        System.out.println("===========================================================");
        System.out.println(url);
        System.out.println("-----------------------------------------------------------");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
        System.out.println("===========================================================");

    }
}
