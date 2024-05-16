package com.candles.api.candlesapi.persistence;

import java.io.IOException;

import com.candles.api.candlesapi.model.User;
import com.candles.api.candlesapi.model.Candle;
import com.candles.api.candlesapi.model.CreditCard;

public interface UserDAO {
     /**
     * Creates and saves a {@linkplain candle candle}
     * 
     * @param candle {@linkplain candle candle} object to be created and saved
     * <br>
     * The id of the candle object is ignored and a new unique id is assigned
     *
     * @return new {@link candle candle} if successful
     * 
     * @throws IOException if an issue with underlying storage
     */
    //Candle createCandle(Candle candle, boolean isAdmin) throws IOException;

    /**
     * Updates and saves a {@linkplain candle candle}
     * 
     * @param {@link candle candle} object to be updated and saved
     * 
     * @return updated {@link candle candle} if successful, null if
     * {@link candle candle} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    //Candle updateCandle(Candle candle, boolean isAdmin) throws IOException;

    /**
     * Deletes a {@linkplain candle candle} with the given id
     * 
     * @param id The id of the {@link candle candle}
     * 
     * @return true if the {@link candle candle} was deleted
     * <br>
     * false if candle with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    //Candle deleteCandle(int id, boolean isAdmin) throws IOException;

     /**
      * 
      * @param user
      * @return
      * @throws IOException
      */
     User createUser(User user) throws IOException;

     /**
     * Deletes a {@linkplain User user} with the given id
     * 
     * @param id The id of the {@link User user}
     * 
     * @return true if the {@link User user} was deleted
     * <br>
     * false if user with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
     boolean deleteUser(int id) throws IOException;

     /**
      * 
      * @return
      * @throws IOException
      */
     User[] getUsers() throws IOException;

     /**
      * 
      * @param name
      * @return
      * @throws IOException
      */
     User[] findUsers(String name) throws IOException;
     
     /**
      * 
      * @param user
      * @return
      * @throws IOException
      */
     User updateUser(User user) throws IOException;

     /**
      * 
      * @param id
      * @return
      * @throws IOException
      */
     User getUser(int id) throws IOException;

     /**
      * 
      * @param candle
      * @return
      * @throws IOException
      */
    Candle adminCreateCandle(Candle candle) throws IOException;

    /**
     * 
     * @param candle
     * @return
     * @throws IOException
     */
    Candle adminUpdateCandle(Candle candle) throws IOException;

    /**
     * 
     * @param id
     * @return
     * @throws IOException
     */
    boolean adminDeleteCandle(int id) throws IOException;

    CreditCard addCreditCard(int id, CreditCard creditCard) throws IOException;
}