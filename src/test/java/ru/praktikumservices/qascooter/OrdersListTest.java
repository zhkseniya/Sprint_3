package ru.praktikumservices.qascooter;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;


import static org.hamcrest.Matchers.*;

public class OrdersListTest {

    private static OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Check status code and not null value body of /api/v1/orders")
    @Description("Basic test for /api/v1/orders endpoint")
    public void checkingGettingOrdersList() {
        ValidatableResponse response = orderClient.getOrdersList();
        response.assertThat().statusCode(200);
        response.body("orders", notNullValue());
    }

}
