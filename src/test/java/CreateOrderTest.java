import api.client.OrdersClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String color;
    String track;

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[] dataForTest() {
        return new Object[][]{
                {"createOrderBlack.json"},
                {"createOrderGrey.json"},
                {"createOrderBlackAndGrey.json"},
                {"createOrderWOColor.json"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        try {
            OrdersClient ordersClient = new OrdersClient();
            ordersClient.cancelOrder(track);
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("Check struct of correct response")
    public void trackReturns() {
        OrdersClient ordersClient = new OrdersClient();
        Response correctAnswer = ordersClient.createOrder("createOrderBlack.json");
        correctAnswer.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check order create with all avail colors")
    public void createOrder() {
        OrdersClient ordersClient = new OrdersClient();
        Response correctAnswer = ordersClient.createOrder(color);
        correctAnswer.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }
}
