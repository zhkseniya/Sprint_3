package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderParameterizedTest {

    private static OrderClient orderClient = new OrderClient();
    private Order order;
    private List<ScooterColor> color;
    private int trackId;
    private final int expectedStatus;

    public OrderParameterizedTest(List<ScooterColor> color, int expectedStatus) {
        this.color = color;
        this.expectedStatus = expectedStatus;
    }

   @After
    public void tearDown() {
        orderClient.cancel(trackId);
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {List.of(ScooterColor.BLACK, ScooterColor.GREY), 201},
                {List.of(ScooterColor.BLACK), 201},
                {List.of(), 201},
        };
    }

    @Test
    @DisplayName("Creating an order with a different set of colors: /api/v1/orders")
    @Description("Creation of an order with two colors; with only one color; without color ")
    public void orderCanBeCreated() {

        order = Order.getOrder(color);

        ValidatableResponse response = orderClient.create(order);
        response.assertThat().statusCode(expectedStatus);
        trackId = response.extract().path("track");

        ValidatableResponse responseOrder = orderClient.getOrder(trackId);
        responseOrder.body("order", notNullValue());
    }
}
