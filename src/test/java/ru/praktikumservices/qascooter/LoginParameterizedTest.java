package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;


import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class LoginParameterizedTest {

    private static CourierClient courierClient = new CourierClient();
    private static Courier courier = Courier.getRandom();
    private final CourierCredentials courierCredentials;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public LoginParameterizedTest(CourierCredentials courierCredentials, int expectedStatus, String expectedErrorMessage) {
        this.courierCredentials = courierCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierLoginData() {
        return new Object[][] {
                {CourierCredentials.getWithLoginOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getWithPasswordOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getWithRandomLoginAndPassword(), 404, "Учетная запись не найдена"},
        };
    }

    @Test
    @DisplayName("Courier login with different data set: api/v1/courier/login/")
    @Description("Courier login: only with login; only with a password; with random username and password")
    public void invalidRequestInNotAllowed() {
        courierClient.create(courier);

        ValidatableResponse response = courierClient.login(courierCredentials);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }

}
