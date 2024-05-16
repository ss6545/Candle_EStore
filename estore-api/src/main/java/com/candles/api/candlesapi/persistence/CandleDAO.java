package com.candles.api.candlesapi.persistence;

import java.io.IOException;

import com.candles.api.candlesapi.model.Candle;

/**
 * Defines the interface for candle object persistence
 * 
 * @author SWEN Faculty
 */
public interface CandleDAO {
    /**
     * Retrieves all {@linkplain candle candles}
     * 
     * @return An array of {@link candle candle} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Candle[] getCandles() throws IOException;

    /**
     * Finds all {@linkplain candle candles} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link candle candles} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Candle[] findCandles(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain candle candle} with the given id
     * 
     * @param id The id of the {@link candle candle} to get
     * 
     * @return a {@link candle candle} object with the matching id
     * <br>
     * null if no {@link candle candle} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Candle getCandle(int id) throws IOException;

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
    Candle createCandle(Candle candle) throws IOException;

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
    Candle updateCandle(Candle candle) throws IOException;

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
    boolean deleteCandle(int id) throws IOException;
}
