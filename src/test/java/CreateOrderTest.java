import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String color;

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] dataForTest() {
        return new Object[][]{
                {"src/test/resources/createOrderBlack.json"},
                {"src/test/resources/createOrderGrey.json"},
                {"src/test/resources/createOrderBlackAndGrey.json"},
                {"src/test/resources/createOrderWOColor.json"}
        };
    }

    String track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        try {
            Response cancel =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body("{\"track\":" + track + "}")
                            .when()
                            .put("/api/v1/orders/cancel");
        } catch (Exception e) {
        }
    }

    @Test
    public void trackReturns() {
        File json = new File("src/test/resources/createOrderBlack.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue());
    }

    @Test
    public void createOrder() {
        File json = new File(color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/orders");
        response.then().body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}
