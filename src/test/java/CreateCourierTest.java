import api.client.CourierClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
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
    @DisplayName("Check code and body for correct courier create")
    public void correctCreateResponse() {
        CourierClient courierClient = new CourierClient();
        Response correctCreate = courierClient.create("createCourierOK.json");
        correctCreate.then().assertThat().statusCode(201).and().body("ok", is(true));
    }

    @Test
    @DisplayName("New courier exists")
    public void isCourierExist() {
        CourierClient courierClient = new CourierClient();
        Response courierExists = courierClient.isCourierExist("createCourierOK.json");
        courierExists.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Check error message and code on duplicate couriers")
    public void noDuplicateCode() {
        CourierClient courierClient = new CourierClient();
        Response noDuplicate = courierClient.noDuplicate();
        noDuplicate.then().assertThat().statusCode(409).and().body("message", is("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Check error message and code for create courier without login")
    public void loginRequired() {
        CourierClient courierClient = new CourierClient();
        Response loginErrorCreate = courierClient.create("createCourierWOLogin.json");
        loginErrorCreate.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check error message and code for create courier without password")
    public void passwordRequired() {
        CourierClient courierClient = new CourierClient();
        Response loginErrorCreate = courierClient.create("createCourierWOPassword.json");
        loginErrorCreate.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
