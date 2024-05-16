package com.candles.api.candlesapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.candles.api.candlesapi.model.Candle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the candle File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class CandleFileDAOTest {
    CandleFileDAO candleFileDAO;
    Candle[] testcandles;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupcandleFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testcandles = new Candle[3];
        testcandles[0] = new Candle(99,"Scent 1",14.99,1);
        testcandles[1] = new Candle(100,"Scent 2",14.99,1);
        testcandles[2] = new Candle(101,"Scent 3",14.99,1);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the candle array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Candle[].class))
                .thenReturn(testcandles);
        candleFileDAO = new CandleFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetcandles() {
        // Invoke
        Candle[] candles = candleFileDAO.getCandles();

        // Analyze
        assertEquals(candles.length,testcandles.length);
        for (int i = 0; i < testcandles.length;++i)
            assertEquals(candles[i],testcandles[i]);
    }

    @Test
    public void testFindcandles() {
        // Invoke
        Candle[] testCandles0 = candleFileDAO.findCandles("Sc");

        // Analyze
        assertEquals(testCandles0.length,3);
        assertEquals(testCandles0[0],testcandles[0]);
        assertEquals(testCandles0[1],testcandles[1]);
        assertEquals(testCandles0[2],testcandles[2]);
        
        // Invoke
        Candle[] testCandles1 = candleFileDAO.findCandles("2");

        // Analyze
        assertEquals(testCandles1.length,1);
        assertEquals(testCandles1[0],testcandles[1]);
    }

    @Test
    public void testGetcandle() {
        // Invoke
        Candle candle = candleFileDAO.getCandle(99);

        // Analzye
        assertEquals(candle,testcandles[0]);
    }

    @Test
    public void testDeletecandle() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> candleFileDAO.deleteCandle(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test candles array - 1 (because of the delete)
        // Because candles attribute of candleFileDAO is package private
        // we can access it directly
        assertEquals(candleFileDAO.candles.size(),testcandles.length-1);
    }

    @Test
    public void testCreatecandle() {
        // Setup
        Candle candle = new Candle(102,"Scent 4",14.99,1);

        // Invoke
        Candle result = assertDoesNotThrow(() -> candleFileDAO.createCandle(candle),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Candle actual = candleFileDAO.getCandle(candle.getId());
        assertEquals(actual.getId(),candle.getId());
        assertEquals(actual.getName(),candle.getName());
    }

    @Test
    public void testUpdatecandle() {
        // Setup
        Candle candle = new Candle(99,"Scent 1",14.99,1);

        // Invoke
        Candle result = assertDoesNotThrow(() -> candleFileDAO.updateCandle(candle),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Candle actual = candleFileDAO.getCandle(candle.getId());
        assertEquals(actual,candle);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Candle[].class));

        Candle candle = new Candle(102,"Scent 4",14.99,1);

        assertThrows(IOException.class,
                        () -> candleFileDAO.createCandle(candle),
                        "IOException not thrown");
    }

    @Test
    public void testGetcandleNotFound() {
        // Invoke
        Candle candle = candleFileDAO.getCandle(98);

        // Analyze
        assertEquals(candle,null);
    }

    @Test
    public void testDeletecandleNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> candleFileDAO.deleteCandle(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(candleFileDAO.candles.size(),testcandles.length);
    }

    @Test
    public void testUpdatecandleNotFound() {
        // Setup
        Candle candle = new Candle(98,"Scent 0",14.99,1);

        // Invoke
        Candle result = assertDoesNotThrow(() -> candleFileDAO.updateCandle(candle),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the candleFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Candle[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new CandleFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
