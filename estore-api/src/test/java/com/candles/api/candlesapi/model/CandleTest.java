package com.candles.api.candlesapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the candle class
 * 
 * @author SWEN Faculty 
 */
@Tag("Model-tier")
public class CandleTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        double expected_price = 14.99;
        int expected_quantity = 1;

        // Invoke
        Candle candle = new Candle(expected_id,expected_name,expected_price,expected_quantity);

        // Analyze
        assertEquals(expected_id,candle.getId());
        assertEquals(expected_name,candle.getName());
        assertEquals(expected_price, candle.getPrice());
        assertEquals(expected_quantity, candle.getQuantity());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        double price = 14.99;
        int quantity = 1;
        Candle candle = new Candle(id,name,price,quantity);

        String expected_name = "Galactic Agent";

        // Invoke
        candle.setName(expected_name);

        // Analyze
        assertEquals(expected_name,candle.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        double price = 14.99;
        int quantity = 1;
        String expected_string = String.format(Candle.STRING_FORMAT,id,name,price,quantity);
        Candle candle = new Candle(id,name,price,quantity);

        // Invoke
        String actual_string = candle.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}