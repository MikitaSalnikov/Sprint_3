package api.client;

import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CourierClient {
    String testSrcPath = "src/test/resources/";

    public Response create(String jsonBody) {
        File json = new File(testSrcPath + jsonBody);
        Response create =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        return create;
    }

    public Response auth(String jsonBody) {
        File loginJson = new File(testSrcPath + jsonBody);
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        return auth;
    }

    public void delete(String deleteId) {
        Response delete = given()
                .header("Content-type", "application/json")
                .and()
                .body("{\"id\":" + deleteId + "}")
                .when()
                .delete("/api/v1/courier/" + deleteId);
    }

    public Response isCourierExist(String jsonBody) {
        CourierClient courierClient = new CourierClient();
        courierClient.create(jsonBody);
        return courierClient.auth("loginCourier.json");
    }

    public Response noDuplicate() {
        CourierClient courierClient = new CourierClient();
        courierClient.create("createCourierOK.json");
        return courierClient.create("createCourierOK.json");
    }
}
