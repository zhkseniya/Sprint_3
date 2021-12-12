package ru.praktikumservices.qascooter;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Courier creation: /api/v1/courier")
    @Description("Creation of a courier with valid data: login, password, name")
    public void courierCanBeCreatedWithValidData() {
        ValidatableResponse courierCreateResponse = courierClient.create(courier);
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.from(courier));

        courierId = courierLoginResponse.extract().path("id");

        courierCreateResponse.statusCode(201);
        courierCreateResponse.extract().path("ok");
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Creation of two identical couriers: /api/v1/courier")
    @Description("You cannot create two identical couriers")
    public void cannotCreateTwoIdenticalCouriers() {
        courierClient.create(courier);

        ValidatableResponse secondCourierCreateResponse = courierClient.create(courier);

        secondCourierCreateResponse.statusCode(409);
        secondCourierCreateResponse.assertThat().body("message", equalTo("Этот логин уже используется."));
    }
}
