package com.src.lob;

import com.src.lob.model.Order;
import com.src.lob.model.OrderSide;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

/**
 * This class represents an application that manages an order book.
 * An order book is a list of buy and sell orders, with each order
 * containing a price and a quantity.
 *
 * The orders are stored in two data structures:
 * 1. A HashMap called ordersHash that maps order IDs to orders.
 * 2. A HashMap called orderBook that maps order sides (buy/sell) to
 *    another HashMap that maps prices to a PriorityQueue of orders.
 *
 * The addOrder() method adds a new order to the order book, while the
 * deleteOrder() method removes an existing order from the book. The
 * modifyOrder() method updates the quantity of an existing order.
 *
 * The printState() method prints the current state of the order book,
 * which includes a list of all buy and sell orders sorted by price.
 */
public class Application {

	/**
     * A HashMap that maps order IDs to orders.
     */
    private final Map<UUID, Order> ordersHash;
    
    /**
     * A HashMap that maps order sides (buy/sell) to another HashMap
     * that maps prices to a PriorityQueue of orders.
     */
    private final Map<OrderSide, Map<BigDecimal, PriorityQueue<Order>>> orderBook;

    /**
     * Constructs a new Application object with empty order book data structures.
     */
    public Application() {
        ordersHash = new HashMap<>();
        orderBook = new HashMap<>();

        orderBook.put(OrderSide.BUY, new HashMap<>());
        orderBook.put(OrderSide.SELL, new HashMap<>());
    }

    /**
     * Adds a new order to the order book.
     *
     * @param price the price of the order
     * @param quantity the quantity of the order
     * @param side the side of the order (buy/sell)
     * @return the newly added order
     */
    public Order addOrder(BigDecimal price, int quantity, OrderSide side) {
        Order order = new Order(price, quantity, side);
        add(order);
        return order;
    }

    /**
     * Adds an order to the order book and updates the order book data structures.
     *
     * @param order the order to add
     */
    private void add(Order order) {
        orderBook.get(order.getSide())
                .computeIfAbsent(order.getPrice(), k -> new PriorityQueue<>())
                .add(order);
        ordersHash.put(order.getId(), order);
    }

    /**
     * Deletes an order from the order book.
     *
     * @param id the ID of the order to delete
     * @return an Optional containing the deleted order, or an empty Optional if no such order exists
     */
    public Optional<Order> deleteOrder(UUID id) {
        Order order = ordersHash.get(id);
        if (order == null) {
            return Optional.empty();
        }
        delete(order);
        return Optional.of(order);
    }

    /**
     * Deletes an order from the order book and updates the order book data structures.
     *
     * @param order the order to delete
     */
    private void delete(Order order) {
        orderBook.get(order.getSide()).get(order.getPrice()).remove(order);
        ordersHash.remove(order.getId());
    }

    /**
     * Modifies the quantity of an existing order in the order book.
     *
     * @param id the ID of the order to modify
     * @param newQuantity the new quantity of the order
     * @return an Optional containing the modified order, or an empty Optional if no such order exists
     */
    public Optional<Order> modifyOrder(UUID id, int newQuantity) {
        Order order = ordersHash.get(id);
        if (order == null) {
            return Optional.empty();
        }
        delete(order);
        Order modifiedOrder = order.withQuantity(newQuantity);
        add(modifiedOrder);
        return Optional.of(modifiedOrder);
    }

    /**
     * Prints the current state of the order book.
     */
    public void printState() {
        System.out.println("[BUY]");
        System.out.println(buildOutput(orderBook.get(OrderSide.BUY)));

        System.out.println("[SELL]");
        System.out.println(buildOutput(orderBook.get(OrderSide.SELL)));

        System.out.println("////////////////////////////////////////////////////////");
        System.out.println();
    }

    /**
    * Builds a string representation of the order book for a specific order side.
    *
    * @param qs the map of orders to be printed
    * @return a string representation of the order book for the given order side
    */
    private String buildOutput(Map<BigDecimal, PriorityQueue<Order>> qs) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<BigDecimal, PriorityQueue<Order>> es : qs.entrySet()) {
            String orders = es.getValue().stream().map(Order::toString).collect(joining(" << "));
            sb.append(fp(es.getKey())).append("| ").append(orders).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
    * Formats a BigDecimal price to a string with two decimal places.
    *
    * @param price the BigDecimal price to format
    * @return a string representation of the formatted BigDecimal price
    */
    private String fp(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_UP).toString();
    }
}