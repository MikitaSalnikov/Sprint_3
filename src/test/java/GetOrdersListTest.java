import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    String courierId;
    String orderId;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        File courierJson = new File("src/test/resources/createCourierOK.json");
        Response courierCreate =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierJson)
                        .when()
                        .post("/api/v1/courier");
        File loginJson = new File("src/test/resources/loginCourier.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        String courierId = auth.then().extract().path("id").toString();
        File orderJson = new File("src/test/resources/createOrderBlack.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderJson)
                        .when()
                        .post("/api/v1/orders");
        String orderId = response.then().extract().path("track").toString();
        Response orderToCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{}")
                        .when()
                        .put("/api/v1/orders/accept/"+orderId+"?courierId="+courierId);
    }
    @After
    public void tearDown() {
        try {
            Response delete = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body("{\"id\":" + courierId + "}")
                    .when()
                    .delete("/api/v1/courier/" + courierId);
            Response cancel =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body("{\"track\":" + orderId + "}")
                            .when()
                            .put("/api/v1/orders/cancel");
        }
        catch (Exception e) {
        }
    }
        @Test
    public void getOrdersList(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{}")
                        .when()
                        .get("/api/v1/orders?courierId="+courierId);
        response.then().assertThat().body("orders", notNullValue());
    }
}
