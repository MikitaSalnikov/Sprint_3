import api.client.CourierClient;
import api.client.OrdersClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest {
    String courierId;
    String orderId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        OrdersClient ordersClient = new OrdersClient();
        orderId = ordersClient.prepareOrderForCourier();
        CourierClient courierClient = new CourierClient();
        courierId = courierClient.auth("loginCourier.json").then().extract().path("id").toString();
    }

    @After
    public void tearDown() {
        try {
            CourierClient courierClient = new CourierClient();
            courierClient.delete(courierId);
            OrdersClient ordersClient = new OrdersClient();
            ordersClient.cancelOrder(orderId);
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("Check correct get order list request")
    public void getOrdersList() {
        OrdersClient ordersClient = new OrdersClient();
        Response getOrderList = ordersClient.getOrderList(courierId);
        getOrderList.then().assertThat().body("orders", notNullValue());
    }
}
