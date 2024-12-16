package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ReqResTest {
    @BeforeAll
    public static void Setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    //Получение всех пользователей
    @Test
    public void testGetListUsers() {
        Response response = given()
                .when()
                .get("/users?page=2");
        response.then()
                .statusCode(200)
                .body("page", equalTo(2));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    //Получение пользователя по id = 2
    @Test
    public void testGetSingleUser() {
        Response response = given()
                .when()
                .get("/users/2");
        response.then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    //Получение пользователя по id которого нет
    @Test
    public void testGetSingleUserNotFound() {
        Response response = given()
                .when()
                .get("/users/23");
        response.then()
                .statusCode(404)
                .body(equalTo("{}"));

        assertEquals(404, response.statusCode(), "Expected 404");
        assertEquals("{}", response.body().asString(), "Response body should be an empty JSON object");
    }

    //Получение списка ресурсов
    @Test
    public void testGetListResources() {
        Response response = given()
                .when()
                .get("/unknown");
        response.then()
                .statusCode(200)
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    //Получение одного ресурса по id = 2
    @Test
    public void testGetSingleResource() {
        Response response = given()
                .when()
                .get("/unknown/2");
        response.then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo("fuchsia rose"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertNotNull(response.body(), "Response body should not be null");
    }

    //Получение одного ресурса по id которого нет
    @Test
    public void testGetSingleResourceNotFound() {
        Response response = given()
                .when()
                .get("/unknown/23");
        response.then()
                .statusCode(404)
                .body(equalTo("{}"));
        assertEquals(404, response.statusCode(), "Expected 200");
        assertEquals("{}", response.body().asString(), "Response body should be an empty JSON object");
    }

    //Создание пользователя
    @Test
    public void testPostCreate() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"name\": \"morpheus\", " + "\"job\": \"leader\"}")
                .when()
                .post("/users");
        response.then()
                .statusCode(201)
                .body("name", equalTo("morpheus"));
        assertEquals(201, response.statusCode(), "Expected 201");
        assertTrue(response.body().asString().contains("morpheus"), "Response should contains 'morpheus'");
    }

    //Обновление пользователя
    @Test
    public void testPutUpdate() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"name\": \"morpheus\", " + "\"job\": \"zion resident\"}")
                .when()
                .put("/users/2");
        response.then()
                .statusCode(200)
                .body("job", equalTo("zion resident"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertTrue(response.body().asString().contains("zion resident"), "Response should contains 'zion resident'");
    }

    //Частичное обновление пользователя
    @Test
    public void testPatchUpdate() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"name\": \"morpheus\", " + "\"job\": \"zion resident\"}")
                .when()
                .patch("/users/2");
        response.then()
                .statusCode(200)
                .body("job", equalTo("zion resident"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertTrue(response.body().asString().contains("zion resident"), "Response should contains 'zion resident'");
    }

    //Удаление пользователя
    @Test
    public void testDelete() {
        Response response = given()
                .when()
                .delete("/users/2");
        response.then()
                .statusCode(204);
        assertEquals(204, response.statusCode(), "Expected 204");
        assertTrue(response.body().asString().isEmpty(), "Response body should be empty");
    }

    //Успешная регистрация
    @Test
    public void testPostRegisterSuccessful() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"email\": \"eve.holt@reqres.in\", " + "\"password\": \"pistol\"}")
                .when()
                .post("/register");
        response.then()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertTrue(response.body().asString().contains("QpwL5tke4Pnpja7X4"), "Response should contains 'QpwL5tke4Pnpja7X4'");
    }

    //Неуспешная регистрация
    @Test
    public void testPostRegisterUnsuccessful() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"email\": \"eve.holt@reqres.in\"}")
                .when()
                .post("/register");
        response.then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
        assertEquals(400, response.statusCode(), "Expected 400");
        assertTrue(response.body().asString().contains("Missing password"), "Response should contains 'Missing password'");
    }

    //Успешный логин
    @Test
    public void testPostLoginSuccessful() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"email\": \"eve.holt@reqres.in\", " + "\"password\": \"cityslicka\"}")
                .when()
                .post("/login");
        response.then()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertTrue(response.body().asString().contains("QpwL5tke4Pnpja7X4"), "Response should contains 'QpwL5tke4Pnpja7X4'");
    }

    //Неуспешный логин
    @Test
    public void testPostLoginUnsuccessful() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{" + "\"email\": \"peter@klaven\"}")
                .when()
                .post("/login");
        response.then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
        assertEquals(400, response.statusCode(), "Expected 400");
        assertTrue(response.body().asString().contains("Missing password"), "Response should contains 'Missing password'");
    }

    //Получение пользователя по id = 2
    @Test
    public void testGetDelayedResponse() {
        Response response = given()
                .when()
                .get("users?delay=3");
        response.then()
                .statusCode(200)
                .body("total_pages", equalTo(2))
                .body("total", equalTo(12));
        assertEquals(200, response.statusCode(), "Expected 200");
        assertNotNull(response.body(), "Response body should not be null");
    }
}