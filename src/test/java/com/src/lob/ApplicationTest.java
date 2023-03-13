package com.src.lob;

import com.src.lob.model.Order;
import com.src.lob.model.OrderSide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Application} class.
 */
class ApplicationTest {

	 /**
     * The component under test.
     */
    private Application cut;

    /**
     * Sets up the test fixture before each test method is run.
     */
    @BeforeEach
    void setUp() {
        cut = new Application();
    }

    /**
     * Tests that invalid orders cannot be created.
     */
    @Test
    void disallowInvalidOrderCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Order(null, 5, OrderSide.BUY));
        assertThrows(IllegalArgumentException.class, () -> new Order(BigDecimal.TEN, -5, OrderSide.SELL));
        assertThrows(IllegalArgumentException.class, () -> new Order(BigDecimal.TEN, 105, null));
    }

    /**
     * Tests that the created order has the correct id and placed values set.
     */
    @Test
    void createdOrderHasIdAndPlacedValuesSet() {
        Order order = cut.addOrder(BigDecimal.TEN, 20, OrderSide.SELL);
        assertAll(() -> assertNotNull(order.getId()), () -> assertNotNull(order.getPlaced()));
    }

    /**
     * Tests that an order can be deleted.
     */
    @Test
    void deleteOrder() {
        Order order = cut.addOrder(BigDecimal.TEN, 20, OrderSide.SELL);
        assertAll(() -> assertTrue(cut.deleteOrder(order.getId()).isPresent()),
                () -> assertTrue(cut.deleteOrder(order.getId()).isEmpty()));
    }

    /**
     * Tests that modifying an order updates the placed value.
     *
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    @Test
    void modifyOrderShouldUpdatePlacedValue() throws InterruptedException {
        Order order = cut.addOrder(BigDecimal.TEN, 20, OrderSide.SELL);
        sleep(1L);
        Optional<Order> modifiedOrder = cut.modifyOrder(order.getId(), 30);
        assertAll(() -> assertTrue(modifiedOrder.isPresent()),
                () -> assertEquals(order.getId(), modifiedOrder.get().getId()),
                () -> assertEquals(30, modifiedOrder.get().getQuantity()),
                () -> assertTrue(ChronoUnit.NANOS.between(order.getPlaced(), modifiedOrder.get().getPlaced()) != 0L));
    }
}