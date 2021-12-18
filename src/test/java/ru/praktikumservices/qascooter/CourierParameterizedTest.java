package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class CourierParameterizedTest {

    private CourierClient courierClient = new CourierClient();
    private final Courier courier;
    private final int expectedStatus;
    private final String expectedErrorMessage;

    public CourierParameterizedTest(Courier courier, int expectedStatus, String expectedErrorMessage) {
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierData() {
        return new Object[][] {
                {Courier.getWithLoginOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithFirstNameOnly(), 400, "Недостаточно данных для создания учетной записи"},
        };
    }

    @Test
    @DisplayName("Сreating a courier with a different set of parameters: /api/v1/courier")
    @Description("Courier creation: with login only; only with a password; only with a name")
    public void cannotCreateCourierWithInvalidParameters() {

        ValidatableResponse response = courierClient.create(courier);

        response.assertThat().statusCode(expectedStatus);
        response.assertThat().body("message", equalTo(expectedErrorMessage));
    }

}
