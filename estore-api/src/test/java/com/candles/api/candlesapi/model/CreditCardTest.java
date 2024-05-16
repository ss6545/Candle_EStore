package com.candles.api.candlesapi.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the candle class
 * 
 * @author Alex Bruno
 */
@Tag("Model-tier")
public class CreditCardTest {
    @Test
    public void testExpiryDateIsValidValidDate() {
        // Setup
        String validExpiryDate = "12/25";
        CreditCard card = new CreditCard(null, null, validExpiryDate, null);

        // Invoke
        boolean isValid = card.expiryDateIsValid();

        // Analyze
        assertTrue(isValid);
    }

    @Test
    public void testExpiryDateIsValidInvalidDate() {
        // Setup
        String invalidExpiryDate = "13/25";
        CreditCard card = new CreditCard(null, null, invalidExpiryDate, null);

        // Invoke
        boolean isValid = card.expiryDateIsValid();

        // Analyze
        assertFalse(isValid);
    }

    @Test
    public void testExpiryDateIsValidDateDNE() {
        // Setup
        String invalidExpiryDate = "132/25";
        CreditCard card = new CreditCard(null, null, invalidExpiryDate, null);

        // Invoke
        boolean isValid = card.expiryDateIsValid();

        // Analyze
        assertFalse(isValid);
    }

    @Test
    public void testCardNumberIsValidValidNumber() {
        // Setup
        String validCardNumber = "1234567890123456";
        CreditCard card = new CreditCard(validCardNumber, null, null, null);

        // Invoke
        boolean isValid = card.cardNumberIsValid();

        // Analyze
        assertTrue(isValid);
    }

    @Test
    public void testCardNumberIsValidInvalidNumber() {
        // Setup
        String invalidCardNumber = "12345";
        CreditCard card = new CreditCard(invalidCardNumber, null, null, null);

        // Invoke
        boolean isValid = card.cardNumberIsValid();

        // Analyze
        assertFalse(isValid);
    }

    @Test
    public void testCardNumberIsValidInvalidCharsValidLength() {
        // Setup
        String invalidCardNumber = "ABCDEFGHIJKLMNOP";
        CreditCard card = new CreditCard(invalidCardNumber, null, null, null);

        // Invoke
        boolean isValid = card.cardNumberIsValid();

        // Analyze
        assertFalse(isValid);
    }

    @Test
    public void testCardNumberIsValidInvalidCharsInValidLength() {
        // Setup
        String invalidCardNumber = "ABCD";
        CreditCard card = new CreditCard(invalidCardNumber, null, null, null);

        // Invoke
        boolean isValid = card.cardNumberIsValid();

        // Analyze
        assertFalse(isValid);
    }

    @Test
    public void testSecCodeIsValidValidCode() {
        // Setup
        String validSecCode = "123";
        CreditCard card = new CreditCard(null, validSecCode, null, null);

        // Invoke
        boolean isValid = card.secCodeIsValid();

        // Analyze
        assertTrue(isValid, "Security code should be valid");
    }

    @Test
    public void testSecCodeIsValidInvalidCode() {
        // Setup
        String invalidSecCode = "12A";
        CreditCard card = new CreditCard(null, invalidSecCode, null, null);

        // Invoke
        boolean isValid = card.secCodeIsValid();

        // Analyze
        assertFalse(isValid, "Security code should be invalid");
    }

    @Test
    public void testSecCodeIsValidInvalidLength() {
        // Setup
        String invalidSecCode = "12345";
        CreditCard card = new CreditCard(null, invalidSecCode, null, null);

        // Invoke
        boolean isValid = card.secCodeIsValid();

        // Analyze
        assertFalse(isValid, "Security code should be invalid");
    }

    @Test
    public void testSecCodeIsValidInvalidCodeInvalidLength() {
        // Setup
        String invalidSecCode = "ABCDE";
        CreditCard card = new CreditCard(null, invalidSecCode, null, null);

        // Invoke
        boolean isValid = card.secCodeIsValid();

        // Analyze
        assertFalse(isValid, "Security code should be invalid");
    }
}
