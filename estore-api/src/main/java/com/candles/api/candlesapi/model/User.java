package com.candles.api.candlesapi.model;

import java.util.logging.Logger;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class User {
    // Package private for tests
    static final String STRING_FORMAT = "user [id=%d, name=%s, cart=%s, isAdmin=%b, password=%s, creditCard=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cart") private Candle[] cart;
    @JsonProperty("isAdmin") private Boolean isAdmin;
    @JsonProperty("password") private String password;
    @JsonProperty("creditCard") private CreditCard creditCard;

    /**
     * Create a candle with the given id and name
     * @param id The id of the user
     * @param name The name of the user
     * @param cart The users cart
     * @param isAdmin Whether this is an admin account
     * @param creditCard Credit card info for each customer
     * @param password The password of the user
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public User(
        @JsonProperty("id") int id, 
        @JsonProperty("name") String name, 
        @JsonProperty("cart") Candle[] cart, 
        @JsonProperty("isAdmin") Boolean isAdmin, 
        @JsonProperty("password") String password,
        @JsonProperty("creditCard") CreditCard creditCard)
    {
        this.id = id;
        this.name = name;
        this.cart = cart;
        this.isAdmin = isAdmin;
        this.password = password;
        if(creditCard != null) {
            this.creditCard = creditCard;
        } else {
            this.creditCard = new CreditCard("", "", "", "");
        }
    }

    /**
     * Retrieves the id of the user
     * @return The id of the user
     */
    public int getId() {return id;}

    /**
     * Sets the name of the user - necessary for JSON object to Java object deserialization
     * @param name The name of the user
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the user
     * @return The name of the user
     */
    public String getName() {return name;}

    /**
     * Retrives the cart of the user
     * @return The cart of the user
     */
    public Candle[] getCart() {return cart;}

    /**
     * sets the admin privelege of user
     * @param the users admin perms
     */
    public void setAdmin(Boolean perms) {this.isAdmin = perms;}

    /**
     * Retrieves whether a user is an admin
     * @return Admin Privilege
     */
    public Boolean getAdmin() {return isAdmin;}

    /**
    * Returns the credit card associated with this object.
    * 
    * @return The CreditCard object representing the associated credit card.
    */
    public CreditCard getCC() {return creditCard;}

    public CreditCard setCC(CreditCard cc) {
        this.creditCard = cc;
        return this.creditCard;
    }

    /**
     * Retrives the password of the user
     * @return the user's password
     */
    public String getPassword() {return password;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,Arrays.toString(cart),isAdmin, password, String.format(creditCard.toString()));
    }
}