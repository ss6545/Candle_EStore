package com.candles.api.candlesapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a candle entity
 * 
 * @author SWEN Faculty
 */
public class Candle {
    private static final Logger LOG = Logger.getLogger(Candle.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "candle [id=%d, name=%s, price=%.2f, quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private double price;
    @JsonProperty("quantity") private int quantity;

    /**
     * Create a candle with the given id and name
     * @param id The id of the candle
     * @param name The name of the candle
     * @param price The cost of the candle
     * @param quantity The amount of each candle we have 
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Candle(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") double price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Retrieves the id of the candle
     * @return The id of the candle
     */
    public int getId() {return id;}

    /**
     * Sets the name of the candle - necessary for JSON object to Java object deserialization
     * @param name The name of the candle
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the candle
     * @return The name of the candle
     */
    public String getName() {return name;}

    /**
     * Retrives the price of the candle
     * @return The price of the candle
     */
    public double getPrice() {return price;}

    /**
     * Retrives the quantity of candles
     * @return The quantity of candles
     */
    public int getQuantity() {return quantity;}

    /**
     * Updates the quantity of candles
     * @return The new quantity of candles
     */
    public int setQuantity(int quantity) {return this.quantity + quantity;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantity);
    }
}