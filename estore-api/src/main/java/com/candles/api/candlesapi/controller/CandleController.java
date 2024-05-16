package com.candles.api.candlesapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.candles.api.candlesapi.model.Candle;
import com.candles.api.candlesapi.persistence.CandleDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the candle resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty
 */

@RestController
@RequestMapping("candles")
public class CandleController {
    private static final Logger LOG = Logger.getLogger(CandleController.class.getName());
    private CandleDAO candleDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param candleDao The {@link CandleDAO candle Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CandleController(CandleDAO candleDao) {
        this.candleDao = candleDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Candle candle} for the given id
     * 
     * @param id The id used to locate the {@link Candle candle}
     * 
     * @return ResponseEntity with {@link Candle candle} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Candle> getCandle(@PathVariable int id) {
        LOG.info("GET /candles/" + id);
        try {
            Candle candle = candleDao.getCandle(id);
            if (candle != null)
                return new ResponseEntity<Candle>(candle,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Candle candles}
     * 
     * @return ResponseEntity with array of {@link Candle candle} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Candle[]> getCandles() {
        LOG.info("GET /candles");
        try {
            Candle[] candles = candleDao.getCandles();
            return new ResponseEntity<Candle[]>(candles,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Candle candles} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Candle candles}
     * 
     * @return ResponseEntity with array of {@link Candle candle} (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all candles that contain the text "ma"
     * GET http://localhost:8080/candles/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Candle[]> searchCandles(@RequestParam String name) {
        LOG.info("GET /candles/?name="+name);
        try {
            Candle[] candles = candleDao.findCandles(name);
            if (candles != null)
                return new ResponseEntity<Candle[]>(candles,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Candle candle} with the provided candle object
     * 
     * @param candle - The {@link Candle candle} to create
     * 
     * @return ResponseEntity with created {@link Candle candle} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if a {@link Candle candle} with the same name already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Candle> createCandle(@RequestBody Candle candle) {
        LOG.info("POST /candles " + candle);
        try {
            if(candleDao.findCandles(candle.getName()) != null){
                for (Candle existingCandle : candleDao.findCandles(candle.getName())) {
                    if (existingCandle.getName().equals(candle.getName()))
                        return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            Candle newCandle = candleDao.createCandle(candle);
            return new ResponseEntity<Candle>(newCandle,HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Candle candle} with the provided {@linkplain Candle candle} object, if it exists
     * 
     * @param candle The {@link Candle candle} to update
     * 
     * @return ResponseEntity with updated {@link Candle candle} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Candle> updateCandle(@RequestBody Candle candle) {
        LOG.info("PUT /candles " + candle);
        try {
            Candle newCandle = candleDao.updateCandle(candle);
            if (newCandle != null)
                return new ResponseEntity<Candle>(newCandle,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Candle candle} with the given id
     * 
     * @param id The id of the {@link Candle candle} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Candle> deleteCandle(@PathVariable int id) {
        LOG.info("DELETE /candles/" + id);
        try {
            boolean exists = candleDao.deleteCandle(id);
            if (exists)
                return new ResponseEntity<Candle>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
