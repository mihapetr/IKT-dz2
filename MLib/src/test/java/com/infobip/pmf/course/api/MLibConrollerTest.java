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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.BDDAssertions.catchThrowableOfType;
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

    @Test
    void allLibs() throws Exception {
        request(
                "get",
                "/libraries",
                null,
                HttpStatus.OK
        );
    }

    @Test
    void allLibsFilter() throws Exception {
        request(
                "get",
                "/libraries?groupId=org.springframework",
                null,
                HttpStatus.OK
        );
    }

    // popravi tako da indeks strancie bude stvaran, a ne traženi
    // samo dva filtera dopuštena uz page i size? -> popravi?
    @Test
    void allLibsFilterBad() throws Exception {
        request(
                "get",
                "/libraries?group=a&page=1",
                null,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void allLibsFilterBadPage() throws Exception{
        request(
                "get",
                "/libraries?page=-1",
                null,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void shouldGiveEmptyList() throws Exception{
            request(
                    "get",
                    "/libraries?groupId=org.springframework&artifactId=nepostojeciiii",
                    null,
                    HttpStatus.OK
            );
    }

    // more filter and page tests todo here (done?)

    @Test
    void illegalLibFormatGroup() throws Exception {
        request(
                "post",
                "/libraries",
                """
                    {
                        "artifactId": "a-thing",
                        "name": "the-thing",
                        "description": "hehe ;)"
                    }""",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void illegalLibFormatName() throws Exception {
        request(
                "post",
                "/libraries",
                """
                    {
                        "groupId": "nova.grupa",
                        "artifactId": "a-thing"
                    }""",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void illegalLibFormatArtifact() throws Exception {
        request(
                "post",
                "/libraries",
                """
                    {
                        "groupId": "nova.grupa",
                        "name": "the-thing",
                        "description": "hehe ;)"
                    }""",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void libIdComboViolation() throws Exception {
        request(
                "post",
                "/libraries",
                """
                    {
                        "groupId": "org.moja",
                        "artifactId": "art1",
                        "name": "",
                        "description": ""
                    }""",
                HttpStatus.CONFLICT
        );
    }

    @Test
    void libById() throws Exception {
        request(
                "get",
                "/libraries/1",
                null,
                HttpStatus.OK
        );
    }

    @Test
    void libById404() throws Exception {
        request(
                "get",
                "/libraries/707",
                null,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void registerLib() throws Exception {
        System.out.println("SAVING ALIENS--------------------------------------------- oo xxx -------------");
        request(
                "post",
                "/libraries",
                """
                    {
                        "groupId": "aliens",
                        "artifactId": "super-secret-****",
                        "name": "Overlord"
                    }""",
                HttpStatus.CREATED
        );
    }

    @Test
    void updateLib() throws Exception {
        request(
                "patch",
                "/libraries/1",
                """
                    {
                        "name": "the-thing",
                        "description": "hehe ;)"
                    }""",
                HttpStatus.OK
        );
    }

    @Test
    void updateLib404() throws Exception {
        request(
                "patch",
                "/libraries/7077",
                """
                    {
                        "name": "the-thingy",
                        "description": "hehe ;)"
                    }""",
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void updateLibJustDescription() throws Exception {
        request(
                "patch",
                "/libraries/1",
                """
                    {
                        "description": "hehe ;)"
                    }""",
                HttpStatus.OK
        );
    }

    @Test
    void updateLibbadFormat() throws Exception {
        request(
                "patch",
                "/libraries/1",
                """
                    {
                        "id": 777,
                        "description": "hehe ;)"
                    }""",
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void deleteLib() throws Exception {
        request(
                "delete",
                "/libraries/1",
                null,
                HttpStatus.NO_CONTENT
        );
        System.out.println("AFTER DELETION : ");
        request(
                "get",
                "/libraries",
                null,
                HttpStatus.OK
        );
    }

    @Test
    void deleteLibTwice() throws Exception {
        request(
                "delete",
                "/libraries/1",
                null,
                HttpStatus.NO_CONTENT
        );
        System.out.println("AFTER DELETION : ");
        request(
                "delete",
                "/libraries/1",
                null,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void getLibVersions() throws Exception {
        request(
                "get",
                "/libraries/1/versions",
                null,
                HttpStatus.OK
        );
    }

    @Test
    void getLibVersions404() throws Exception {
        request(
                "get",
                "/libraries/77/versions",
                null,
                HttpStatus.NOT_FOUND
        );
    }

    @Test
    void getLibVersion() throws Exception {
        request(
                "get",
                "/libraries/1/versions/1",
                null,
                HttpStatus.OK
        );
    }

    @Test
    void getLibVersion404() throws Exception {
        request(
                "get",
                "/libraries/1/versions/77",
                null,
                HttpStatus.NOT_FOUND
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
                        """,
                HttpStatus.CREATED
        );
    }

    @Test
    void registerVersionSemVerConflict() throws Exception {
        request("post",
                "/libraries/1/versions",
                """
                        {
                          "semanticVersion": "5.3.11",
                          "description": "Spring Core Framework 5.3.10"
                        }
                        """,
                HttpStatus.CREATED
        );
        request("post",
                "/libraries/1/versions",
                """
                        {
                          "semanticVersion": "5.3.11",
                          "description": "Nes drugo"
                        }
                        """,
                HttpStatus.CONFLICT
        );
    }

    @Test
    void registerVersionsemVerViolation() throws Exception {
        request("post",
                "/libraries/1/versions",
                """
                        {
                          "semanticVersion": "05.3.11",
                          "description": "Spring Core Framework 5.3.10",
                          "deprecated": false
                        }
                        """,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void registerVersionBadFormat() throws Exception {
        request("post",
                "/libraries/1/versions",
                """
                        {
                          "semanticVersion": "1.1.111",
                          "description": "ekeke",
                          "releaseDate": "neki datum",
                          "deprecated": true
                        }
                        """,
                HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void updateVersion() throws Exception {
        request("patch",
                "/libraries/1/versions/1",
                """
                        {
                          "description": "brbrbrbbrbr",
                          "deprecated": true
                        }
                        """,
                HttpStatus.OK
        );
    }

    @Test
    void updateDeprecatedVersion400() throws Exception {
        request("patch",
                "/libraries/1/versions/2",
                """
                        {                 
                          "deprecated": false
                        }
                        """,
                HttpStatus.BAD_REQUEST
        );
    }

    // unauthorized request test todo

    // ----------------------------- helper functions -------------------

    void pp(String method, String url, String body, String status, String json) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        if (json != null) {
            JsonNode node = mapper.readTree(json);

            System.out.println("===========================================================");
            System.out.println(method + " | " + url + " : status = " + status);
            if (method == "post" || method == "patch") {
                JsonNode node1 = mapper.readTree(body);
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node1));
            }
            System.out.println("-----------------------------------------------------------");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
            //System.out.println("===========================================================");
        }
        else {
            System.out.println("===========================================================");
            System.out.println(method + " | " + url + " | status = " + status);
            System.out.println("-----------------------------------------------------------");
            System.out.println("Nothing Here");
            //System.out.println("===========================================================");
        }
    }

    void request(String method, String url, String body, HttpStatus errStatus) throws Exception{

        switch (method) {
            case "get":

                try {
                    var r1 = restClient.get()
                            .uri(url)
                            .header("Authorization", "App la9psd71atbpgeg7fvvx")
                            .retrieve()
                            .toEntity(String.class);
                    then(r1.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, r1.getStatusCode().toString(), r1.getBody());
                } catch (HttpClientErrorException e) {
                    then(e.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, e.getStatusCode().toString(), e.getResponseBodyAsString());
                }

                break;

            case "delete":

                try {
                    var r2 = restClient.delete()
                            .uri(url)
                            .header("Authorization", "App la9psd71atbpgeg7fvvx")
                            .retrieve()
                            .toEntity(String.class);
                    then(r2.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, r2.getStatusCode().toString(), r2.getBody());
                } catch (HttpClientErrorException e) {
                    then(e.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, e.getStatusCode().toString(), e.getResponseBodyAsString());
                }

                break;

            case "post":

                try {
                    var r3 = restClient.post()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "App la9psd71atbpgeg7fvvx")
                            .body(body)
                            .retrieve()
                            .toEntity(String.class);
                    then(r3.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, r3.getStatusCode().toString(), r3.getBody());
                } catch (HttpClientErrorException e) {
                    then(e.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, e.getStatusCode().toString(), e.getResponseBodyAsString());
                }

                break;

            case "patch":

                try {
                    var r4 = restClient.patch()
                            .uri(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "App la9psd71atbpgeg7fvvx")
                            .body(body)
                            .retrieve()
                            .toEntity(String.class);
                    then(r4.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, r4.getStatusCode().toString(), r4.getBody());
                } catch (HttpClientErrorException e) {
                    then(e.getStatusCode()).isEqualTo(errStatus);
                    pp(method, url, body, e.getStatusCode().toString(), e.getResponseBodyAsString());
                }

                break;
        }
    }
}
