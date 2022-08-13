import api.client.CourierClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CourierClient courierClient = new CourierClient();
        courierClient.create("createCourierOK.json");
    }

    @After
    public void tearDown() {
        try {
            CourierClient courier = new CourierClient();
            courier.delete(courier.auth("loginCourier.json").then().extract().path("id").toString());
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("Check correct auth message and code")
    public void loginOK() {
        CourierClient courier = new CourierClient();
        Response correctAuth = courier.auth("loginCourier.json");
        correctAuth.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Check code and error for auth without login")
    public void loginRequired() {
        CourierClient courier = new CourierClient();
        Response authWOLogin = courier.auth("loginCourierWOLogin.json");
        authWOLogin.then().assertThat().statusCode(400).and().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check code and error for auth without password")
    public void passwordRequired() {
        CourierClient courier = new CourierClient();
        Response authWOPassword = courier.auth("loginCourierWOPassword.json");
        authWOPassword.then().assertThat().statusCode(400).and().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check code and error for auth with wrong login")
    public void wrongLogin() {
        CourierClient courier = new CourierClient();
        Response authWrongLogin = courier.auth("loginCourierWrongLogin.json");
        authWrongLogin.then().assertThat().statusCode(404).and().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check code and error for auth with wrong password")
    public void wrongPassword() {
        CourierClient courier = new CourierClient();
        Response authWrongLogin = courier.auth("loginCourierWrongPassword.json");
        authWrongLogin.then().assertThat().statusCode(404).and().body("message", is("Учетная запись не найдена"));
    }
}
