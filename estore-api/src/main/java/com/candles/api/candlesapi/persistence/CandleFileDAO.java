package com.candles.api.candlesapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.candles.api.candlesapi.model.Candle;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for candles
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class CandleFileDAO implements CandleDAO {
    private static final Logger LOG = Logger.getLogger(CandleFileDAO.class.getName());
    Map<Integer,Candle> candles;   // Provides a local cache of the candle objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between candle
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new candle
    private String filename;    // Filename to read from and write to

    /**
     * Creates a candle File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CandleFileDAO(@Value("${candles.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the candles from the file
    }

    /**
     * Generates the next id for a new {@linkplain candle candle}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain candle candles} from the tree map
     * 
     * @return  The array of {@link candle candles}, may be empty
     */
    private Candle[] getCandlesArray() {
        return getCandlesArray(null);
    }

    /**
     * Generates an array of {@linkplain candle candles} from the tree map for any
     * {@linkplain candle candles} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain candle candles}
     * in the tree map
     * 
     * @return  The array of {@link candle candles}, may be empty
     */
    private Candle[] getCandlesArray(String containsText) { // if containsText == null, no filter
        ArrayList<Candle> candleArrayList = new ArrayList<>();
        
        // Cycles through all candles to either find candles with names that contains
        // the argument containsText and add them to the empty array created or all 
        // the all canadles will be added to array if containsText is null
        for (Candle candle : candles.values()) {
            if (containsText == null || candle.getName().toLowerCase().contains(containsText.toLowerCase())) { // making sure that the candle search is not case sensitive
                candleArrayList.add(candle);
            }
        }

        // Convert arraylist to a native array of Candle objects
        Candle[] candleArray = new Candle[candleArrayList.size()];
        candleArrayList.toArray(candleArray);
        return candleArray;
    }

    /**
     * Saves the {@linkplain candle candles} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link candle candles} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Candle[] candleArray = getCandlesArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),candleArray);
        return true;
    }

    /**
     * Loads {@linkplain candle candles} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        candles = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of candles
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Candle[] candleArray = objectMapper.readValue(new File(filename),Candle[].class);

        // Add each candle to the tree map and keep track of the greatest id
        for (Candle candle : candleArray) {
            candles.put(candle.getId(),candle);
            if (candle.getId() > nextId)
                nextId = candle.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Candle[] getCandles() {
        synchronized(candles) {
            return getCandlesArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Candle[] findCandles(String containsText) {
        synchronized(candles) {
            // Using text, we can look through the array of candles and look at its
            // name to see if it contains the text from the argument containsText
            return getCandlesArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Candle getCandle(int id) {
        synchronized(candles) {
            if (candles.containsKey(id))
                return candles.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Candle createCandle(Candle candle) throws IOException {
        synchronized(candles) {
            // We create a new candle object because the id field is immutable
            // and we need to assign the next unique id
            Candle newcandle = new Candle(nextId(),candle.getName(),candle.getPrice(),candle.getQuantity());
            candles.put(newcandle.getId(),newcandle);
            save(); // may throw an IOException
            return newcandle;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Candle updateCandle(Candle candle) throws IOException {
        synchronized(candles) {
            if (candles.containsKey(candle.getId()) == false)
                return null;  // candle does not exist

            candles.put(candle.getId(),candle);
            save(); // may throw an IOException
            return candle;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteCandle(int id) throws IOException {
        synchronized(candles) {
            if (candles.containsKey(id)) {
                candles.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
