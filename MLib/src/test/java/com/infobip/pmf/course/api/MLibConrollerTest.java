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
import org.springframework.http.MediaType;
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
    void allLibs() throws Exception {
        request(
                "get",
                "/libraries",
                null
        );
    }

    @Test
    void allLibsFilter() throws Exception {
        request(
                "get",
                "libraries?groupId=org.springframework",
                null
        );
    }

    // popravi tako da indeks strancie bude stvaran, a ne traženi
    // samo dva filtera dopuštena uz page i size? -> popravi?
    @Test
    void allLibsFilterBad() throws Exception {
        request(
                "get",
                "libraries?group=a&page=1",
                null
        );
    }

    @Test
    void allLibsFilterBadPage() throws Exception {
        request(
                "get",
                "libraries?page=-1",
                null
        );
    }

    // more filter and page tests todo here

    @Test
    void libById() throws Exception {
        request(
                "get",
                "/libraries/1",
                null
        );
    }

    @Test
    void updateLib() throws Exception {
        request(
                "patch",
                "/libraries/1",
                """
                    {
                        "groupId": "nova.grupa",
                        "artifactId": "a-thing",
                        "name": "the-thing",
                        "description": "hehe ;)"
                    }"""
        );
    }

    @Test
    void deleteLib() throws Exception {
        request(
                "delete",
                "/libraries/1",
                null
        );
        System.out.println("AFTER DELETION : ");
        request(
                "get",
                "/libraries",
                null
        );
    }

    @Test
    void getLibVersions() throws Exception {
        request(
                "get",
                "/libraries/1/versions",
                null
        );
    }

    @Test
    void getLibVersion() throws Exception {
        request(
                "get",
                "/libraries/1/versions/1",
                null
        );
    }

    @Test
    void registerVersion() throws Exception {
        request("post",
                "/libraries/1/versions",
                """
                        {
                          "semanticVersion": "5.3.11",
                          "description": "Spring Core Framework 5.3.10",
                          "deprecated": false
                        }
                        """
        );
    }

    // test exceptions

    @Test
    void updateVersion() throws Exception {
        request("patch",
                "/libraries/1/versions/1",
                """
                        {
                          "description": "brbrbrbbrbr",
                          "deprecated": true
                        }
                        """
        );
    }


    void pp(String method, String url, String status, String json) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        if (json != null) {
            JsonNode node = mapper.readTree(json);

            System.out.println("===========================================================");
            System.out.println(method + " | " + url + " : status = " + status);
            System.out.println("-----------------------------------------------------------");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
            System.out.println("===========================================================");
        }
        else {
            System.out.println("===========================================================");
            System.out.println(method + " | " + url + " | status = " + status);
            System.out.println("-----------------------------------------------------------");
            System.out.println("Nothing Here");
            System.out.println("===========================================================");
        }
    }

    void request(String method, String url, String body) throws Exception{

        switch (method) {
            case "get":

                var r1 = restClient.get()
                        .uri(url)
                        .header("Authorization", "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class);
                pp(method, url, r1.getStatusCode().toString(), r1.getBody());

                break;

            case "delete":

                var r2 = restClient.delete()
                        .uri(url)
                        .header("Authorization", "App la9psd71atbpgeg7fvvx")
                        .retrieve()
                        .toEntity(String.class);
                pp(method, url, r2.getStatusCode().toString(), r2.getBody());

                break;

            case "post":

                var r3 = restClient.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "App la9psd71atbpgeg7fvvx")
                        .body(body)
                        .retrieve()
                        .toEntity(String.class);
                pp(method, url, r3.getStatusCode().toString(), r3.getBody());

                break;

            case "patch":

                var r4 = restClient.patch()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "App la9psd71atbpgeg7fvvx")
                        .body(body)
                        .retrieve()
                        .toEntity(String.class);
                pp(method, url, r4.getStatusCode().toString(), r4.getBody());

                break;
        }
    }
}
