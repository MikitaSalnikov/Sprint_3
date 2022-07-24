import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        File json = new File("src/test/resources/createCourierOK.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
    }
    @After
    public void tearDown(){
        try {
            File loginJson = new File("src/test/resources/loginCourier.json");
            Response auth =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(loginJson)
                            .when()
                            .post("/api/v1/courier/login");
            String deleteId = auth.then().extract().path("id").toString();
            Response delete = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body("{\"id\":" + deleteId + "}")
                    .when()
                    .delete("/api/v1/courier/" + deleteId);
        }
        catch (Exception e) {
        }
    }
    @Test
    public void loginOK(){
        File loginJson = new File("src/test/resources/loginCourier.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        auth.then().assertThat().body("id", notNullValue());
    }
    @Test
    public void loginRequired(){
        File loginJson = new File("src/test/resources/loginCourierWOLogin.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        auth.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
    @Test
    public void passwordRequired(){
        File loginJson = new File("src/test/resources/loginCourierWOPassword.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        auth.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }
    @Test
    public void wrongLogin(){
        File loginJson = new File("src/test/resources/loginCourierWrongLogin.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        auth.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
    @Test
    public void wrongPassword(){
        File loginJson = new File("src/test/resources/loginCourierWrongPassword.json");
        Response auth =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginJson)
                        .when()
                        .post("/api/v1/courier/login");
        auth.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}
