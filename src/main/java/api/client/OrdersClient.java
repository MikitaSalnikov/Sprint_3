package api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    String testSrcPath = "src/test/resources/";

    public Response createOrder(String jsonBody) {
        File orderJson = new File(testSrcPath + jsonBody);
        Response create =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderJson)
                        .when()
                        .post("/api/v1/orders");
        return create;
    }

    public Response cancelOrder(String orderId) {
        Response cancel =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{\"track\":" + orderId + "}")
                        .when()
                        .put("/api/v1/orders/cancel");
        return cancel;
    }

    public Response orderToCourier(String orderId, String courierId) {
        Response toCourier =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{}")
                        .when()
                        .put("/api/v1/orders/accept/" + orderId + "?courierId=" + courierId);
        return toCourier;
    }

    public Response getOrderList(String courierId) {
        Response orderList =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body("{}")
                        .when()
                        .get("/api/v1/orders?courierId=" + courierId);
        return orderList;
    }
    public String prepareOrderForCourier(){
        CourierClient courierClient = new CourierClient();
        courierClient.create("createCourierOK.json");
        String courierId = courierClient.auth("loginCourier.json").then().extract().path("id").toString();
        OrdersClient ordersClient = new OrdersClient();
        String track = ordersClient.createOrder("createOrderBlack.json").then().extract().path("track").toString();
        ordersClient.orderToCourier(track, courierId);
        return track;
    }
}
