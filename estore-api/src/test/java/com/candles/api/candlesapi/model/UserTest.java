package com.candles.api.candlesapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the candle class
 * 
 * @author Jack Franczyk
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        Candle[] expected_cart = {};
        boolean expected_isAdmin = false;
        String expected_password = "Password123";
        CreditCard creditCard = null;

        // Invoke
        User user = new User(expected_id,expected_name,expected_cart,expected_isAdmin,expected_password,creditCard);

        // Analyze
        assertEquals(expected_id,user.getId());
        assertEquals(expected_name,user.getName());
        assertEquals(expected_cart, user.getCart());
        assertEquals(expected_isAdmin, user.getAdmin());
        assertEquals(expected_password, user.getPassword());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        Candle[] cart = {};
        boolean isAdmin = false;
        String password = "Password123";
        CreditCard creditCard = null;
        User user = new User(id,name,cart,isAdmin,password,creditCard);

        String expected_name = "Galactic Agent";

        // Invoke
        user.setName(expected_name);

        // Analyze
        assertEquals(expected_name,user.getName());
    }

    @Test
    public void testToString() {
        int id = 99;
        String name = "Wi-Fire";
        Candle[] cart = new Candle[0];
        boolean isAdmin = false;
        String password = "Password123";
        String expected_string = String.format(User.STRING_FORMAT,id,name,Arrays.toString(cart),isAdmin,password,String.format(CreditCard.STRING_FORMAT,"","","",""));
        User user = new User(id,name,cart,isAdmin,password,null);

        // Invoke
        String actual_string = user.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}