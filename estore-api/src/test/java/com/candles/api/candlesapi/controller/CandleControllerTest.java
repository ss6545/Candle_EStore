package com.candles.api.candlesapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.candles.api.candlesapi.model.Candle;
import com.candles.api.candlesapi.persistence.CandleDAO;

/**
 * Test the candle Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class CandleControllerTest {
    private CandleController candleController;
    private CandleDAO mockCandleDAO;

    /**
     * Before each test, create a new candleController object and inject
     * a mock candle DAO
     */
    @BeforeEach
    public void setupCandleController() {
        mockCandleDAO = mock(CandleDAO.class);
        candleController = new CandleController(mockCandleDAO);
    }

    @Test
    public void testGetCandle() throws IOException {  // getCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 1", 14.99, 1);
        // When the same id is passed in, our mock candle DAO will return the candle object
        when(mockCandleDAO.getCandle(candle.getId())).thenReturn(candle);

        // Invoke
        ResponseEntity<Candle> response = candleController.getCandle(candle.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(candle,response.getBody());
    }

    @Test
    public void testGetCandleNotFound() throws Exception { // createCandle may throw IOException
        // Setup
        int candleId = 99;
        // When the same id is passed in, our mock candle DAO will return null, simulating
        // no candle found
        when(mockCandleDAO.getCandle(candleId)).thenReturn(null);

        // Invoke
        ResponseEntity<Candle> response = candleController.getCandle(candleId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetCandleHandleException() throws Exception { // createCandle may throw IOException
        // Setup
        int candleId = 99;
        // When getCandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).getCandle(candleId);

        // Invoke
        ResponseEntity<Candle> response = candleController.getCandle(candleId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all candleController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateCandle() throws IOException {  // createCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 2", 14.99, 2);
        // when createCandle is called, return true simulating successful
        // creation and save
        when(mockCandleDAO.createCandle(candle)).thenReturn(candle);

        // Invoke
        ResponseEntity<Candle> response = candleController.createCandle(candle);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(candle,response.getBody());
    }

    @Test
    public void testCreateCandleFailed() throws IOException {  // createCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 3", 14.99, 3);
        when(mockCandleDAO.findCandles(candle.getName())).thenReturn(
            new Candle[]{new Candle(3,"Scent 3", 16.99, 10)}
        ); //candle with same name already exists

        // Invoke
        ResponseEntity<Candle> response = candleController.createCandle(candle);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateCandleHandleException() throws IOException {  // createCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 4", 14.99, 1);

        // When createCandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).createCandle(candle);

        // Invoke
        ResponseEntity<Candle> response = candleController.createCandle(candle);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateCandle() throws IOException { // updateCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 5", 14.99, 5);
        // when updateCandle is called, return true simulating successful
        // update and save
        when(mockCandleDAO.updateCandle(candle)).thenReturn(candle);
        ResponseEntity<Candle> response = candleController.updateCandle(candle);
        candle.setName("Scent 6");

        // Invoke
        response = candleController.updateCandle(candle);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(candle,response.getBody());
    }

    @Test
    public void testUpdateCandleFailed() throws IOException { // updateCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 1", 14.99, 1);
        // when updateCandle is called, return true simulating successful
        // update and save
        when(mockCandleDAO.updateCandle(candle)).thenReturn(null);

        // Invoke
        ResponseEntity<Candle> response = candleController.updateCandle(candle);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateCandleHandleException() throws IOException { // updateCandle may throw IOException
        // Setup
        Candle candle = new Candle(99,"Scent 1", 14.99, 2);
        // When updateCandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).updateCandle(candle);

        // Invoke
        ResponseEntity<Candle> response = candleController.updateCandle(candle);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCandles() throws IOException { // getCandles may throw IOException
        // Setup
        Candle[] candles = new Candle[2];
        candles[0] = new Candle(99,"Scent A", 14.99, 1);
        candles[1] = new Candle(100,"Scent B", 14.99, 1);
        // When getCandles is called return the candles created above
        when(mockCandleDAO.getCandles()).thenReturn(candles);

        // Invoke
        ResponseEntity<Candle[]> response = candleController.getCandles();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(candles,response.getBody());
    }

    @Test
    public void testGetCandlesHandleException() throws IOException { // getCandles may throw IOException
        // Setup
        // When getCandles is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).getCandles();

        // Invoke
        ResponseEntity<Candle[]> response = candleController.getCandles();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchCandles() throws IOException { // findcandles may throw IOException
        // Setup
        String searchString = "la";
        Candle[] candles = new Candle[2];
        candles[0] = new Candle(99,"Scent A", 14.99, 1);
        candles[1] = new Candle(100,"Scent B", 14.99, 1);
        // When findcandles is called with the search string, return the two
        /// candles above
        when(mockCandleDAO.findCandles(searchString)).thenReturn(candles);

        // Invoke
        ResponseEntity<Candle[]> response = candleController.searchCandles(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(candles,response.getBody());
    }

    @Test
    public void testSearchCandleNotFound() throws IOException {
        // Setup
        String name = "nonexistentCandle";

        // When findCandles is called on the Mock candle DAO, return null aka no candle found
        when(mockCandleDAO.findCandles(name)).thenReturn(null);

        // Invoke
        ResponseEntity<Candle[]> response = candleController.searchCandles(name);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchCandlesHandleException() throws IOException { // findcandles may throw IOException
        // Setup
        String searchString = "an";
        // When createCandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).findCandles(searchString);

        // Invoke
        ResponseEntity<Candle[]> response = candleController.searchCandles(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteCandle() throws IOException { // deletecandle may throw IOException
        // Setup
        int candleId = 99;
        // when deletecandle is called return true, simulating successful deletion
        when(mockCandleDAO.deleteCandle(candleId)).thenReturn(true);

        // Invoke
        ResponseEntity<Candle> response = candleController.deleteCandle(candleId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteCandleNotFound() throws IOException { // deletecandle may throw IOException
        // Setup
        int candleId = 99;
        // when deletecandle is called return false, simulating failed deletion
        when(mockCandleDAO.deleteCandle(candleId)).thenReturn(false);

        // Invoke
        ResponseEntity<Candle> response = candleController.deleteCandle(candleId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteCandleHandleException() throws IOException { // deletecandle may throw IOException
        // Setup
        int candleId = 99;
        // When deletecandle is called on the Mock candle DAO, throw an IOException
        doThrow(new IOException()).when(mockCandleDAO).deleteCandle(candleId);

        // Invoke
        ResponseEntity<Candle> response = candleController.deleteCandle(candleId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
