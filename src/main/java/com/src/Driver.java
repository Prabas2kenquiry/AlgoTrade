package com.src;

import com.src.lob.Application;
import com.src.lob.model.Order;
import com.src.lob.model.OrderSide;

import java.math.BigDecimal;

/**
 * The Driver class contains a main method that uses an Application object to
 * add, modify, and delete orders, and print the current state of the order book.
 */
public class Driver {

	/**
     * The main method initializes an Application object, adds several orders to the
     * order book, modifies and deletes some of those orders, and prints the current
     * state of the order book after each modification.
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        Application lobApp = new Application();

        Order order1 = lobApp.addOrder(p("100.10"), 10, OrderSide.BUY);
        Order order2 = lobApp.addOrder(p("100.20"), 45, OrderSide.SELL);
        Order order3 = lobApp.addOrder(p("100.20"), 15, OrderSide.SELL);
        Order order4 = lobApp.addOrder(p("99.90"), 25, OrderSide.BUY);
        Order order5 = lobApp.addOrder(p("98.50"), 5, OrderSide.SELL);
        Order order6 = lobApp.addOrder(p("112.40"), 200, OrderSide.SELL);
        Order order7 = lobApp.addOrder(p("98.50"), 50, OrderSide.BUY);
        Order order8 = lobApp.addOrder(p("98.50"), 60, OrderSide.BUY);
        Order order9 = lobApp.addOrder(p("99.90"), 160, OrderSide.BUY);
        
        // Print the current state of the order book
        lobApp.printState();

        // Modify an order and print the updated state of the order book
        lobApp.modifyOrder(order2.getId(), 145);
        lobApp.printState();

        // Delete an order and print the updated state of the order book
        lobApp.deleteOrder(order9.getId());
        lobApp.printState();

        // Modify another order and print the updated state of the order book
        lobApp.modifyOrder(order8.getId(), 65);
        lobApp.printState();
    }

    /**
     * This private method converts a String price into a BigDecimal object.
     *
     * @param price the price to convert
     * @return a BigDecimal object representing the price
     */
    private static BigDecimal p(String price) {
        return new BigDecimal(price);
    }
}
