package com.candles.api.candlesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public class CreditCard {
    // Package private for tests
    static final String STRING_FORMAT = "CreditCard [cardNumber=%s, cardHolderName=%s, expiryDate=%s, secCode=%s]";

    @JsonProperty("cardNumber") private String cardNumber;
    @JsonProperty("secCode") private String secCode;
    @JsonProperty("expiryDate") private String expiryDate;
    @JsonProperty("cardHolderName") private String cardHolderName;
    
    /**
     * Create a credit card with the given user
     * @param cardNumber 16 digit credit card number
     * @param secCode Three digit security code for the credit card
     * @param expiryDate CC expiry date in the format: MM/YY
     * @param cardHolderName Name of the card holder
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public CreditCard(@JsonProperty("cardNumber") String cardNumber, @JsonProperty("secCode") String secCode, @JsonProperty("expiryDate") String expiryDate, @JsonProperty("cardHolderName") String cardHolderName){
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.secCode = secCode;
        this.cardHolderName = cardHolderName;
    }

    /**
    * Checks if the credit card number is valid.
    * 
    * @return true if the credit card number consists of only numeric characters and has a length of 16 digits, false otherwise.
    */
    public boolean cardNumberIsValid(){
        return this.cardNumber.matches("[0-9]+") && this.cardNumber.length() == 16; // Cheks whether it is a 16 digit number
    }

    /**
    * Checks if the security code (CVC) of the credit card is valid.
    * 
    * @return true if the security code consists of only numeric characters and has a length of 3 digits, false otherwise.
    */
    public boolean secCodeIsValid(){
        return this.secCode.matches("[0-9]+") && this.secCode.length() == 3; // Checks whether it is a 3 digit number
    }

    /**
    * Checks if the expiry date of the credit card is valid.
    * 
    * @return true if the expiry date is in the format "MM/YY" and represents a valid month (greater than 00 and less than or equal to 12), false otherwise.
    */
    public boolean expiryDateIsValid(){
        boolean validity;
        if(this.expiryDate.matches("\\d{2}/\\d{2}")){ // Checks whether it is of the format {MM/YY}
            int month = Integer.parseInt(this.expiryDate.substring(0,2));
            validity = (month>00 && month<=12)? true:false; // Checks whether it is a valid month
        } else {
            validity = false;
        }
        return validity;
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,cardNumber,cardHolderName,expiryDate,secCode);
    }


}
