package ru.praktikumservices.qascooter;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginCourierTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom();
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void courierCanBeLoginWithValidData() {
        ValidatableResponse courierLoginResponse = courierClient.login(CourierCredentials.from(courier));

        courierLoginResponse.statusCode(200);
        courierId = courierLoginResponse.extract().path("id");
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

}
