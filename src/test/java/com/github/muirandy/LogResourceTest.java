package com.github.muirandy;

import com.github.muirandy.docs.living.api.Log;
import com.github.muirandy.docs.living.api.Logs;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbBuilder;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class LogResourceTest {


    private String uniqueId;
    private String uniqueMessage;
    private String uniqueBody;
    private Log log;
    private Logs logs = new Logs();

    @BeforeEach
    void setUp() {
        createLog();
        reset();
    }

    @Test
    void noLogs() {
        markEnd();
        assertRetrieve(logs);
    }

    @Test
    void retrieveNonExistentMarkedLog() {
        assertRetrieve(logs);
    }

    @Test
    void readSingleMarkedLog() {
        logs.add(log);

        storeLog(log);
        markEnd();
        assertRetrieve(logs);
    }

    @Test
    void removeStoredLogs() {
        storeLog(log);
        markEnd();

        reset();

        assertRetrieve(logs);
    }

    @Test
    void readAllLogs() {
        logs.add(log);
        storeLog(log);
        markEnd();

        createLog();
        logs.add(log);
        storeLog(log);

        assertRetrieveAll(logs);
    }

    private void assertRetrieveAll(Logs logs) {
        given().when().get("/log/read")
               .then()
               .statusCode(200)
               .body(is(JsonbBuilder.create().toJson(logs)));
    }

    private void createLog() {
        uniqueId = UUID.randomUUID().toString();
        uniqueMessage = "message" + uniqueId;
        uniqueBody = "Some fancy body";
        log = new Log(uniqueMessage, uniqueBody);
    }

    private void reset() {
        given().when().delete("/log")
               .then()
               .statusCode(204);
    }

    private void markEnd() {
        given().when().post("/log/markEnd/" + uniqueId)
               .then()
               .statusCode(204);
    }

    private void storeLog(Log log) {
        given().header("Content-Type", "application/json")
               .when()
               .body(JsonbBuilder.create().toJson(log))
               .post("/log")
               .then()
               .statusCode(204);
    }

    private void assertRetrieve(Logs logs) {
        given().when().get("/log/read/" + uniqueId)
               .then()
               .statusCode(200)
               .body(is(JsonbBuilder.create().toJson(logs)));
    }
}