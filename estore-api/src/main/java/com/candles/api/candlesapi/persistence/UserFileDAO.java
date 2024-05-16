package com.candles.api.candlesapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.candles.api.candlesapi.controller.CandleController;
import com.candles.api.candlesapi.model.Candle;
import com.candles.api.candlesapi.model.CreditCard;
import com.candles.api.candlesapi.model.User;
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
public class UserFileDAO implements UserDAO {

    private static final Logger LOG = Logger.getLogger(CandleController.class.getName());
    private CandleDAO candleDao;

    Map<Integer,User> users;   // Provides a local cache of the users objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between user
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new users
    private String filename;    // Filename to read from and write to

    /**
     * Creates a user File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${user.file}") String filename ,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }
    
    /**
     * Generates the next id for a new {@linkplain user users}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain user users} from the tree map
     * 
     * @return  The array of {@link user users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Generates an array of {@linkplain user users} from the tree map for any
     * {@linkplain user users} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain user users}
     * in the tree map
     * 
     * @return  The array of {@link user users}, may be empty
     */
    private User[] getUsersArray(String containsText) {
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getName().contains(containsText)) {
                userArrayList.add(user);
            }
        }
        return userArrayList.toArray(new User[userArrayList.size()]);
    }

    /**
     * Saves the {@linkplain user users} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link User user} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    boolean save() throws IOException {
        User[] userArray = getUsers();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads {@linkplain user users} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of user
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (User user : userArray) {
            users.put(user.getId(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    public User createUser(User user) throws IOException {
        synchronized(users) {
            // We create a new user object because the id field is immutable
            // and we need to assign the next unique id
            User newuser = new User(nextId(),user.getName(),user.getCart(),user.getAdmin(),user.getPassword(),user.getCC());
            users.put(newuser.getId(),newuser);
            save(); // may throw an IOException
            return newuser;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    public User[] getUsers() throws IOException {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    public User getUser(int id) {
        synchronized(users) {
            if (users.containsKey(id))
                return users.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    public User[] findUsers(String name) throws IOException {
        synchronized(users) {
            return getUsersArray(name);
        }
    }

    /**
    ** {@inheritDoc}
     */
    public User updateUser(User user) throws IOException {
        synchronized(users) {
            if (users.containsKey(user.getId()) == false)
                return null;  // user does not exist

            users.put(user.getId(),user);
            save(); // may throw an IOException
            return user;
        }
    }

    /**
    ** {@inheritDoc}
     * @throws IOException 
     */
    public Candle adminCreateCandle(Candle candle) throws IOException{
        return candleDao.createCandle(candle);
    }

    /**
    ** {@inheritDoc}
     * @throws IOException 
     */
    public Candle adminUpdateCandle(Candle candle) throws IOException{
        return candleDao.updateCandle(candle);
    }

    /**
    ** {@inheritDoc}
     * @throws IOException 
     */
    public boolean adminDeleteCandle(int id) throws IOException{
        return candleDao.deleteCandle(id);
    }

    /**
    ** {@inheritDoc}
     * @throws IOException 
     */
    public CreditCard addCreditCard(int id, CreditCard creditCard) throws IOException{
        synchronized(users) {
            if (users.containsKey(id)) {
                return users.get(id).setCC(creditCard);
            } else {
                return null;
            }
        }
    }
}