import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
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
    public void code201(){
        File json = new File("src/test/resources/createCourierOK.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().statusCode(201);
    }
    @Test
    public void bodyIsCorrect(){
        File json = new File("src/test/resources/createCourierOK.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", is(true));
    }
    @Test
    public void createCourier(){
        File json = new File("src/test/resources/createCourierOK.json");
        File loginJson = new File("src/test/resources/loginCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        Response auth =
                given()
                .header("Content-type", "application/json")
                .and()
                .body(loginJson)
                .when()
                .post("/api/v1/courier/login");
        auth.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }
    @Test
    public void noDuplicateCode(){
        File json = new File("src/test/resources/createCourierOK.json");
        Response first =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        Response duplicate =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        duplicate.then().assertThat().statusCode(409);
    }
    @Test
    public void noDuplicateMessage(){
        File json = new File("src/test/resources/createCourierOK.json");
        Response first =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        Response duplicate =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        duplicate.then().assertThat().body("code", is(409))
                .and()
                .body("message", is("Этот логин уже используется"));
    }
    @Test
    public void loginRequired(){
        File json = new File("src/test/resources/createCourierWOLogin.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
    @Test
    public void passwordRequired(){
        File json = new File("src/test/resources/createCourierWOPassword.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }
}
